package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.entity.MealPlanEntry;
import de.gruppe1.mealprep.entity.MealType;
import de.gruppe1.mealprep.entity.Recipe;
import de.gruppe1.mealprep.exception.NotFoundException;
import de.gruppe1.mealprep.repository.MealPlanEntryRepository;
import de.gruppe1.mealprep.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Service
public class MealPlanService {
    private final MealPlanEntryRepository mealPlanEntryRepository;
    private final RecipeRepository recipeRepository;


    public MealPlanService(MealPlanEntryRepository mealPlanEntryRepository, RecipeRepository recipeRepository) {
        this.mealPlanEntryRepository = mealPlanEntryRepository;
        this.recipeRepository = recipeRepository;
    }

    @Transactional(readOnly = true)
    public List<MealPlanEntry> getPlan(){
        return mealPlanEntryRepository.findAll();
    }

    @Transactional
    public  MealPlanEntry assign(DayOfWeek day, MealType mealType, Long recipeId){
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        if(optionalRecipe.isEmpty()){
            throw new NotFoundException("Rezept nicht gefunden");
        }
        Recipe recipe = optionalRecipe.get();

        Optional<MealPlanEntry> optionalMealPlanEntry = mealPlanEntryRepository.findByDayOfWeekAndMealType(day,mealType);
        MealPlanEntry entry;
        if(optionalMealPlanEntry.isPresent()){
            entry = optionalMealPlanEntry.get();
        } else {
            entry = new MealPlanEntry();
            entry.setDayOfWeek(day);
            entry.setMealType(mealType);
        }

        entry.setRecipe(recipe);
        return mealPlanEntryRepository.save(entry);
    }

    @Transactional
    public void removeEntry(DayOfWeek day, MealType mealType){
        mealPlanEntryRepository.deleteByDayOfWeekAndMealType(day,mealType);
    }

    @Transactional
    public void clearPlan(){
        mealPlanEntryRepository.deleteAll();
    }

}
