package de.gruppe1.mealprep.controller;

import de.gruppe1.mealprep.dto.PantryItemDto;
import de.gruppe1.mealprep.dto.PantryItemRequestDto;
import de.gruppe1.mealprep.entity.PantryItem;
import de.gruppe1.mealprep.mapper.PantryMapper;
import de.gruppe1.mealprep.service.PantryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pantry")
public class PantryController {
    private final PantryService pantryService;
    private final PantryMapper pantryMapper;

    public PantryController(PantryService pantryService, PantryMapper pantryMapper) {
        this.pantryService = pantryService;
        this.pantryMapper = pantryMapper;
    }

    @GetMapping
    public List<PantryItemDto> getAll(){
        List<PantryItemDto> result = new ArrayList<>();
        for (PantryItem item : pantryService.getAll()) {
            result.add(pantryMapper.toDto(item));
        }
        return result;
    }

    @PostMapping
    public ResponseEntity<PantryItemDto> create(@Valid @RequestBody PantryItemRequestDto requestDto){
        PantryItem creates = pantryService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pantryMapper.toDto(creates));
    }

    @PutMapping("/{id}")
    public PantryItemDto update(@PathVariable Long id, @Valid @RequestBody PantryItemRequestDto requestDto){
        return pantryMapper.toDto(pantryService.update(id,requestDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        pantryService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
