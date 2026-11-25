import axios from 'axios'
import type { ShoppingListItem } from '../types/shoppingList'

const BASE = '/api/shopping-list'

export async function getShoppingList(): Promise<ShoppingListItem[]> {
    const response = await axios.get<ShoppingListItem[]>(BASE)
    return response.data
}

export async function generateShoppingList(): Promise<ShoppingListItem[]> {
    const response = await axios.post<ShoppingListItem[]>(`${BASE}/generate`)
    return response.data
}

export async function addShoppingListItem(name: string, measure: string): Promise<ShoppingListItem> {
    const response = await axios.post<ShoppingListItem>(`${BASE}/items`, { name, measure })
    return response.data
}

export async function setItemChecked(id: number, checked: boolean): Promise<ShoppingListItem> {
    const response = await axios.patch<ShoppingListItem>(`${BASE}/items/${id}`, { checked })
    return response.data
}

export async function deleteCheckedItems(): Promise<void> {
    await axios.delete(`${BASE}/checked-items`)
}