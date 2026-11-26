import axios from "axios";
import type {DayOfWeek, MealPlanEntry, MealType} from "../types/mealPlan.ts";

const url = "/api/meal-plan";

export async function getPlan(): Promise<MealPlanEntry[]>{
    const response = await axios.get<MealPlanEntry[]>(url);
    return response.data;
}


export async function assignRecipe(day: DayOfWeek, mealType: MealType, recipeId: number): Promise<MealPlanEntry> {
    const response = await axios.put<MealPlanEntry>(
        `${url}/${day}/${mealType}`, {recipeId}
    );
    return response.data;
}

export async function removeEntry(day: DayOfWeek, mealType: MealType): Promise<void>{
    await axios.delete(`${url}/${day}/${mealType}`);
}

export async function clearPlan(): Promise<void>{
    await axios.delete(url);
}