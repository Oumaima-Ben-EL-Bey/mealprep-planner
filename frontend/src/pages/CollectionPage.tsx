import {useEffect, useState} from "react";
import type {RecipeSummary} from "../types/recipe.ts";
import {getRecipes} from "../api/recipeApi.ts";
import RecipeCardList from "../components/RecipeCardList.tsx";
import {Link} from "react-router-dom";

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
            <div>
                <div style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center"
                }}>
            <h1>Meine Rezepte</h1>
                    <Link
                        to="/recipes/new"
                        style={{
                            backgroundColor: "#16a34a",
                            color: "white",
                            padding: "12px 20px",
                            borderRadius: "8px",
                            textDecoration: "none",
                            fontWeight: "600",
                            display: "inline-block"
                        }}>

                        + Rezept erstellen

                    </Link>
                </div>
            <p>Deine persönliche Sammlung...</p>

            </div>


            {/* loading zeigen */}
            {isLoading && (
                <div style={{
                    padding: "40px",
                    textAlign: "center",
                    backgroundColor: "#f9fafb",
                    borderRadius: "12px"
                }}>
                    <p>Deine Rezepte werden geladen...</p>
                </div>
            )}
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