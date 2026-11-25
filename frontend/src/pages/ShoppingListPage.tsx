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
import Icon from '../components/Icon'
import { EMPTY_SHOPPING_LIST_IMAGE, SHOPPING_ICONS } from '../icons'

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
            <div className="page-header">
                <h1>Einkaufsliste</h1>
                <button type="button" className="btn btn-expand" onClick={handleGenerate}>
                    <Icon svg={SHOPPING_ICONS.generieren} size={22} />
                    <span className="btn-expand-label">Einkaufsliste aus Wochenplan generieren</span>
                </button>
            </div>
            <AddItemForm onAdd={handleAdd} />
            {error && <p className="alert-error">{error}</p>}

            {loading ? (
                <p className="status">Einkaufsliste wird geladen …</p>
            ) : items.length === 0 ? (
                <div className="empty-state">
                    <img src={EMPTY_SHOPPING_LIST_IMAGE} alt="" />
                    <p>Die Einkaufsliste ist leer. Generiere sie aus dem Wochenplan oder füge Artikel hinzu.</p>
                </div>
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
                                <h2 className="section-title">
                                    <Icon svg={SHOPPING_ICONS.einkaufswagen} size={22} />
                                    Im Einkaufswagen ({checkedItems.length})
                                </h2>
                                <button type="button" className="btn btn-secondary" onClick={handleDeleteChecked}>
                                    <Icon svg={SHOPPING_ICONS.erledigteLoeschen} size={18} />
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