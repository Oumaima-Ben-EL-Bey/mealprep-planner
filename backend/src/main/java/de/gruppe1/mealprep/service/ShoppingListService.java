package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.dto.ManualItemRequestDto;
import de.gruppe1.mealprep.dto.ShoppingListItemDto;
import de.gruppe1.mealprep.entity.MealPlanEntry;
import de.gruppe1.mealprep.entity.RecipeIngredient;
import de.gruppe1.mealprep.entity.ShoppingListItem;
import de.gruppe1.mealprep.exception.NotFoundException;
import de.gruppe1.mealprep.mapper.ShoppingListMapper;
import de.gruppe1.mealprep.repository.MealPlanEntryRepository;
import de.gruppe1.mealprep.repository.ShoppingListItemRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ShoppingListService {

    private final ShoppingListItemRepository itemRepository;
    private final ShoppingListMapper mapper;
    private final MealPlanEntryRepository mealPlanEntryRepository;

    public ShoppingListService(ShoppingListItemRepository itemRepository, ShoppingListMapper mapper, MealPlanEntryRepository mealPlanEntryRepository) {
        this.itemRepository = itemRepository;
        this.mapper = mapper;
        this.mealPlanEntryRepository = mealPlanEntryRepository;
    }

    public List<ShoppingListItemDto> getList() {
        return itemRepository.findAll(Sort.by("checked", "name"))
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public ShoppingListItemDto addManualItem(ManualItemRequestDto request) {
        ShoppingListItem item = new ShoppingListItem();
        item.setName(request.name().trim());
        item.setMeasure(request.measure() == null ? "" : request.measure().trim());
        item.setManual(true);
        return mapper.toDto(itemRepository.save(item));
    }

    public ShoppingListItemDto setChecked(Long id, boolean checked) {
        ShoppingListItem item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Artikel nicht gefunden"));
        item.setChecked(checked);
        return mapper.toDto(itemRepository.save(item));
    }

    @Transactional
    public void deleteCheckedItems() {
        itemRepository.deleteByCheckedTrue();
    }

    @Transactional
    public List<ShoppingListItemDto> generateFromMealPlan() {
        itemRepository.deleteByManualFalse();

        Map<String, ShoppingListItem> grouped = new LinkedHashMap<>();
        for (MealPlanEntry entry : mealPlanEntryRepository.findAll()) {
            for (RecipeIngredient ingredient : entry.getRecipe().getIngredients()) {
                String name = ingredient.getName() == null ? "" : ingredient.getName().trim();
                if (name.isEmpty()) {
                    continue;
                }
                String key = name.toLowerCase(Locale.ROOT);
                ShoppingListItem item = grouped.computeIfAbsent(key, k -> newGeneratedItem(name));
                addMeasure(item, ingredient.getMeasure());
            }
        }

        itemRepository.saveAll(grouped.values());
        return getList();
    }

    private ShoppingListItem newGeneratedItem(String name) {
        ShoppingListItem item = new ShoppingListItem();
        item.setName(name);
        item.setMeasure("");
        return item;
    }

    private void addMeasure(ShoppingListItem item, String measure) {
        if (measure == null || measure.isBlank()) {
            return;
        }
        String current = item.getMeasure();
        item.setMeasure(current.isEmpty() ? measure.trim() : current + " + " + measure.trim());
    }
}