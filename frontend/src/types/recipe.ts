export interface Ingredient {
    name: string,
    measure: string
}

export interface RecipeSummary {
    id: number | null,
    externalId: string | null,
    title: string,
    imageUrl: string | null,
    category: string,
    region: string | null,
    favorite: boolean
}

export interface RecipeDetail extends RecipeSummary{
    instructions: string,
    ingredients: Ingredient[]
}