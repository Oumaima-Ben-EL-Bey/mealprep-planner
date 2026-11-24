import {NavLink} from "react-router-dom";

function NavBar() {
    return (

            <nav>
                <NavLink to={"/"}>Entdecken</NavLink>
                <NavLink to={"/recipes"}>Meine Rezepte</NavLink>
                <NavLink to={"/plan"}>Wochenplan</NavLink>
                <NavLink to={"/shopping-list"}>Einkaufsliste</NavLink>
                <NavLink to={"/pantry"}>Vorrat</NavLink>
            </nav>

    );
}

export default NavBar;