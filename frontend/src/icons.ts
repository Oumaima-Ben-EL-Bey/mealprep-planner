// Icons für Navigation und Bedienelemente: als Text importiert (?raw), damit sie die Textfarbe übernehmen
import entdecken from './assets/icons/navbar/entdecken.svg?raw'
import meineRezepte from './assets/icons/navbar/meine-rezepte.svg?raw'
import wochenplan from './assets/icons/navbar/wochenplan.svg?raw'
import einkaufsliste from './assets/icons/navbar/einkaufsliste.svg?raw'
import vorrat from './assets/icons/navbar/vorrat.svg?raw'
import suchen from './assets/icons/extras/suchen.svg?raw'
import ueberraschung from './assets/icons/extras/ueberraschung.svg?raw'
import region from './assets/icons/extras/region.svg?raw'
import zutaten from './assets/icons/extras/zutaten.svg?raw'
import zubereitung from './assets/icons/extras/zubereitung.svg?raw'
import generieren from './assets/icons/einkaufsliste/aus-wochenplan-generieren.svg?raw'
import hinzufuegen from './assets/icons/einkaufsliste/artikel-hinzufuegen.svg?raw'
import einkaufswagen from './assets/icons/einkaufsliste/im-einkaufswagen.svg?raw'
import imVorrat from './assets/icons/einkaufsliste/bereits-im-vorrat.svg?raw'
import erledigteLoeschen from './assets/icons/einkaufsliste/erledigte-loeschen.svg?raw'
import quelleManuell from './assets/icons/einkaufsliste/quelle-manuell.svg?raw'
import quelleWochenplan from './assets/icons/einkaufsliste/quelle-wochenplan.svg?raw'
import leereListe from './assets/icons/einkaufsliste/leer-illustration.svg'

// Kategorie-Icons haben feste Farben und werden als Bild eingebunden
import alle from './assets/icons/kategorien/alle.svg'
import beilage from './assets/icons/kategorien/beilage.svg'
import dessert from './assets/icons/kategorien/dessert.svg'
import fisch from './assets/icons/kategorien/fisch.svg'
import fruehstueck from './assets/icons/kategorien/fruehstueck.svg'
import haehnchen from './assets/icons/kategorien/haehnchen.svg'
import lamm from './assets/icons/kategorien/lamm.svg'
import pasta from './assets/icons/kategorien/pasta.svg'
import rind from './assets/icons/kategorien/rind.svg'
import schwein from './assets/icons/kategorien/schwein.svg'
import sonstiges from './assets/icons/kategorien/sonstiges.svg'
import vegan from './assets/icons/kategorien/vegan.svg'
import vegetarisch from './assets/icons/kategorien/vegetarisch.svg'
import vorspeise from './assets/icons/kategorien/vorspeise.svg'
import ziege from './assets/icons/kategorien/ziege.svg'

export const NAV_ICONS = {entdecken, meineRezepte, wochenplan, einkaufsliste, vorrat}

export const UI_ICONS = {suchen, ueberraschung, region, zutaten, zubereitung}

export const SHOPPING_ICONS = {
    generieren,
    hinzufuegen,
    einkaufswagen,
    imVorrat,
    erledigteLoeschen,
    quelleManuell,
    quelleWochenplan,
}

export const EMPTY_SHOPPING_LIST_IMAGE = leereListe

export const ALL_CATEGORIES_ICON = alle

export const CATEGORY_ICONS: Record<string, string> = {
    Beef: rind,
    Breakfast: fruehstueck,
    Chicken: haehnchen,
    Dessert: dessert,
    Goat: ziege,
    Lamb: lamm,
    Miscellaneous: sonstiges,
    Pasta: pasta,
    Pork: schwein,
    Seafood: fisch,
    Side: beilage,
    Starter: vorspeise,
    Vegan: vegan,
    Vegetarian: vegetarisch,
}
