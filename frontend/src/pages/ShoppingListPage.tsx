import { useEffect, useState } from 'react'
import type { ShoppingListItem } from '../types/shoppingList'
import {
    addShoppingListItem,
    deleteCheckedItems,
    generateShoppingList,
    getShoppingList,
    setItemChecked,
} from '../api/shoppingListApi'
import ShoppingListRow from '../components/ShoppingListRow'
import AddItemForm from '../components/AddItemForm'

const SAVE_ERROR = 'Die Änderung konnte nicht gespeichert werden. Bitte versuche es noch einmal.'

function ShoppingListPage() {
    const [items, setItems] = useState<ShoppingListItem[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        getShoppingList()
            .then(setItems)
            .catch(() => setError('Die Einkaufsliste konnte nicht geladen werden. Bitte versuche es später noch einmal.'))
            .finally(() => setLoading(false))
    }, [])

    function handleGenerate() {
        setError(null)
        generateShoppingList().then(setItems).catch(() => setError(SAVE_ERROR))
    }

    function handleAdd(name: string, measure: string) {
        setError(null)
        addShoppingListItem(name, measure)
            .then((item) => setItems((current) => [...current, item]))
            .catch(() => setError(SAVE_ERROR))
    }

    function handleToggle(item: ShoppingListItem) {
        setError(null)
        setItemChecked(item.id, !item.checked)
            .then((updated) => setItems((current) => current.map((i) => (i.id === updated.id ? updated : i))))
            .catch(() => setError(SAVE_ERROR))
    }

    function handleDeleteChecked() {
        setError(null)
        deleteCheckedItems()
            .then(() => setItems((current) => current.filter((i) => !i.checked)))
            .catch(() => setError(SAVE_ERROR))
    }

    const openItems = items.filter((i) => !i.checked)
    const checkedItems = items.filter((i) => i.checked)

    return (
        <div className="shopping-list">
            <h1>Einkaufsliste</h1>
            <button type="button" className="btn" onClick={handleGenerate}>
                Einkaufsliste aus Wochenplan generieren
            </button>
            <AddItemForm onAdd={handleAdd} />
            {error && <p className="alert-error">{error}</p>}

            {loading ? (
                <p className="status">Einkaufsliste wird geladen …</p>
            ) : items.length === 0 ? (
                <p className="status">Die Einkaufsliste ist leer. Generiere sie aus dem Wochenplan oder füge Artikel hinzu.</p>
            ) : (
                <>
                    <div className="section-header">
                        <h2>Offen ({openItems.length})</h2>
                    </div>
                    <ul className="item-list">
                        {openItems.map((item) => (
                            <ShoppingListRow key={item.id} item={item} onToggle={handleToggle} />
                        ))}
                    </ul>

                    {checkedItems.length > 0 && (
                        <>
                            <div className="section-header">
                                <h2>Im Einkaufswagen ({checkedItems.length})</h2>
                                <button type="button" className="btn btn-secondary" onClick={handleDeleteChecked}>
                                    Erledigte Artikel löschen
                                </button>
                            </div>
                            <ul className="item-list">
                                {checkedItems.map((item) => (
                                    <ShoppingListRow key={item.id} item={item} onToggle={handleToggle} />
                                ))}
                            </ul>
                        </>
                    )}
                </>
            )}
        </div>
    )
}

export default ShoppingListPage