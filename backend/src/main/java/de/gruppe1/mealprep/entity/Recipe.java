package de.gruppe1.mealprep.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", unique = true)
    private String externalId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    private String region;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private boolean favorite = false;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    // beide Richtungen pflegen, sonst steht recipe_id in der Datenbank nicht drin
    public void addIngredient(RecipeIngredient ingredient) {
        ingredients.add(ingredient);
        ingredient.setRecipe(this);
    }

    public void removeIngredient(RecipeIngredient ingredient) {
        ingredients.remove(ingredient);
        ingredient.setRecipe(null);
    }
}
