package de.gruppe1.mealprep.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;

@Entity
@Table(
        name="meal_plan_entries",
        uniqueConstraints = @UniqueConstraint(columnNames = {"day_of_week", "meal_type"})
)
@Getter
@Setter
@NoArgsConstructor
public class MealPlanEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Enumerated(EnumType.STRING)
    @Column(name ="meal_type", nullable = false)
    private MealType mealType;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name= "recipe_id", nullable = false)
    private Recipe recipe;

}
