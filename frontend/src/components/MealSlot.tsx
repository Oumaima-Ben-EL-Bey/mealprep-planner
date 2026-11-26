import type {MealSlotProps} from "../types/mealPlan.ts";


function MealSlot({ entry, onClick, onRemove }: MealSlotProps) {

    if (entry === undefined) {
        return (
            <button type="button" className="slot-empty" onClick={onClick}>
                + Rezept wählen
            </button>
        )
    }

    return (
        <div className="slot-filled">
            <button type="button" className="slot-title" onClick={onClick}>
                {entry.recipe.title}
            </button>
            <button type="button" className="slot-remove" onClick={onRemove} aria-label="Eintrag entfernen">
                ✕
            </button>
        </div>
    );
}

export default MealSlot;