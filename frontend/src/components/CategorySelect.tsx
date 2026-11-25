import {CATEGORY_LABELS} from "../labels.ts";

interface CategorySelectProps {
    onChange: (category: string) => void,
    value: string
}

function CategorySelect({onChange, value}: CategorySelectProps) {
    return (
        <div>
            <select value={value}
                    onChange={(event) => onChange(event.target.value)}>
                <option value={""}>Alle Kategorien</option>
                {Object.entries(CATEGORY_LABELS)
                    .map((k) => <option key={k[0]} value={k[0]}>{k[1]}</option>)}
            </select>
        </div>
    );
}

export default CategorySelect;