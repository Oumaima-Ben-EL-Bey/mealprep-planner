export interface ShoppingListItem {
    id: number
    name: string
    displayName: string
    measure: string | null
    checked: boolean
    manual: boolean
    inPantry: boolean
}