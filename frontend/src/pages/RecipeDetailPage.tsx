import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import axios from 'axios'
import type { RecipeDetail } from '../types/recipe'
import { getExternalRecipe } from '../api/externalRecipeApi'
import { categoryLabel, regionLabel } from '../labels'
import IngredientList from '../components/IngredientList'
import Icon from '../components/Icon'
import { CATEGORY_ICONS, UI_ICONS } from '../icons'
import {getRecipeById} from '../api/recipeApi'

function RecipeDetailPage() {
    const { id, mealDbId } = useParams()
    const [recipe, setRecipe] = useState<RecipeDetail | null>(null)
    const [loading, setLoading] = useState(Boolean(id || mealDbId))
    const [error, setError] = useState<string | null>(null)


    //Anpassung das auch interne rezepte geladen werden können
    useEffect(() => {
        async function loadRecipe() {
            try {
                if (id) {
                    const data = await getRecipeById(Number(id));
                    setRecipe(data);
                } else if (mealDbId) {
                    const data = await getExternalRecipe(mealDbId);
                    setRecipe(data);
                }
            } catch (e) {
                const message = axios.isAxiosError(e)
                    ? e.response?.data?.message
                    : undefined;

                setError(
                    message ?? "Das Rezept konnte nicht geladen werden. Bitte versuche es später noch einmal."
                );
            } finally {
                setLoading(false);
            }
        }

        loadRecipe();
    }, [id, mealDbId]);

    if (loading) return <p className="status">Rezept wird geladen …</p>
    if (error) return <p className="alert-error">{error}</p>
    if (!recipe) return null

    return (
        <article className="detail">
            <h1>{recipe.title}</h1>
            {recipe.imageUrl && <img className="detail-image" src={recipe.imageUrl} alt={recipe.title} />}
            <div className="card-meta">
                <span className="badge">
                    {CATEGORY_ICONS[recipe.category] && <img src={CATEGORY_ICONS[recipe.category]} alt="" />}
                    {categoryLabel(recipe.category)}
                </span>
                {recipe.region && (
                    <span className="region"><Icon svg={UI_ICONS.region} size={16} />{regionLabel(recipe.region)}</span>
                )}
            </div>

            <h2 className="section-title"><Icon svg={UI_ICONS.zutaten} size={22} />Zutaten</h2>
            <IngredientList ingredients={recipe.ingredients} />

            <h2 className="section-title"><Icon svg={UI_ICONS.zubereitung} size={22} />Zubereitung</h2>
            <ol className="steps">
                {recipe.instructions
                    .split(/\r?\n/)
                    .map((line) => line.trim())
                    .filter((line) => line !== '' && !/^(step\s*)?\d+[.):]?$/i.test(line))
                    .map((line, index) => (
                        <li key={index}>{line}</li>
                    ))}
            </ol>
        </article>
    )
}

export default RecipeDetailPage