import type {PantryItem, PantryItemRequest} from "../types/pantry.ts";
import axios from "axios";

const url = "/api/pantry";

export async function getPantryItems(): Promise<PantryItem[]>{
    const response = await axios.get<PantryItem[]>(url);
    return response.data;
}

export async function createPantryItem(request: PantryItemRequest): Promise<PantryItem>{
    const response = await axios.post<PantryItem>(url, request);
    return response.data;
}

export async function updatePantryItem(id:number, request: PantryItemRequest): Promise<PantryItem>{
    const response = await axios.put<PantryItem>(`${url}/${id}`, request);
    return response.data;
}

export async function deletePantryItem(id:number):Promise<void>{
    await axios.delete(`${url}/${id}`);
}