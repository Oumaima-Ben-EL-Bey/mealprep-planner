import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import axios from 'axios'
import type { RecipeDetail } from '../types/recipe'
import { getExternalRecipe } from '../api/externalRecipeApi'
import { categoryLabel, regionLabel } from '../labels'
import IngredientList from '../components/IngredientList'

function RecipeDetailPage() {
    const { mealDbId } = useParams()
    const [recipe, setRecipe] = useState<RecipeDetail | null>(null)
    const [loading, setLoading] = useState(Boolean(mealDbId))
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        if (!mealDbId) return
        getExternalRecipe(mealDbId)
            .then((data) => setRecipe(data))
            .catch((e) => {
                const message = axios.isAxiosError(e) ? e.response?.data?.message : undefined
                setError(message ?? 'Das Rezept konnte nicht geladen werden. Bitte versuche es später noch einmal.')
            })
            .finally(() => setLoading(false))
    }, [mealDbId])

    if (loading) return <p>Rezept wird geladen …</p>
    if (error) return <p>{error}</p>
    if (!recipe) return null

    return (
        <article>
            <h1>{recipe.title}</h1>
            {recipe.imageUrl && <img src={recipe.imageUrl} alt={recipe.title} />}
            <p>Kategorie: {categoryLabel(recipe.category)}</p>
            {recipe.region && <p>Region: {regionLabel(recipe.region)}</p>}

            <h2>Zutaten</h2>
            <IngredientList ingredients={recipe.ingredients} />

            <h2>Zubereitung</h2>
            {recipe.instructions
                .split(/\r?\n/)
                .filter((line) => line.trim() !== '')
                .map((line, index) => (
                    <p key={index}>{line}</p>
                ))}
        </article>
    )
}

export default RecipeDetailPage