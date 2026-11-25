package de.gruppe1.mealprep.repository;

import de.gruppe1.mealprep.entity.MealPlanEntry;
import de.gruppe1.mealprep.entity.MealType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.Optional;

public interface MealPlanEntryRepository extends JpaRepository<MealPlanEntry, Long> {

    Optional<MealPlanEntry> findByDayOfWeekAndMealType(DayOfWeek dayOfWeek, MealType mealType);

    void deleteByDayOfWeekAndMealType(DayOfWeek dayOfWeek, MealType mealType);

    boolean existsByRecipeId(Long recipeId);

    void deleteByRecipeId(Long recipeId);
}
