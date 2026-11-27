export interface PantryItem{
    id:number,
    name: string;
    measure: string|null
}

export interface PantryItemRequest{
    name: string,
    measure: string|null
}