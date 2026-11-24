import type {RecipeSummary} from "../types/recipe.ts";
import {categoryLabel, regionLabel} from "../labels.ts";
import {Link} from "react-router-dom";

interface RecipeCardProps {
    recipe: RecipeSummary
}

function RecipeCard({recipe}: RecipeCardProps) {
    function getPath(): string {
        if (recipe.id !== null) {
            return `/recipes/${recipe.id}`;

        }
        return `/recipes/external/${recipe.externalId}`
    }

    return (
        <div>
            <Link to={getPath()}>
                <div>
                    {recipe.imageUrl !== null && <img src={recipe.imageUrl} alt={recipe.title} width={50}/>}
                    <h4>{recipe.title}</h4>
                    <p>Kategorie: {categoryLabel(recipe.category)}</p>
                    { recipe.region !==null && <p>Region: {regionLabel(recipe.region)}</p>}
                </div>
            </Link>
        </div>
    );
}

export default RecipeCard;