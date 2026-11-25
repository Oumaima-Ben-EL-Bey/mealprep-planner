import {useEffect, useState} from "react";
import type {RecipeSummary} from "../types/recipe.ts";
import {getRecipes} from "../api/recipeApi.ts";
import RecipeCardList from "../components/RecipeCardList.tsx";

function CollectionPage() {
    const [recipes, setRecipes] = useState<RecipeSummary[]>([]);
    const [isLoading, setIsLoading]  = useState(true);
    const [error, setError] = useState<string | null>(null);


    useEffect(() => {
        async function loadRecipes() {
            setIsLoading(true);

            try {
                const data = await getRecipes();
                setRecipes(data);

            } catch {
                setError("Rezepte konnten nicht geladen werden");

            } finally {
                setIsLoading(false);
            }
        }

        loadRecipes();
    }, []);

    return (
        <div>
            <h1>Meine Rezepte</h1>
            {/* loading zeigen */}
            {isLoading && <p>Rezepte werden geladen...</p>}
            {error && <p>{error}</p>}

            {/*wenn keine rezepte und wenn es nicht läd keine rezepte anzeigen */}
            {!isLoading && recipes.length === 0 && !error && (
                <p>Du hast noch keine Rezepte.</p>)}

            {!isLoading && recipes.length > 0 && (
                <RecipeCardList recipes={recipes}/>
            )}

        </div>
    );
}

export default CollectionPage;