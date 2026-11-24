import {Route, Routes} from 'react-router-dom'
import DiscoverPage from "./pages/DiscoverPage.tsx";
import CollectionPage from "./pages/CollectionPage.tsx";
import RecipeFormPage from "./pages/RecipeFormPage.tsx";
import RecipeDetailPage from "./pages/RecipeDetailPage.tsx";
import WeekPlanPage from "./pages/WeekPlanPage.tsx";
import ShoppingListPage from "./pages/ShoppingListPage.tsx";
import PantryPage from "./pages/PantryPage.tsx";

function App() {
    return (
        <main>
            <Routes>
                <Route path="/" element={<DiscoverPage/>}/>
                <Route path="/recipes" element={<CollectionPage/>}/>
                <Route path="/recipes/new" element={<RecipeFormPage/>}/>
                <Route path="/recipes/:id/edit" element={<RecipeFormPage/>}/>
                <Route path="/recipes/:id" element={<RecipeDetailPage/>}/>
                <Route path="/recipes/external/:mealDbId" element={<RecipeDetailPage/>}/>
                <Route path="/plan" element={<WeekPlanPage/>}/>
                <Route path="/shopping-list" element={<ShoppingListPage/>}/>
                <Route path="/pantry" element={<PantryPage/>}/>
                <Route path="*" element={<h1>Seite nicht gefunden</h1>}/>
            </Routes>
        </main>
    )
}

export default App