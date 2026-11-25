import {useState} from "react";

interface SearchBarProps {
    onSearch: (term: string) => void
}

function SearchBar({onSearch}: SearchBarProps) {
    const [input, setInput] = useState("");
    return (
        <form onSubmit={(event) => {
            event.preventDefault();
            onSearch(input);
        }}>
            <input id={"search"}
                   type={"text"}
                   value={input}
                   onChange={(event) => setInput(event.target.value)}
                   placeholder="Rezept suchen, z. B. Pasta"/>
            <button type={"submit"}>Suchen</button>
        </form>
    );
}

export default SearchBar;