
package de.gruppe1.mealprep.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "recipes")
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
    private List<RecipeIngredient> ingredients;
}
