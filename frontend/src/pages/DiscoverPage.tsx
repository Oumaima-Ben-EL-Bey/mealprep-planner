import {useEffect, useState} from "react";
import type {RecipeSummary} from "../types/recipe.ts";
import {searchRecipes} from "../api/externalRecipeApi.ts";
import RecipeCardList from "../components/RecipeCardList.tsx";

function DiscoverPage() {
    const [recipes,setRecipes] = useState<RecipeSummary[]>([]);
    useEffect(() => {
        searchRecipes('','').then(data=> setRecipes(data))
            .catch(error=>console.log(error))
    }, []);
    return (
        <div>
            <h1>Rezepte entdecken</h1>
            <RecipeCardList recipes={recipes}/>
        </div>
    );
}

export default DiscoverPage;