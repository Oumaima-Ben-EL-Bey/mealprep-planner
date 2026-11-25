package de.gruppe1.mealprep.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shopping_list_items")
@Getter
@Setter
@NoArgsConstructor
public class ShoppingListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String measure;

    @Column(nullable = false)
    private boolean checked = false;

    @Column(nullable = false)
    private boolean manual = false;

    @Column(name = "in_pantry", nullable = false)
    private boolean inPantry = false;
}