import type {PantryItem, PantryItemRequest} from "../types/pantry.ts";
import {useEffect, useState} from "react";
import {createPantryItem, deletePantryItem, getPantryItems, updatePantryItem} from "../api/pantryApi.ts";
import PantryItemForm from "../components/PantryItemForm.tsx";
import PantryItemRow from "../components/PantryItemRow.tsx";

function PantryPage() {
 const [items, setItems]=useState<PantryItem[]>([]);
 const [loading, setLoading] = useState(true);
 const [error, setError] = useState<string|null>(null);

 async function loadItems(){
     try{
         const loaded = await getPantryItems();
         setItems(loaded);
         setError(null);
     }catch {
         setError("Der Vorrat konnte nicht geladen werden.");

     }finally {
         setLoading(false);
     }
 }

 useEffect(()=>{
     loadItems();
 },[])


    async function handleCreate(request: PantryItemRequest): Promise<boolean>{
     try{
         await createPantryItem(request);
         await loadItems();
         return true;
     }catch {
         setError("Der Artikel konnte nicht hinzugefügt werden.");
         return false;
     }
    }

    async function handleUpdate(id:number, request:PantryItemRequest):Promise<boolean>{
     try{
         await updatePantryItem(id,request);
         await loadItems();
         return true;
     }catch{
         setError("Der Artikel konnte nicht gespeichert werden.");
         return false;
     }

    }

async function handleDelete(id:number){
     try{
         await deletePantryItem(id);
         await loadItems();
     } catch{
         setError("Der Artikel konnte nicht gelöscht werden.");
     }
}

    return (
        <div className={"pantry"}>
            <div className={"page-header"}>
                <h1>Vorrat</h1>
            </div>
            <PantryItemForm submitLabel={"Hinzufügen"} clearAfterSubmit onSubmit={handleCreate}/>

            {error !== null && <p className={"alert-error"}>{error}</p>}
            {loading && <p className={"status"}>Vorrat wird geladen...</p>}

            {!loading && items.length === 0&& error === null&&(
                <p className={"empty-state"}>Dein Vorrat ist noch leer.</p>
            )}

            {items.length>0&&(
                <ul className={"item-list"}>
                    {items.map((item)=>(
                        <PantryItemRow key={item.id} item={item} onUpdate={handleUpdate} onDelete={handleDelete}/>))}
                </ul>
            )}
        </div>
    );
}

export default PantryPage;