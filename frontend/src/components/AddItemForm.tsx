import { useState } from 'react'
import Icon from './Icon'
import { SHOPPING_ICONS } from '../icons'

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
            <button type="submit" className="btn btn-secondary" disabled={name.trim() === ''}><Icon svg={SHOPPING_ICONS.hinzufuegen} size={18} />Hinzufügen</button>
        </form>
    )
}

export default AddItemForm