import type { ShoppingListItem } from '../types/shoppingList'

interface ShoppingListRowProps {
    item: ShoppingListItem
    onToggle: (item: ShoppingListItem) => void
}

function ShoppingListRow({ item, onToggle }: ShoppingListRowProps) {
    return (
        <li className={item.checked ? 'item-row checked' : 'item-row'}>
            <label>
                <input type="checkbox" checked={item.checked} onChange={() => onToggle(item)} />
                <span className="item-name">{item.name}</span>
                {item.measure && <span className="item-measure">{item.measure}</span>}
            </label>
            {item.inPantry && <span className="badge">bereits im Vorrat</span>}
        </li>
    )
}

export default ShoppingListRow