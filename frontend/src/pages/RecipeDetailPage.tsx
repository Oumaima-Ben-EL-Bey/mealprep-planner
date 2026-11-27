import { useEffect, useState } from 'react'
import {useNavigate, useParams} from 'react-router-dom'
import axios from 'axios'
import type { RecipeDetail } from '../types/recipe'
import { getExternalRecipe } from '../api/externalRecipeApi'
import { categoryLabel, regionLabel } from '../labels'
import IngredientList from '../components/IngredientList'
import Icon from '../components/Icon'
import { CATEGORY_ICONS, UI_ICONS } from '../icons'
import {deleteRecipe, getRecipeById} from '../api/recipeApi'
import ConfirmDialog from "../components/ConfirmDialog.tsx";

const STEP_NUMBER_ONLY = /^((step|schritt)\s*)?\d+[.):]?$/i
const STEP_HEADING = /^(?:step|schritt)\s*\d+\s*[-–—:.)]\s*(.+)$/i
function RecipeDetailPage() {
    const { id, mealDbId } = useParams()
    const [recipe, setRecipe] = useState<RecipeDetail | null>(null)
    const [loading, setLoading] = useState(Boolean(id || mealDbId))
    const [error, setError] = useState<string | null>(null)
    const [showDeleteDialog, setShowDeleteDialog] = useState(false)

    const navigate = useNavigate()


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

    //Delete mit überprüfung falls es die id nicht gibt abruch
    async function handleDelete() {
        if (!recipe || recipe.id === null) return;

        try {
            await deleteRecipe(recipe.id);
            navigate('/recipes');
        } catch (e) {
            const message = axios.isAxiosError(e)
                ? e.response?.data?.message
                : undefined;

            setError(
                message ?? 'Das Rezept konnte nicht gelöscht werden.'
            );
            setShowDeleteDialog(false);
        }
    }

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

            {/*Button wird nur angezeigt wenn es unser eigenes rezept ist von dort aus können wir editieren*/}
            {recipe.id !== null && <button
                onClick={()=> navigate(`/recipes/${recipe.id}/edit`)}>Bearbeiten</button> }


            {/*Button fürs Löschen*/}
            {recipe.id !== null && <button
                onClick={()=> setShowDeleteDialog(true)}>Löschen</button> }




            <h2 className="section-title"><Icon svg={UI_ICONS.zutaten} size={22} />Zutaten</h2>
            <IngredientList ingredients={recipe.ingredients} />

            {/*Willst du das wirklich löschen anzeigen lassen*/}
            {showDeleteDialog && (
                <ConfirmDialog
                    title="Rezept löschen?"
                    message="Möchtest du dieses Rezept wirklich löschen? Diese Aktion kann nicht rückgängig gemacht werden."
                    onCancel={() => setShowDeleteDialog(false)}
                    onConfirm={handleDelete}
                />
            )}
            <h2 className="section-title"><Icon svg={UI_ICONS.zubereitung} size={22} />Zubereitung</h2>
            <div className="instructions">
                {recipe.instructions
                    .split(/\r?\n/)
                    .map((line) => line.trim())
                    .filter((line) => line !== '' && !STEP_NUMBER_ONLY.test(line))
                    .map((line, index) => {
                        const heading = line.match(STEP_HEADING)
                        return heading ? <h3 key={index}>{heading[1]}</h3> : <p key={index}>{line}</p>
                    })}
            </div>
        </article>
    )
}

export default RecipeDetailPage