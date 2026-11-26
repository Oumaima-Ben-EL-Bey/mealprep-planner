import WeekGrid from "../components/WeekGrid.tsx";

function WeekPlanPage() {
    return (
        <div>
            <h1>Wochenplan</h1>
            <WeekGrid
                entries={[]}
                onSlotClick={(day, mealType) => console.log("klick", day, mealType)}
                onRemove={(day, mealType) => console.log("entfernen", day, mealType)}
            />
        </div>
    );
}

export default WeekPlanPage;