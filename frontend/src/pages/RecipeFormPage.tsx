import {Link, useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import type {RecipeRequest} from "../types/recipe.ts";
import {createRecipe, getRecipeById, updateRecipe} from "../api/recipeApi.ts";

function RecipeFormPage() {



    const [recipe, setRecipe] = useState<RecipeRequest>({
        title: "",
        category: "",
        region: null,
        instructions: "",
        imageUrl: null,
        ingredients: []
    })


    const { id } = useParams();
    const isEditMode = Boolean(id);
    const navigate = useNavigate();
    const [error, setError] = useState<string | null>(null);
    const [isSaving, setIsSaving] = useState(false);

    useEffect(() => {
        if (!isEditMode || !id) return;

        async function loadRecipe() {
            try {
                const data = await getRecipeById(Number(id));
                setRecipe(data);
            } catch {
                setError("Rezept konnte nicht geladen werden.");
            }
        }

        loadRecipe();
    }, [id, isEditMode]);


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

    function handleIngredientChange(
        index: number,
        field: "name" | "measure",
        value: string
    ) {
        const updatedIngredients = [...recipe.ingredients];

        updatedIngredients[index] = {
            ...updatedIngredients[index],
            [field]: value
        };

        setRecipe({
            ...recipe,
            ingredients: updatedIngredients
        });
    }

    function removeIngredient(index: number) {
        setRecipe({
            ...recipe,
            ingredients: recipe.ingredients.filter((_, i) => i !== index)
        });
    }

    async function handleSubmit(e: React.SubmitEvent<HTMLFormElement>) {
        e.preventDefault();

        if (
            recipe.ingredients.length === 0 ||
            recipe.ingredients.some(
                ingredient => !ingredient.name.trim() || !ingredient.measure.trim()
            )
        ) {
            setError("Bitte füge mindestens eine vollständige Zutat hinzu.");
            return;
        }


        setError(null);
        setIsSaving(true);

        try {
            const cleanedRecipe: RecipeRequest = {
                ...recipe,
                region: recipe.region?.trim() || null,
                imageUrl: recipe.imageUrl?.trim() || null
            };

            if (isEditMode && id) {
                await updateRecipe(Number(id), cleanedRecipe);
            } else {
                await createRecipe(cleanedRecipe);
            }

            navigate("/recipes");
        } catch {
            setError(
                isEditMode
                    ? "Rezept konnte nicht aktualisiert werden."
                    : "Rezept konnte nicht gespeichert werden."
            );
        } finally {
            setIsSaving(false);
        }
    }

    return (
        <div>
            <h1>{isEditMode ? "Rezept bearbeiten" : "Neues Rezept erstellen"}</h1>
            <Link to="/recipes">
                ← Zurück zur Sammlung
            </Link>


            <form onSubmit={handleSubmit}>


                {error && <p>{error}</p>}
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
            {recipe.ingredients.map((ingredient, index) => (
                <div key={index}>
                    <input
                        type="text"
                        placeholder="Zutat"
                        value={ingredient.name}
                        onChange={(e) => handleIngredientChange(index, "name", e.target.value)}
                    />

                    <input
                        type="text"
                        placeholder="Menge"
                        value={ingredient.measure}
                        onChange={(e) => handleIngredientChange(index, "measure", e.target.value)}
                    />
                    <button
                        type="button"
                        onClick={() => removeIngredient(index)}
                    >
                        Entfernen
                    </button>


                </div>
            ))}

                <button type="submit" disabled={isSaving}>
                    {isSaving ? "Speichert..." : "Rezept speichern"}
                </button>
            </form>
        </div>
    );
}

export default RecipeFormPage;