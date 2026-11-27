import type {RecipeSummary} from "../types/recipe.ts";
import RecipeCard from "./RecipeCard.tsx";

interface RecipeCardListProps {
    recipes: RecipeSummary[]
    onToggleFavorite?: (recipe: RecipeSummary) => void
}

function RecipeCardList({recipes,onToggleFavorite}: RecipeCardListProps) {
    if (recipes.length === 0) {
        return <p className="status">Keine Rezepte gefunden.</p>
    }
    return (
        <div className="recipe-grid">
            {recipes
                .map((rec) => <RecipeCard recipe={rec} key={rec.id ?? rec.externalId} onToggleFavorite={onToggleFavorite}/>)}
        </div>
    );
}

export default RecipeCardList;