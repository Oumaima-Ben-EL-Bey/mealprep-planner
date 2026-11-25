import type { Ingredient } from '../types/recipe'

interface IngredientListProps {
    ingredients: Ingredient[]
}

function IngredientList({ ingredients }: IngredientListProps) {
    return (
        <ul className="tags">
            {ingredients.map((ingredient, index) => (
                <li key={index} className="tag">
                    {ingredient.measure && <span className="tag-measure">{ingredient.measure} </span>}
                    {ingredient.name}
                </li>
            ))}
        </ul>
    )
}

export default IngredientList