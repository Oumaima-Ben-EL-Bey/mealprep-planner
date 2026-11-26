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
        <div className="modal-backdrop" onClick={onCancel}>
            <div className="modal" onClick={(event) => event.stopPropagation()}>
                <h2>Rezept auswählen</h2>
                {loading && <p className="status">Rezepte werden geladen...</p>}
                {error && <p className="alert-error">Rezepte können nicht geladen werden.</p>}
                {!loading && !error && recipes.length === 0 && (
                    <p className="status">Du hast noch keine gespeicherten Rezepte.</p>
                )}
                <ul className="picker-list">
                    {recipes.map((recipe) => {
                        const id = recipe.id;
                        if (id === null) return null;
                        return (
                            <li key={id}>
                                <button type="button" className="picker-item" onClick={() => onSelect(id)}>
                                    <span>{recipe.title}</span>
                                    <span className="badge">{categoryLabel(recipe.category)}</span>
                                </button>
                            </li>
                        )
                    })}
                </ul>
                <button type="button" className="btn btn-secondary" onClick={onCancel}>Abbrechen</button>
            </div>
        </div>
    );
}

export default RecipePicker;