import type {RecipePickerProps} from "../types/mealPlan.ts";
import {useEffect, useState} from "react";
import type {RecipeSummary} from "../types/recipe.ts";
import {getRecipes} from "../api/recipeApi.ts";
import {categoryLabel} from "../labels.ts";

function RecipePicker({onSelect, onCancel}: RecipePickerProps) {
    const [recipes, setRecipes] = useState<RecipeSummary[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);

    useEffect(() => {
        getRecipes().then(setRecipes).catch(() => setError(true)).finally(() => setLoading(false));

    }, []);


    return (
        <div>
            <h2>Rezept auswählen</h2>
            {loading && <p>Rezepte werden geladen...</p>}
            {error && <p>Rezepte können nicht geladen werden.</p>}
            {!loading && !error && recipes.length === 0 && (<p>Du hast noch keine gespeicherten Rezepte</p>)}


            <ul>
                {recipes.map((recipe) => {
                    const id = recipe.id;
                    if (id === null) return null;
                    return (
                        <li key={id}>
                            <button type={"button"} onClick={() => onSelect(id)}>
                                {recipe.title}({categoryLabel(recipe.category)})
                            </button>
                        </li>
                    )
                })}
            </ul>
            <button type={"button"} onClick={onCancel}>Abbrechen</button>
        </div>
    );
}

export default RecipePicker;