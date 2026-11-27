export interface PantryItem{
    id:number,
    name: string;
    measure: string|null
}

export interface PantryItemRequest{
    name: string,
    measure: string|null
}

export interface PantryItemFormProps{
    initialName?: string,
    initialMeasure?: string|null,
    submitLabel: string,
    clearAfterSubmit?: boolean,
    onSubmit:(request:PantryItemRequest)=>Promise<boolean>,
    onCancel?:()=> void
}