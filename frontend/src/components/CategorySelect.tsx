import {CATEGORY_LABELS} from "../labels.ts";
import {ALL_CATEGORIES_ICON, CATEGORY_ICONS} from "../icons.ts";

interface CategorySelectProps {
    onChange: (category: string) => void,
    value: string
}

const OPTIONS: [string, string][] = [["", "Alle"], ...Object.entries(CATEGORY_LABELS)];

function CategorySelect({onChange, value}: CategorySelectProps) {
    return (
        <div className="chips" role="group" aria-label="Kategorie">
            {OPTIONS.map(([key, label]) => (
                <button
                    key={key || "all"}
                    type="button"
                    className={key === value ? "chip active" : "chip"}
                    aria-pressed={key === value}
                    onClick={() => {
                        if (key !== value) onChange(key);
                    }}>
                    <img src={key ? CATEGORY_ICONS[key] : ALL_CATEGORIES_ICON} alt=""/>
                    {label}
                </button>
            ))}
        </div>
    );
}

export default CategorySelect;
