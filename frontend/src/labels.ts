export const CATEGORY_LABELS: Record<string, string> = {
    Beef: 'Rind',
    Breakfast: 'Frühstück',
    Chicken: 'Hähnchen',
    Dessert: 'Dessert',
    Goat: 'Ziege',
    Lamb: 'Lamm',
    Miscellaneous: 'Sonstiges',
    Pasta: 'Pasta',
    Pork: 'Schwein',
    Seafood: 'Fisch & Meeresfrüchte',
    Side: 'Beilage',
    Starter: 'Vorspeise',
    Vegan: 'Vegan',
    Vegetarian: 'Vegetarisch',
}

export const DAY_LABELS: Record<string, string> = {
    MONDAY: 'Montag',
    TUESDAY: 'Dienstag',
    WEDNESDAY: 'Mittwoch',
    THURSDAY: 'Donnerstag',
    FRIDAY: 'Freitag',
    SATURDAY: 'Samstag',
    SUNDAY: 'Sonntag',
}

export const MEAL_TYPE_LABELS: Record<string, string> = {
    LUNCH: 'Mittagessen',
    DINNER: 'Abendessen',
}

export function categoryLabel(category: string | null): string {
    if (!category) return ''
    return CATEGORY_LABELS[category] ?? category
}

export const COUNTRY_LABELS: Record<string, string> = {
    'United States': 'USA',
    'United Kingdom': 'Großbritannien',
    Algeria: 'Algerien',
    Argentina: 'Argentinien',
    Australia: 'Australien',
    Canada: 'Kanada',
    China: 'China',
    Croatia: 'Kroatien',
    Egypt: 'Ägypten',
    France: 'Frankreich',
    Greece: 'Griechenland',
    India: 'Indien',
    Ireland: 'Irland',
    Italy: 'Italien',
    Jamaica: 'Jamaika',
    Japan: 'Japan',
    Kenya: 'Kenia',
    Malaysia: 'Malaysia',
    Mexico: 'Mexiko',
    Morocco: 'Marokko',
    Netherlands: 'Niederlande',
    Norway: 'Norwegen',
    Philippines: 'Philippinen',
    Poland: 'Polen',
    Portugal: 'Portugal',
    Russia: 'Russland',
    'Saudi Arabia': 'Saudi-Arabien',
    Slovakia: 'Slowakei',
    Spain: 'Spanien',
    Syria: 'Syrien',
    Thailand: 'Thailand',
    Tunisia: 'Tunesien',
    Turkey: 'Türkei',
    Ukraine: 'Ukraine',
    Uruguay: 'Uruguay',
    Venezuela: 'Venezuela',
    Vietnam: 'Vietnam',
}

export function regionLabel(region: string | null): string {
    if (!region) return ''
    return COUNTRY_LABELS[region] ?? region
}