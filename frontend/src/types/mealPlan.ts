import type {RecipeSummary} from "./recipe.ts";

export type DayOfWeek = "MONDAY" | "TUESDAY" | "WEDNESDAY" | "THURSDAY" | "FRIDAY" | "SATURDAY" | "SUNDAY";


export type MealType = "LUNCH" | "DINNER";

export interface MealPlanEntry {
    id: number,
    day:DayOfWeek,
    mealType: MealType,
    recipe: RecipeSummary
}

export interface MealSlotProps{
    entry: MealPlanEntry|undefined,
    onClick: ()=> void,
    onRemove: ()=> void
}

export interface WeekGridProps{
    entries: MealPlanEntry[];
    onSlotClick: (day: DayOfWeek, mealType: MealType)=> void,
    onRemove: (day: DayOfWeek, mealType: MealType) => void
}

export const DAYS: DayOfWeek[]=[
    "MONDAY", "TUESDAY" , "WEDNESDAY" , "THURSDAY" , "FRIDAY" , "SATURDAY" , "SUNDAY"
];

export const MEAL_TYPES: MealType[] = ["LUNCH", "DINNER"];