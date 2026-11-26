import {
    type DayOfWeek,
    DAYS,
    MEAL_TYPES,
    type MealPlanEntry,
    type MealType,
    type WeekGridProps
} from "../types/mealPlan.ts";
import {DAY_LABELS, MEAL_TYPE_LABELS} from "../labels.ts";
import MealSlot from "./MealSlot.tsx";

function WeekGrid({entries, onSlotClick, onRemove}: WeekGridProps) {
    function findEntry(day:DayOfWeek, mealType: MealType): MealPlanEntry|undefined{
        return entries.find((entry)=> entry.day === day && entry.mealType ===mealType);
    }


    return (
        <table className="week-grid">
            <thead>
            <tr>
                <th></th>
                {DAYS.map((day) => (
                    <th key={day}>{DAY_LABELS[day]}</th>
                ))}
            </tr>
            </thead>
            <tbody>
            {MEAL_TYPES.map((mealType) => (
                <tr key={mealType}>
                    <th>{MEAL_TYPE_LABELS[mealType]}</th>
                    {DAYS.map((day) => (
                        <td key={day}>
                            <MealSlot
                                entry={findEntry(day, mealType)}
                                onClick={() => onSlotClick(day, mealType)}
                                onRemove={() => onRemove(day, mealType)}
                            />
                        </td>
                    ))}
                </tr>
            ))}
            </tbody>
        </table>
    );
}

export default WeekGrid;