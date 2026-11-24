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