import { useState } from 'react'
import type { ShoppingListItem } from '../types/shoppingList'
import Icon from './Icon'
import { SHOPPING_ICONS } from '../icons'

interface ShoppingListRowProps {
    item: ShoppingListItem
    onToggle: (item: ShoppingListItem) => void
}

function ShoppingListRow({ item, onToggle }: ShoppingListRowProps) {
    const [imageFound, setImageFound] = useState(true)
    const source = item.manual ? 'Manuell hinzugefügt' : 'Aus dem Wochenplan'

    return (
        <li className={item.checked ? 'item-row checked' : 'item-row'}>
            <label>
                <input type="checkbox" checked={item.checked} onChange={() => onToggle(item)} />
                {imageFound ? (
                    <img
                        className="item-thumb"
                        src={`https://www.themealdb.com/images/ingredients/${encodeURIComponent(item.name)}-small.png`}
                        alt=""
                        onError={() => setImageFound(false)}
                    />
                ) : (
                    <span className="item-thumb" />
                )}
                <span className="item-name">{item.name}</span>
                {item.measure && <span className="item-measure">{item.measure}</span>}
            </label>
            {item.inPantry && (
                <span className="badge">
                    <Icon svg={SHOPPING_ICONS.imVorrat} size={16} />
                    bereits im Vorrat
                </span>
            )}
            <span className="source-icon has-tooltip" role="img" aria-label={source} data-tooltip={source}>
                <Icon svg={item.manual ? SHOPPING_ICONS.quelleManuell : SHOPPING_ICONS.quelleWochenplan} size={18} />
            </span>
        </li>
    )
}

export default ShoppingListRow
