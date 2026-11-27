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
    private final TranslationService translationService;

    public ShoppingListService(ShoppingListItemRepository itemRepository, ShoppingListMapper mapper, MealPlanEntryRepository mealPlanEntryRepository, PantryItemRepository pantryItemRepository, TranslationService translationService) {
        this.itemRepository = itemRepository;
        this.mapper = mapper;
        this.mealPlanEntryRepository = mealPlanEntryRepository;
        this.pantryItemRepository = pantryItemRepository;
        this.translationService = translationService;
    }

    public List<ShoppingListItemDto> getList() {
        return itemRepository.findAll(Sort.by("checked", "displayName"))
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public ShoppingListItemDto addManualItem(ManualItemRequestDto request) {
        ShoppingListItem item = new ShoppingListItem();
        item.setName(request.name().trim());
        item.setDisplayName(request.name().trim());
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

        // 1. Alle Zutaten aus dem Wochenplan einsammeln, ein mehrfach eingeplantes Rezept zählt mehrfach
        List<RecipeIngredient> ingredients = new ArrayList<>();
        for (MealPlanEntry entry : mealPlanEntryRepository.findAll()) {
            for (RecipeIngredient ingredient : entry.getRecipe().getIngredients()) {
                if (ingredient.getName() != null && !ingredient.getName().isBlank()) {
                    ingredients.add(ingredient);
                }
            }
        }

        // 2. Jeden Namen einmal ins Deutsche übersetzen, bekannte Namen kommen aus dem Cache
        List<String> names = ingredients.stream().map(i -> i.getName().trim()).distinct().toList();
        List<String> germanNames = translationService.toGerman(names);
        Map<String, String> german = new HashMap<>();
        for (int i = 0; i < names.size(); i++) {
            german.put(names.get(i), germanNames.get(i));
        }

        // 3. Nach dem deutschen Namen gruppieren, so landen "Onion" und "Zwiebel" in einer Zeile
        Map<String, ShoppingListItem> grouped = new LinkedHashMap<>();
        Map<String, List<String>> measures = new LinkedHashMap<>();
        for (RecipeIngredient ingredient : ingredients) {
            String name = ingredient.getName().trim();
            String germanName = german.get(name);
            String key = germanName.toLowerCase(Locale.ROOT);

            ShoppingListItem item = grouped.computeIfAbsent(key, k -> newGeneratedItem(name, germanName));
            // Vorrat kann deutsch ("Reis") oder englisch ("Rice") gepflegt sein
            if (pantry.contains(key) || pantry.contains(name.toLowerCase(Locale.ROOT))) {
                item.setInPantry(true);
            }
            measures.computeIfAbsent(key, k -> new ArrayList<>()).add(ingredient.getMeasure());
        }
        grouped.forEach((key, item) -> item.setMeasure(MeasureCombiner.combine(measures.get(key))));

        itemRepository.saveAll(grouped.values());
        return getList();
    }

    private ShoppingListItem newGeneratedItem(String name, String displayName) {
        ShoppingListItem item = new ShoppingListItem();
        item.setName(name);
        item.setDisplayName(displayName);
        item.setMeasure("");
        return item;
    }



    private Set<String> pantryNames() {
        return pantryItemRepository.findAll().stream()
                .map(PantryItem::getName)
                .map(name -> name.trim().toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());
    }
}