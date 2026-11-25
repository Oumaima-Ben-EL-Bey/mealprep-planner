import type { Ingredient } from '../types/recipe'

interface IngredientListProps {
    ingredients: Ingredient[]
}

function IngredientList({ ingredients }: IngredientListProps) {
    return (
        <ul>
            {ingredients.map((ingredient, index) => (
                <li key={index}>
                    {ingredient.measure} {ingredient.name}
                </li>
            ))}
        </ul>
    )
}

export default IngredientList