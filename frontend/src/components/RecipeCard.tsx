import type {RecipeSummary} from "../types/recipe.ts";
import {categoryLabel, regionLabel} from "../labels.ts";
import {Link} from "react-router-dom";
import Icon from "./Icon.tsx";
import {CATEGORY_ICONS, UI_ICONS} from "../icons.ts";

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
        <Link to={getPath()} className="recipe-card">
            {recipe.imageUrl !== null && (
                <div className="recipe-card-image">
                    <img src={recipe.imageUrl} alt={recipe.title}/>
                </div>
            )}
            <div className="recipe-card-body">
                <h4>{recipe.title}</h4>
                <div className="card-meta">
                    <span className="badge">
                        {CATEGORY_ICONS[recipe.category] && <img src={CATEGORY_ICONS[recipe.category]} alt=""/>}
                        {categoryLabel(recipe.category)}
                    </span>
                    {recipe.region !== null && (
                        <span className="region"><Icon svg={UI_ICONS.region} size={16}/>{regionLabel(recipe.region)}</span>
                    )}
                </div>
            </div>
        </Link>
    );
}

export default RecipeCard;