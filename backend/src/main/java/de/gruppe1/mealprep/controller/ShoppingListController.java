package de.gruppe1.mealprep.controller;

import de.gruppe1.mealprep.dto.CheckItemRequestDto;
import de.gruppe1.mealprep.dto.ManualItemRequestDto;
import de.gruppe1.mealprep.dto.ShoppingListItemDto;
import de.gruppe1.mealprep.service.ShoppingListService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopping-list")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping
    public List<ShoppingListItemDto> getList() {
        return shoppingListService.getList();
    }

    @PostMapping("/items")
    public ResponseEntity<ShoppingListItemDto> addItem(@Valid @RequestBody ManualItemRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shoppingListService.addManualItem(request));
    }

    @PatchMapping("/items/{id}")
    public ShoppingListItemDto checkItem(@PathVariable Long id, @Valid @RequestBody CheckItemRequestDto request) {
        return shoppingListService.setChecked(id, request.checked());
    }

    @DeleteMapping("/checked-items")
    public ResponseEntity<Void> deleteCheckedItems() {
        shoppingListService.deleteCheckedItems();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/generate")
    public List<ShoppingListItemDto> generate() {
        return shoppingListService.generateFromMealPlan();
    }
}