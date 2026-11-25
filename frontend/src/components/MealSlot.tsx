import type {MealSlotProps} from "../types/mealPlan.ts";


function MealSlot({ entry, onClick, onRemove }: MealSlotProps) {

    if(entry === undefined){
        return(
            <button type="button" onClick={onClick}>
                + Rezept wählen
            </button>
        )
    }

    return (
        <div>
            <button type={"button"} onClick={onClick}>
                {entry.recipe.title}
            </button>
            <button type={"button"} onClick={onRemove} aria-label={"Eintrag entfernen"}>
                ✕
            </button>
        </div>
    );
}

export default MealSlot;