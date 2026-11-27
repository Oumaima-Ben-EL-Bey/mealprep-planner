import axios from 'axios'
import type {RandomMode, RecipeDetail, RecipeSummary} from '../types/recipe'

export async function searchRecipes(search: string, category: string): Promise<RecipeSummary[]> {
    const response = await axios.get<RecipeSummary[]>('/api/external/recipes', {
        params: {search, category},
    })
    return response.data
}

export async function getExternalRecipe(mealDbId: string): Promise<RecipeDetail> {
    const response = await axios.get<RecipeDetail>(`/api/external/recipes/${mealDbId}`)
    return response.data
}

export async function getRandomRecipe(mode: RandomMode, category: string): Promise<RecipeDetail> {
    const response = await axios.get<RecipeDetail>('/api/external/recipes/random', {
        params: {mode, category},
    })
    return response.data
}