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
        <table>
            <thead>
            <tr>
                <th></th>
                {MEAL_TYPES.map((mealType) => (
                    <th key={mealType}>{MEAL_TYPE_LABELS[mealType]}</th>
                ))}
            </tr>
            </thead>
            <tbody>
            {DAYS.map((day)=>(
                <tr key={day}>
                    <th>{DAY_LABELS[day]}</th>
                    {MEAL_TYPES.map((mealType)=>(
                        <td key={mealType}>
                            <MealSlot
                                entry={findEntry(day,mealType)}
                                onClick={()=>onSlotClick(day,mealType)}
                                onRemove={()=>onRemove(day,mealType)}
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