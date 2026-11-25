import {useEffect, useState} from "react";
import type {RecipeSummary} from "../types/recipe.ts";
import {searchRecipes} from "../api/externalRecipeApi.ts";
import RecipeCardList from "../components/RecipeCardList.tsx";
import SearchBar from "../components/SearchBar.tsx";
import CategorySelect from "../components/CategorySelect.tsx";

function DiscoverPage() {
    const [recipes, setRecipes] = useState<RecipeSummary[]>([]);
    const [term, setTerm] = useState<string>("");
    const [category, setCategory] = useState<string>("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    useEffect(() => {
        searchRecipes(term, category).then(data => setRecipes(data))
            .catch(() => setError("Rezepte konnten nicht geladen werden. Bitte versuche es später noch einmal."))
            .finally(() => setLoading(false))
    }, [category, term]);

    function handleSearch(sterm: string) {
        if(sterm === term){
            return;
        }
        setLoading(true);
        setError(null);
        setTerm(sterm);
    }

    function handleCategory(category: string) {
        setLoading(true);
        setError(null);
        setCategory(category);
    }


    return (
        <div>
            <h1>Rezepte entdecken</h1>
            <SearchBar onSearch={handleSearch}/>
            <CategorySelect value={category} onChange={handleCategory}/>
            {loading ? <h5>Rezepte werden geladen …</h5> : error ? <h5>{error}</h5> :
                <RecipeCardList recipes={recipes}/>}

        </div>
    );
}

export default DiscoverPage;