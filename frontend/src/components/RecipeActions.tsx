import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'
import type { RecipeDetail } from '../types/recipe'
import { saveExternalRecipe } from '../api/recipeApi'

type RecipeActionsProps = {
    recipe: RecipeDetail
    onDelete: () => void
    onSaved: (recipe: RecipeDetail) => void
}

function RecipeActions({ recipe, onDelete, onSaved }: RecipeActionsProps) {
    const [saving, setSaving] = useState(false)
    const [message, setMessage] = useState<string | null>(null)
    const navigate = useNavigate()

    async function handleSave() {
        if (!recipe.externalId) return

        setSaving(true)
        setMessage(null)

        try {
            const savedRecipe = await saveExternalRecipe(recipe.externalId)
            onSaved(savedRecipe)
            setMessage('Rezept wurde erfolgreich gespeichert.')
        } catch (error) {
            if (axios.isAxiosError(error) && error.response?.status === 409) {
                setMessage('Dieses Rezept ist bereits in deiner Sammlung.')
            } else {
                setMessage('Das Rezept konnte nicht gespeichert werden.')
            }
        } finally {
            setSaving(false)
        }
    }

    return (
        <div>
            {recipe.id === null && (
                <button onClick={handleSave} disabled={saving}>
                    {saving ? 'Wird gespeichert …' : 'In meiner Sammlung speichern'}
                </button>
            )}

            {recipe.id !== null && recipe.externalId === null && (
                <button onClick={() => navigate(`/recipes/${recipe.id}/edit`)}>
                    Rezept bearbeiten
                </button>
            )}

            {recipe.id !== null && recipe.externalId !== null && (
                <button onClick={onDelete}>
                    Löschen
                </button>
            )}

            {recipe.id !== null && recipe.externalId === null && (
                <button onClick={onDelete}>
                    Löschen
                </button>
            )}

            {message && <p role="status">{message}</p>}
        </div>
    )
}

export default RecipeActions