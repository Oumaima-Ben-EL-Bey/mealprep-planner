import {useEffect, useState} from "react";
import type {RecipeSummary} from "../types/recipe.ts";
import {getRecipes, setFavorite} from "../api/recipeApi.ts";
import RecipeCardList from "../components/RecipeCardList.tsx";
import {Link} from "react-router-dom";
import SearchBar from "../components/SearchBar.tsx";
import CategorySelect from "../components/CategorySelect.tsx";

function CollectionPage() {
    const [recipes, setRecipes] = useState<RecipeSummary[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [term, setTerm] = useState<string>("");
    const [category, setCategory] = useState<string>("")
    const [onlyFavorites, setOnlyFavorites] = useState(false);

    function filterRecipes(): RecipeSummary[]{
        let result : RecipeSummary[] = recipes;
        if(onlyFavorites){
            result = result.filter((rec)=>rec.favorite);
        }
        if (term.trim() !== "") {
            const search = term.trim().toLowerCase();
            result = result.filter((rec) => rec.title.toLowerCase().includes(search));
        }
        if(category !== ""){
            result= result.filter((rec)=> rec.category===category);
        }
        return result;
    }
    useEffect(() => {
        async function loadRecipes() {
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


    async function handleFavoriteToggle(recipe: RecipeSummary) {
        if (recipe.id === null) return;
        try {
            const updated = await setFavorite(recipe.id, !recipe.favorite);
            setRecipes((current) => current.map((rec) => rec.id === updated.id ? updated : rec));
        } catch {
            setError("Der Favorit konnte nicht gespeichert werden.");
        }
    }


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
                <SearchBar onSearch={setTerm}/>
                <CategorySelect value={category} onChange={setCategory}/>
                <button type="button"
                        className={onlyFavorites ? "chip active" : "chip"}
                        onClick={() => setOnlyFavorites((current) => !current)}>
                    ★ Nur Favoriten
                </button>
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
                <RecipeCardList recipes={filterRecipes()} onToggleFavorite={handleFavoriteToggle}/>
            )}

        </div>
    );
}

export default CollectionPage;