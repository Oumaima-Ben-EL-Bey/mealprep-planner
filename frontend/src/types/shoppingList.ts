export interface ShoppingListItem {
    id: number
    name: string
    measure: string | null
    checked: boolean
    manual: boolean
    inPantry: boolean
}