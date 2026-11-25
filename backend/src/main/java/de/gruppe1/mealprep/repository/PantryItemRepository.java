package de.gruppe1.mealprep.repository;

import de.gruppe1.mealprep.entity.PantryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PantryItemRepository extends JpaRepository<PantryItem, Long> {
}