import type {RecipeSummary} from "../types/recipe.ts";
import RecipeCard from "./RecipeCard.tsx";

interface RecipeCardListProps{
    recipes: RecipeSummary[]
}
function RecipeCardList({recipes}:RecipeCardListProps) {
    if (recipes.length === 0) {
        return <p>Keine Rezepte gefunden.</p>
    }
    return (
        <div>
            {recipes
                .map((rec)=><RecipeCard recipe={rec} key={rec.id??rec.externalId}/>)}
        </div>
    );
}

export default RecipeCardList;