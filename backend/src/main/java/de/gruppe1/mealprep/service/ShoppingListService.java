package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.dto.ManualItemRequestDto;
import de.gruppe1.mealprep.dto.ShoppingListItemDto;
import de.gruppe1.mealprep.entity.MealPlanEntry;
import de.gruppe1.mealprep.entity.PantryItem;
import de.gruppe1.mealprep.entity.RecipeIngredient;
import de.gruppe1.mealprep.entity.ShoppingListItem;
import de.gruppe1.mealprep.exception.NotFoundException;
import de.gruppe1.mealprep.mapper.ShoppingListMapper;
import de.gruppe1.mealprep.repository.MealPlanEntryRepository;
import de.gruppe1.mealprep.repository.PantryItemRepository;
import de.gruppe1.mealprep.repository.ShoppingListItemRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShoppingListService {

    private final ShoppingListItemRepository itemRepository;
    private final ShoppingListMapper mapper;
    private final MealPlanEntryRepository mealPlanEntryRepository;
    private final PantryItemRepository pantryItemRepository;

    public ShoppingListService(ShoppingListItemRepository itemRepository, ShoppingListMapper mapper, MealPlanEntryRepository mealPlanEntryRepository, PantryItemRepository pantryItemRepository) {
        this.itemRepository = itemRepository;
        this.mapper = mapper;
        this.mealPlanEntryRepository = mealPlanEntryRepository;
        this.pantryItemRepository = pantryItemRepository;
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
        Set<String> pantry = pantryNames();

        Map<String, ShoppingListItem> grouped = new LinkedHashMap<>();
        Map<String, List<String>> measures = new LinkedHashMap<>();
        for (MealPlanEntry entry : mealPlanEntryRepository.findAll()) {
            for (RecipeIngredient ingredient : entry.getRecipe().getIngredients()) {
                String name = ingredient.getName() == null ? "" : ingredient.getName().trim();
                if (name.isEmpty()) {
                    continue;
                }
                String key = name.toLowerCase(Locale.ROOT);
                grouped.computeIfAbsent(key, k -> newGeneratedItem(name, pantry.contains(k)));
                measures.computeIfAbsent(key, k -> new ArrayList<>()).add(ingredient.getMeasure());
            }
        }
        grouped.forEach((key, item) -> item.setMeasure(MeasureCombiner.combine(measures.get(key))));

        itemRepository.saveAll(grouped.values());
        return getList();
    }

    private ShoppingListItem newGeneratedItem(String name, boolean inPantry) {
        ShoppingListItem item = new ShoppingListItem();
        item.setName(name);
        item.setMeasure("");
        item.setInPantry(inPantry);
        return item;
    }



    private Set<String> pantryNames() {
        return pantryItemRepository.findAll().stream()
                .map(PantryItem::getName)
                .map(name -> name.trim().toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());
    }
}