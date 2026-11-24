package de.gruppe1.mealprep.repository;

import de.gruppe1.mealprep.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    boolean existsByExternalId(String externalId);

}