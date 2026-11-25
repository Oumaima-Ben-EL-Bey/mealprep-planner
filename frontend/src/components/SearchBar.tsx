import {useState} from "react";
import Icon from "./Icon.tsx";
import {UI_ICONS} from "../icons.ts";

interface SearchBarProps {
    onSearch: (term: string) => void
}

function SearchBar({onSearch}: SearchBarProps) {
    const [input, setInput] = useState("");
    return (
        <form className="search-bar" onSubmit={(event) => {
            event.preventDefault();
            onSearch(input);
        }}>
            <input id={"search"}
                   type={"text"}
                   value={input}
                   onChange={(event) => setInput(event.target.value)}
                   placeholder="Rezept suchen, z. B. Pasta"/>
            <button type={"submit"} className="btn"><Icon svg={UI_ICONS.suchen} size={18}/>Suchen</button>
        </form>
    );
}

export default SearchBar;