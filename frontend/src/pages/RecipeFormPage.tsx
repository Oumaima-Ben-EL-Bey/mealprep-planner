import {Link} from "react-router-dom";
import {type ReactElement, type ReactEventHandler, useState} from "react";
import type {RecipeRequest} from "../types/recipe.ts";

function RecipeFormPage() {
    const [recipe, setRecipe] = useState<RecipeRequest>({
        title: "",
        category: "",
        region: null,
        instructions: "",
        imageUrl: null,
        ingredients: []
    })

    function handleTextChange(
        field: "title" | "category" | "region" | "instructions" | "imageUrl",
        value: string
    ) {
        setRecipe({
            ...recipe,
            [field]: value
        });
    }

    function addIngredient() {
        setRecipe({
            ...recipe,
            ingredients: [
                ...recipe.ingredients,
                { name: "", measure: "" }
            ]
        });
    }


    return (
        <div>
            <h1>Rezept bearbeiten</h1>
            <Link to="/recipes">
                ← Zurück zur Sammlung
            </Link>

            <h2>Neues Rezept erstellen</h2>

            <label>
                Rezeptname
                <input
                    type="text"
                    value={recipe.title}
                    onChange={(e) => handleTextChange(
                        "title", e.target.value)}
                    required
                    placeholder={"z. B. Gemüse Pfanne"}
                />
            </label>

            <label>
                Kategorie
                <select
                    value={recipe.category}
                    onChange={(e) => handleTextChange("category", e.target.value)}
                    required
                >
                    <option value="">Kategorie auswählen</option>
                    <option value="Breakfast">Frühstück</option>
                    <option value="Starter">Vorspeise</option>
                    <option value="Beef">Rindfleisch</option>
                    <option value="Chicken">Hähnchen</option>
                    <option value="Dessert">Dessert</option>
                    <option value="Seafood">Fisch & Meeresfrüchte</option>
                    <option value="Vegetarian">Vegetarisch</option>
                    <option value="Pasta">Pasta</option>
                    <option value="Miscellaneous">Sonstiges</option>
                </select>
            </label>

            <label>
                Region / Herkunft
                <input
                    type="text"
                    value={recipe.region ?? ""}
                    onChange={(e) => handleTextChange("region", e.target.value)}
                    placeholder="z. B. Italien"
                />
            </label>


            <label>
                Bild-URL
                <input
                    type="url"
                    value={recipe.imageUrl ?? ""}
                    onChange={(e) => handleTextChange("imageUrl", e.target.value)}
                    placeholder="https://..."
                />
            </label>

            <label>
                Zubereitung
                <textarea
                    value={recipe.instructions}
                    onChange={(e) => handleTextChange("instructions", e.target.value)}
                    rows={6}
                    placeholder="Beschreibe die einzelnen Zubereitungsschritte..."
                    required
                />
            </label>

            <button type="button" onClick={addIngredient}>
                + Zutat hinzufügen
            </button>

        </div>
    );
}

export default RecipeFormPage;