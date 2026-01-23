# FoodPal (CSEP team 62)

FoodPal is a distributed cooking organizer: a Spring Boot server hosts shared recipes, while a JavaFX client lets users create, browse, edit, and prepare recipes locally and collaboratively. The server stays client-agnostic, while the client persists user-specific settings (e.g., favorites, language) in a local JSON config file.

---

## Key features

✅ **Backlog coverage**: all user stories (non-functional requirements) in the provided backlog are covered.

---

## Recipe management (basic requirements)

* Create, edit, delete, and browse recipes stored on the server.
* Manage recipe name, ingredients, and preparation steps (add/change/delete).
* View full recipe details.
* Clone recipes and rename them (your own twist without changing the original).
* Download a printable version of a recipe.

---

## Extra usability additions:

* **Recipe overview validation and warnings**: The recipe overview includes comprehensive client-side validation and warnings to ensure data is entered in the correct format. This covers validation of the recipe name, ingredient addition (including amounts and units), and preparation step input, helping users avoid invalid or incomplete submissions while editing recipes.
* **Keybinds for UI buttons**: “Cancel/Back” and “Next/Done” buttons are bound to Esc and Enter respectively. This allows users to use this application without the (oftentimes) tedious use of the mouse.

---

## Automated change synchronization

* Changes are automatically propagated across all clients, so there is no need for a manual refresh.
* Changes to a recipe title are propagated immediately, without requiring a manual refresh.
* Addition and deletion of recipes is propagated automatically, without requiring a manual refresh.
* Changes in recipe content are propagated when viewing the same recipe, so manual refreshes are avoided.
* The client uses WebSockets to subscribe to changes.
* The client does not poll for updates; all changes are pushed from the server.

---

## Extra usability additions:

### Automated Change Synchronization for ingredients:

* Changes to ingredient fields (name, density, nutrition values) are propagated immediately, without requiring a manual refresh.
* Addition and deletion of ingredients are propagated automatically, without requiring a manual refresh.

---

## Nutritional value + scaling

* Ingredient types are defined separately from recipes, so nutritional info isn’t duplicated.
* Set protein, fat, and carbs per 100g for ingredient types, and show estimated kcal/100g plus “used in x recipes” for each one.
* See an estimation of the kcal/100g for a recipe.
* Recipes support formal amounts + units (g, kg, ml, l, tbsp, tsp, pinch, etc.).
* Scale recipes by an arbitrary factor (client-only), with unit normalization (e.g., 1000g → 1kg).

---

## Extra usability additions:

* **Nutri-score**: We have implemented our own nutri-score which is calculated per recipe, and ranges from A-E (like the Dutch system). It utilizes the data we have for protein, fat, carbs and kcal per 100g. The nutri-score is displayed on the recipe overview for any recipe that contains ingredients with nutritional information and is also colour coded.
* **Easy ingredient selection when adding recipes**: When adding a new ingredient to a recipe, you can choose the ingredient type through a drop down menu which is sorted alphabetically and can also be searched through by typing into the text field. This makes it easier to find the ingredient type you want when there is a large quantity of them.
* **Nutrition tooltip**: hovering the recipe’s kcal/100g label shows a translated summary of the recipe’s nutrition (the base totals (kcal + grams), the scaled totals for the current scale factor, and how many informal/ignored ingredients were excluded), so users can sanity-check nutrition without cluttering the main UI.
* **Print**: Clicking “Print” opens the system’s default print dialog and allows printing the selected recipe in the same format as the downloaded PDF.
* **Ingredient type search**: The ingredient type overview supports search and ordering, so users can quickly find specific ingredient types and keep the list manageable as it grows.
* **Scaling includes informal units**: Scaling applies not only to formal units (g/ml/etc.) but also to informal ones like pinches and handfuls, keeping the ingredient list of a recipe readable and consistent at any scale factor.
* **Ingredient type consistency**: When saving recipes, FoodPal avoids duplicated ingredient types by deduplicating by type name and ensuring newly introduced types are persisted consistently.
* **Client-side user validation and warnings**: The client performs extensive input validation and provides immediate warnings when invalid data is entered. Users are notified if nutritional values such as protein, fat, or carbohydrates are not valid numeric values, if the density is not a valid number below 22.6, or if ingredient names are invalid (for example, empty or starting with a numeric character).

---

## Searching for recipes + favorites

* Star/unstar favorites; favorites are stored locally and stay linked to server recipes (not clones), even if renamed.
* Full-text search (name/ingredients/steps) with simple AND query syntax.
* Search filters the current view (no separate window) and can be canceled with Escape.

---

## Extra usability additions:

* **Live deletion detection**: The mourning of a favourite recipe appears live, so there is no need to restart the app to see it.
* **Automatic favouriting**: When the user adds a new recipe while filtering by favourite recipes, the recipe is automatically added to the user’s favourites.
* **Order recipes for faster discovery**: In the recipe overview, users can sort the current list by Name (A–Z), Least kcal/100g first, Best Nutri-Score first, Fewest steps first, or Fewest ingredients first. This makes it easy to quickly find, for example, simpler recipes (few steps/ingredients) or healthier ones (nutrition-based) without opening each recipe.
* **Search results indicator**: After searching, FoodPal shows a small tag like “x recipes match your search” (or “No recipes match your search”) so users instantly know whether the filter is working or they just typed something too specific.

---

## Shopping list

* Shopping list is not stored on the server; users can add/remove items and keep an ordered list.
* Add a recipe’s ingredients through an editable “to-be-added” overview (adjust amounts, add/remove items), then confirm, ingredients will appear in the shopping list with the name of the source recipe.
* If multiple recipes add the same ingredient, it can appear multiple times with the source recipe shown.
* Easily download the shopping list as a PDF or reset it. The downloaded document is sorted in the same way as the list at the moment of downloading. It is also in the same language.
* When adding an ingredient that already exists on the server (i.e. is visible in the Ingredient overview), it retains the category it has at the time of adding.
* Ingredients created directly in the shopping list or “to-be-added” overview are not saved on the server and therefore do not appear in the Ingredient overview. These ingredients have a null category.
* The shopping list is independent of database changes. Updates made to ingredients or recipes in the Ingredient or Recipe overview do not propagate to existing shopping list items. This ensures that other users cannot modify your shopping list indirectly. For example, if another user edits or deletes an ingredient in the database, your existing shopping list remains unchanged. Because shopping list items are stored locally, they are isolated from server-side updates and remain exactly as they were at the time they were added.

---

## Extra usability additions:

* **Sorting (with ordered category headers)**: Shopping list items can be sorted by category, recipe name, or item name. When sorting by category, FoodPal also orders the category headers alphabetically (based on the translated category names and category other is always at the end).
* **Print**: Clicking “Print” opens the system’s default print dialog and allows printing the shopping list in the same format as the downloaded PDF.

These features significantly improve usability. Since items in stores are usually organized by category, sorting the shopping list accordingly helps users navigate stores more efficiently. Additionally, the ability to print a sorted shopping list enables easy offline use during shopping, contributing to an excellent user experience.

---

## Live translation
To make localization feel real, we added several small but impactful details:
* Users can switch the app language at any time using the language picker, and all screens update immediately (the language choice persists between restarts).
* This changes the language of buttons, labels, warnings, tooltips, and other interface text, so the interface feels native in English, Dutch, or Portuguese.
* Each recipe also has its own language tag, which you can change and use as a filter when browsing recipes.

---

## Extra usability additions:

* **First launch uses OS language**: when opening the app for the first time, FoodPal automatically starts in the language of your computer (if supported), so users aren’t forced into an English-centric default before they’ve even clicked anything.
* **“Download as PDF” is translated too**: the exported recipe uses the selected UI language.
* **Locale-friendly input**: users can type numbers in the format they’re used to (for example, 1.5 or 1,5, and even fractions like ½ where relevant), therefore reducing the validation frustration and preventing input mistakes.
* **Locale-friendly number display**: numbers are also displayed in the user’s locale conventions (decimal separators and grouping, for example, 1000 vs 1,000 and 1.5 vs 1,5).
* **Diacritics-insensitive search**: search ignores accents/diacritics, so, for example, typing ‘acai’ will still match ‘açaí’. That means users don’t need the “right” keyboard layout or special characters to find what they want, making it faster and less frustrating for international users.

---

## How to run

1. Run the server main class:

```code
mvn -pl server -am spring-boot:run

```

2. Run the client main class:

```code
mvn -pl client -am javafx:run

```

To run with command line argument specifying user config path:

```code
mvn -pl client -am javafx:run "-Djavafx.args=-cfg path/to/userconfig"

```

(Relative path only, Path is relative to the client package, Path must end in / or \ and must not start in / or , File name should not be included, directory must already exist)

---

