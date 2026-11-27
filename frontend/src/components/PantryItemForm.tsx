import type {PantryItemFormProps} from "../types/pantry.ts";
import {type SyntheticEvent, useState} from "react";

function PantryItemForm({
                            initialName = "",
                            initialMeasure = null,
                            submitLabel,
                            clearAfterSubmit = false,
                            onSubmit,
                            onCancel
                        }: PantryItemFormProps) {
    const [name, setName] = useState(initialName);
    const [measure, setMeasure] = useState(initialMeasure ?? "");
    const [error, setError] = useState<string | null>(null);

    async function handleSubmit(event: SyntheticEvent<HTMLFormElement>) {
        event.preventDefault();

        const trimmedName = name.trim();
        if (trimmedName === "") {
            setError("Bitte einen Namen eingeben.");
            return;
        }
        setError(null);
        const trimmedMeasure = measure.trim();
        const success = await onSubmit({
            name: trimmedName,
            measure: trimmedMeasure === "" ? null : trimmedMeasure
        });
        if (success && clearAfterSubmit) {
            setName("");
            setMeasure("");
        }
    }


    return (
        <div>
            <form className={"add-item-form"} onSubmit={handleSubmit}>
                <input type={"text"} value={name} onChange={(event) => setName(event.target.value)}
                       placeholder={"Name, z. B. r"} aria-label={"Name"}/>
                <input
                    type="text"
                    value={measure}
                    onChange={(event) => setMeasure(event.target.value)}
                    placeholder="Menge, z. B. 1 kg"
                    aria-label="Menge"
                />
                <button type={"submit"} className={"btn"}>{submitLabel}</button>
                {onCancel !== undefined && (
                    <button type={"button"} className={"btn btn-secondary"} onClick={onCancel}>
                        Abbrechen
                    </button>
                )}
            </form>
            {error !== null && <p className={"alert-error"}>{error}</p>}
        </div>
    );
}

export default PantryItemForm;