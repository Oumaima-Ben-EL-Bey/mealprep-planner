import {NavLink} from "react-router-dom";
import Icon from "./Icon.tsx";
import {NAV_ICONS} from "../icons.ts";

function NavBar() {
    return (

            <nav className="navbar">
                <NavLink to={"/"}><Icon svg={NAV_ICONS.entdecken}/>Entdecken</NavLink>
                <NavLink to={"/recipes"}><Icon svg={NAV_ICONS.meineRezepte}/>Meine Rezepte</NavLink>
                <NavLink to={"/plan"}><Icon svg={NAV_ICONS.wochenplan}/>Wochenplan</NavLink>
                <NavLink to={"/shopping-list"}><Icon svg={NAV_ICONS.einkaufsliste}/>Einkaufsliste</NavLink>
                <NavLink to={"/pantry"}><Icon svg={NAV_ICONS.vorrat}/>Vorrat</NavLink>
            </nav>

    );
}

export default NavBar;