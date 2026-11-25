import {useEffect, useState} from "react";
import type {RecipeSummary} from "../types/recipe.ts";
import {getRandomRecipe, searchRecipes} from "../api/externalRecipeApi.ts";
import RecipeCardList from "../components/RecipeCardList.tsx";
import SearchBar from "../components/SearchBar.tsx";
import CategorySelect from "../components/CategorySelect.tsx";
import {useNavigate} from "react-router-dom";
import Icon from "../components/Icon.tsx";
import {UI_ICONS} from "../icons.ts";

function DiscoverPage() {
    const [recipes, setRecipes] = useState<RecipeSummary[]>([]);
    const [term, setTerm] = useState<string>("");
    const [category, setCategory] = useState<string>("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate()


    useEffect(() => {
        searchRecipes(term, category).then(data => setRecipes(data))
            .catch(() => setError("Rezepte konnten nicht geladen werden. Bitte versuche es später noch einmal."))
            .finally(() => setLoading(false))
    }, [category, term]);

    function handleSearch(sterm: string) {
        if(sterm === term){
            return;
        }
        setLoading(true);
        setError(null);
        setTerm(sterm);
    }

    function handleCategory(category: string) {
        setLoading(true);
        setError(null);
        setCategory(category);
    }
    function handleRandom() {
        getRandomRecipe()
            .then((recipe) => navigate(`/recipes/external/${recipe.externalId}`))
            .catch(() => setError('Es konnte kein Zufallsrezept geladen werden. Bitte versuche es später noch einmal.'))
    }


    return (
        <div>
            <h1>Rezepte entdecken</h1>
            <div className="toolbar">
                <SearchBar onSearch={handleSearch}/>
                <button type="button" className="btn btn-warm" onClick={handleRandom}><Icon svg={UI_ICONS.ueberraschung} size={18}/>Überraschungsgericht</button>
            </div>
            <p className="hint">Abenteuer-Modus: Es kann auch mal Frühstück zum Abendessen geben.</p>
            <CategorySelect value={category} onChange={handleCategory}/>
            {loading ? <p className="status">Rezepte werden geladen …</p> : error ? <p className="alert-error">{error}</p> :
                <RecipeCardList recipes={recipes}/>}

        </div>
    );
}

export default DiscoverPage;