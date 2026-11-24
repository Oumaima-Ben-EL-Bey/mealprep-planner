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
    useEffect(() => {
        searchRecipes(term, category).then(data => setRecipes(data))
            .catch(error => console.log(error))
    }, [category, term]);

   
    return (
        <div>
            <h1>Rezepte entdecken</h1>
            <SearchBar onSearch={setTerm}/>
            <CategorySelect value={category} onChange={setCategory} />
            <RecipeCardList recipes={recipes}/>
        </div>
    );
}

export default DiscoverPage;