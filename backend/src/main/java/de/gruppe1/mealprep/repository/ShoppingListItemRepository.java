package de.gruppe1.mealprep.repository;

import de.gruppe1.mealprep.entity.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {

    void deleteByCheckedTrue();

    void deleteByManualFalse();
}