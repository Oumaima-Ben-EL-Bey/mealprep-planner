import axios from 'axios'
import type {RecipeSummary, RecipeDetail, RecipeRequest} from '../types/recipe'


//Einfach alle rezepte ausgeben
export async function getRecipes(): Promise<RecipeSummary[]> {
    const response = await axios.get<RecipeSummary[]>('/api/recipes')
    return response.data
}

//Rezept der id ausgeben
export async function getRecipeById(id: number): Promise<RecipeDetail> {
    const response = await axios.get<RecipeDetail>(`/api/recipes/${id}`)
    return response.data
}

//Rezept als Favorite setzen/entsetzen
export async function setFavorite(id: number, status: boolean): Promise<RecipeSummary> {
    const response = await axios.patch<RecipeSummary>(`/api/recipes/${id}/favorite`, {favorite: status})
    return response.data
}

//ein Rezept erstellen
export async function createRecipe(data: RecipeRequest): Promise<RecipeDetail> {
    const response = await axios.post<RecipeDetail>('/api/recipes', data)
    return response.data
}

//Rezept updaten
export async function updateRecipe(
    id: number,
    data: RecipeRequest
): Promise<RecipeDetail> {
    const response = await axios.put<RecipeDetail>(`/api/recipes/${id}`, data)
    return response.data
}

//ein Rezept löschen
export async function deleteRecipe(id: number): Promise<void> {
    await axios.delete(`/api/recipes/${id}`)
}