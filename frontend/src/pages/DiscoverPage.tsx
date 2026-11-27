import {useEffect, useState} from "react";
import axios from "axios";
import type {RandomMode, RecipeSummary} from "../types/recipe.ts";
import {getRandomRecipe, searchRecipes} from "../api/externalRecipeApi.ts";
import RecipeCardList from "../components/RecipeCardList.tsx";
import SearchBar from "../components/SearchBar.tsx";
import CategorySelect from "../components/CategorySelect.tsx";
import {useNavigate} from "react-router-dom";
import Icon from "../components/Icon.tsx";
import {UI_ICONS} from "../icons.ts";

const RANDOM_MODES: { mode: RandomMode, label: string, hint: string }[] = [
    {mode: "ADVENTURE", label: "Abenteuer", hint: "Irgendein Gericht aus aller Welt. Es kann auch mal Frühstück zum Abendessen geben."},
    {mode: "CATEGORY", label: "Passend", hint: "Ein Zufallsgericht aus der Kategorie, die unten ausgewählt ist."},
    {mode: "PANTRY", label: "Aus dem Vorrat", hint: "Ein Gericht mit etwas, das du schon zu Hause hast. So landet weniger im Müll."},
];

function DiscoverPage() {
    const [recipes, setRecipes] = useState<RecipeSummary[]>([]);
    const [term, setTerm] = useState<string>("");
    const [category, setCategory] = useState<string>("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [randomMode, setRandomMode] = useState<RandomMode>("ADVENTURE");
    const [randomError, setRandomError] = useState<string | null>(null);
    const [rolling, setRolling] = useState(false);
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
        setRandomError(null);
        setRolling(true);
        getRandomRecipe(randomMode, category)
            .then((recipe) => navigate(`/recipes/external/${recipe.externalId}`))
            .catch((e) => {
                const message = axios.isAxiosError(e) ? e.response?.data?.message : undefined;
                setRandomError(message ?? 'Es konnte kein Zufallsrezept geladen werden. Bitte versuche es später noch einmal.');
            })
            .finally(() => setRolling(false));
    }


    return (
        <div>
            <h1>Rezepte entdecken</h1>
            <div className="toolbar">
                <SearchBar onSearch={handleSearch}/>
                <button type="button" className="btn btn-warm" onClick={handleRandom} disabled={rolling}><Icon svg={UI_ICONS.ueberraschung} size={18}/>Überraschungsgericht</button>
            </div>
            <div className="chips" role="group" aria-label="Zufallsmodus">
                {RANDOM_MODES.map((option) => (
                    <button key={option.mode} type="button"
                            className={option.mode === randomMode ? "chip active" : "chip"}
                            aria-pressed={option.mode === randomMode}
                            onClick={() => {
                                setRandomMode(option.mode);
                                setRandomError(null);
                            }}>
                        {option.label}
                    </button>
                ))}
            </div>
            <p className="hint">{RANDOM_MODES.find((option) => option.mode === randomMode)?.hint}</p>
            {randomError && <p className="alert-error">{randomError}</p>}
            <CategorySelect value={category} onChange={handleCategory}/>
            {loading ? <p className="status">Rezepte werden geladen …</p> : error ? <p className="alert-error">{error}</p> :
                <RecipeCardList recipes={recipes}/>}

        </div>
    );
}

export default DiscoverPage;