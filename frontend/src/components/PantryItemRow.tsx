import type {PantryItemRequest, PantryItemRowProps} from "../types/pantry.ts";
import {useState} from "react";
import PantryItemForm from "./PantryItemForm.tsx";

function PantryItemRow({item,onUpdate,onDelete}:PantryItemRowProps) {
   const [editing,setEditing]=useState(false);

   async function handleUpdate(request:PantryItemRequest):Promise<boolean>{
       const success = await onUpdate(item.id,request);
       if(success){
           setEditing(false);
       }
       return success;

   }
       if(editing){
           return(
               <li className={"item-row"}>
                   <PantryItemForm initialName={item.name}
                                   initialMeasure={item.measure}
                                   submitLabel={"Speichern"}
                                   onSubmit={handleUpdate}
                                   onCancel={()=>setEditing(false)}
                                   />
               </li>
           );
       }
    return (
        <li className={"item-row"}>
            <div>
                <span className={"item-name"}>{item.name}</span>
                {item.measure!==null&&<span className={"item-measure"}> · {item.measure}</span> }
            </div>
            <div>
                <button type={"button"} className={"btn btn-secondary"} onClick={()=>setEditing(true)}>
                    Bearbeiten
                </button>
                <button type={"button"} className={"btn btn-warm"} onClick={()=>onDelete(item.id)}>
                    Löschen
                </button>
            </div>
        </li>
    );
}

export default PantryItemRow;