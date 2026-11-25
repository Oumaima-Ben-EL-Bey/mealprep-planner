package de.gruppe1.mealprep.controller;

import de.gruppe1.mealprep.dto.AssignRecipeRequestDto;
import de.gruppe1.mealprep.dto.MealPlanEntryDto;
import de.gruppe1.mealprep.entity.MealPlanEntry;
import de.gruppe1.mealprep.entity.MealType;
import de.gruppe1.mealprep.mapper.MealPlanMapper;
import de.gruppe1.mealprep.service.MealPlanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/meal-plan")
public class MealPlanController {

    private final MealPlanService mealPlanService;
    private final MealPlanMapper mealPlanMapper;

    public MealPlanController(MealPlanService mealPlanService, MealPlanMapper mealPlanMapper) {
        this.mealPlanService = mealPlanService;
        this.mealPlanMapper = mealPlanMapper;
    }

    @GetMapping
    public List<MealPlanEntryDto> getPlan(){
        List<MealPlanEntry> entries = mealPlanService.getPlan();
        List<MealPlanEntryDto> result = new ArrayList<>();
        for (MealPlanEntry entry : entries) {
            MealPlanEntryDto dto = mealPlanMapper.toDto(entry);
            result.add(dto);
        }
        return result;
    }

    @PutMapping("/{day}/{mealType}")
    public MealPlanEntryDto assign(@PathVariable DayOfWeek day,
                                   @PathVariable MealType mealType,
                                   @Valid @RequestBody AssignRecipeRequestDto requestDto){
        return mealPlanMapper.toDto(mealPlanService.assign(day, mealType, requestDto.recipeId()));
    }

    @DeleteMapping("/{day}/{mealType}")
    public ResponseEntity<Void> removeEntry(
            @PathVariable DayOfWeek day,
            @PathVariable MealType mealType
    ){
        mealPlanService.removeEntry(day, mealType);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearPlan(){
        mealPlanService.clearPlan();
        return ResponseEntity.noContent().build();
    }
}
