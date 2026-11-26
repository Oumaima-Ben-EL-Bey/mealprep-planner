import WeekGrid from "../components/WeekGrid.tsx";
import {useEffect, useState} from "react";
import type {DayOfWeek, MealPlanEntry, MealType, SelectedSlot} from "../types/mealPlan.ts";
import {assignRecipe, clearPlan, getPlan, removeEntry} from "../api/mealPlanApi.ts";
import RecipePicker from "../components/RecipePicker.tsx";

function WeekPlanPage() {
    const [entries, setEntries] = useState<MealPlanEntry[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string|null>(null);
    const [selectedSlot, setSelectedSlot] = useState<SelectedSlot|null>(null);

    async function loadPlan() {
        try {
            const plan = await getPlan();
            setEntries(plan);
            setError(null);
        } catch {
            setError("Der Wochenplan konnte nicht geladen werden.");
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadPlan();
    }, []);

    function handleSlotClick(day:DayOfWeek, mealType: MealType){
        setSelectedSlot({day, mealType});
    }

    async function handleSelect(recipeId: number){
        if(selectedSlot === null) return;
        try{
            await assignRecipe(selectedSlot.day, selectedSlot.mealType, recipeId);
            setSelectedSlot(null);
            await loadPlan();
        }catch{
            setError("Das Rezept konnte nicht eingeplant werden.");
        }
    }

    async function handleRemove(day: DayOfWeek, mealType:MealType){
        try{
            await removeEntry(day,mealType);
            await loadPlan();
        }catch{
            setError("Der Eintrag konnte nicht entfernt werden.");
        }
    }

    async function handleClear(){
        if(!window.confirm("Wochenplan wirklich leeren?")) return;
        try{
            await clearPlan();
            await loadPlan();
        } catch {
            setError("Der Wochenplan konnte nicht geleert werden.");
        }
    }


    return (
        <div>
            <h1>Wochenplan</h1>
            {error !== null && <p>{error}</p>}
            {loading && <p>Wochenplan wird geladen...</p>}
            {!loading&&(
                <div>
                    <WeekGrid entries={entries} onSlotClick={handleSlotClick} onRemove={handleRemove}/>
                    <button type={"button"} onClick={handleClear} disabled={entries.length===0}>
                        Wochenplan leeren
                    </button>
                </div>
            )}

            {selectedSlot !== null && (
                <RecipePicker onSelect={handleSelect} onCancel={()=>setSelectedSlot(null)}/>
            )}
        </div>
    );
}

export default WeekPlanPage;