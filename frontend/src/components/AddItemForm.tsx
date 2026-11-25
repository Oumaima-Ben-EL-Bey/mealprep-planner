import { useState } from 'react'

interface AddItemFormProps {
    onAdd: (name: string, measure: string) => void
}

function AddItemForm({ onAdd }: AddItemFormProps) {
    const [name, setName] = useState('')
    const [measure, setMeasure] = useState('')

    return (
        <form
            className="add-item-form"
            onSubmit={(event) => {
                event.preventDefault()
                onAdd(name.trim(), measure.trim())
                setName('')
                setMeasure('')
            }}>
            <input value={name} onChange={(e) => setName(e.target.value)} placeholder="Artikel, z. B. Kaffee" aria-label="Artikel" />
            <input value={measure} onChange={(e) => setMeasure(e.target.value)} placeholder="Menge (optional)" aria-label="Menge" />
            <button type="submit" className="btn btn-secondary" disabled={name.trim() === ''}>Hinzufügen</button>
        </form>
    )
}

export default AddItemForm