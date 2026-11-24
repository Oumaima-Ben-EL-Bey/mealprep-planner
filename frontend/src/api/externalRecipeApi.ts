import axios from 'axios'
import type { RecipeSummary } from '../types/recipe'

export async function searchRecipes(search: string, category: string): Promise<RecipeSummary[]> {
    const response = await axios.get<RecipeSummary[]>('/api/external/recipes', {
        params: { search, category },
    })
    return response.data
}