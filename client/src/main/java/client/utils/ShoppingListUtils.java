package client.utils;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import commons.*;
import jakarta.inject.Inject;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import java.awt.*;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import static commons.Category.*;

public class ShoppingListUtils {

    private final IngredientScaling ingredientScaling;
    private final LanguageService languages;
    private final UserConfig user;

    private static final AmountParser amountParser = new AmountParser();

    @Inject
    public ShoppingListUtils(IngredientScaling ingredientScaling,
                             LanguageService languages,
                             UserConfig user) {
        this.ingredientScaling = ingredientScaling;
        this.languages = languages;
        this.user = user;
    }

    /**
     * Creates a deep copy of ingredients in recipe and saves them to provided list
     * @param recipe The recipe
     * @param list The list
     */
    public void addIngredientsToListView(Recipe recipe, List<ShoppingListItem> list, double scale) {
        list.addAll(
                recipe.ingredients.stream()
                        .map(Ingredient::copy)
                        .map(i -> {
                            if(i.amount != null){
                                i.amount = scale * i.amount;
                                return new ShoppingListItem(i);
                            }
                            return new ShoppingListItem(i);
                        })
                        .toList()
        );
    }

    /**
     * Gets shopping list items form a list of shopping list entries
     * @param shoppingList The list of shopping list entries
     * @return Shopping list items array list
     */
    public ArrayList<ShoppingListItem> getShoppingListItems(List<ShoppingListEntry> shoppingList) {
        return new ArrayList<>(shoppingList.
                stream().
                filter(x -> x.getClass().equals(ShoppingListItem.class)).
                map(x -> (ShoppingListItem)x).
                toList());
    }

    /**
     * Checks if the name, amount and unit are valid for ingredient.
     * If not the methods prints warning to the console and returns false
     * @param name The name
     * @param amount The amount
     * @param unit The unit
     * @return true if valid and false if invalid
     */
    public boolean ingredientValidation(String name, String amount, String unit){
        if(name.isEmpty()){
            System.out.println("A name is required.");
            return false;
        }

        if ("__SELECT_UNIT__".equals(unit)) {
            System.out.println("Unit is required.");
            return false;
        }

        if (!unit.equals("TO_TASTE")
                && amount.isEmpty()) {
            System.out.println("This unit needs an amount.");
            return false;
        }

        if (unit.equals("TO_TASTE")
                && !amount.isEmpty()) {
            System.out.println("This unit cannot have an amount.");
            return false;
        }

        if(!unit.equals("TO_TASTE")){
            try{
                amountParser.parseAmount(amount);
            }catch(Exception e){
                System.out.println("Invalid amount");
                return false;
            }
        }

        return true;
    }

    /**
     * Sets name, amount and unit for provided ingredient
     * @param ingredient The ingredient
     * @param name The name as String
     * @param amount The amount as String
     * @param unit The unit as String
     */
    public void applyEditsToIngredient(Ingredient ingredient, String name, String amount, String unit){
        ingredient.ingredientType.name = name;
        if (!amount.isEmpty()) {
            ingredient.amount = amountParser.parseAmount(amount);
        } else {
            ingredient.amount = null;
        }
        if (!unit.isEmpty()) {
            ingredient.unit = Unit.valueOf(unit);
        } else {
            ingredient.unit = null;
        }
    }

    /**
     * Saves the list of ingredients to the user. Adds a recipes name to each ingredient.
     * @param shoppingListItems The list of ingredients
     * @param recipe The recipe from which ingredients comes from
     */
    public void confirmAddingIngredients(List<ShoppingListItem> shoppingListItems, Recipe recipe){
        shoppingListItems.forEach(ingredient -> {
            ingredient.setRecipeName(recipe.name);
            user.addShoppingListItem(ingredient);
        });
        user.saveUser();
    }

    /**
     * Formats the shopping list item, for displaying in shopping list
     * @param item The item
     * @return The item as a string
     */
    public String shoppingListItemString(ShoppingListItem item){
        if(item.getRecipeName() == null){
            return ingredientScaling.format(item.getIngredient(), 1.0, languages.bundle());
        } else {
            return ingredientScaling.format(item.getIngredient(), 1.0, languages.bundle()) +
                    " (" + item.getRecipeName() + ")";
        }
    }

    /**
     * Formats the shopping list header, for displaying in shopping list
     * @param header The header
     * @return The header as a string
     */
    public String shoppingListHeaderString(ShoppingListHeader header){
        ResourceBundle b = languages.bundle();
        try {
            return b.getString("common.category."+header.getCategory());
        }catch(Exception e){
            return header.toString();
        }
    }

    /**
     * Generates a PDF file from the shopping list information.
     *
     * @param doc               the document to write to
     * @param shoppingListItems the list of shopping list item
     * @throws Exception        if the content cannot be added to the document
     */
    public void addRecipeContentToDocument(Document doc,
                                            List<ShoppingListEntry> shoppingListItems) throws Exception {

        //the language for title

        ResourceBundle b = languages.bundle();
        // Define fonts
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD);
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
        Font sectinFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Font recipeFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC);
        Font headerFont =  FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);

        doc.open();
        doc.add(new Paragraph(b.getString("shopping.title"), titleFont));
        doc.add(new Paragraph(LocalDate.now().toString(), dateFont));
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph(languages.bundle().getString("shoppingListItemsHeader") + ":", sectinFont));
        for (ShoppingListEntry i : shoppingListItems) {
            if(i.getClass().equals(ShoppingListHeader.class)){
                ShoppingListHeader header = (ShoppingListHeader)i;

                doc.add(new Paragraph(" "));

                try {
                    doc.add(new Paragraph(b.getString("common.category." + header.getCategory()), headerFont));
                }catch(Exception e){
                    doc.add(new Paragraph("This category is not supported in this language " + header.getCategory(),  headerFont));
                }
            }
            if(i.getClass().equals(ShoppingListItem.class)){
                ShoppingListItem item = (ShoppingListItem)i;

                Paragraph p = new Paragraph();

                doc.add(new Paragraph(" "));
                p.add(new Chunk(" • " + ingredientScaling.format(item.getIngredient(), 1, languages.bundle()), bodyFont));
                if(item.getRecipeName() != null){
                    p.add(new Chunk(" " + item.getRecipeName(), recipeFont));
                }
                doc.add(p);
            }
        }
    }

    /**
     * Generates a PDF file from the previously created doc (with the recipe content).
     *
     * @param file              the PDF to write to
     * @param shoppingListItems the list of shopping list item
     * @throws Exception        if the PDF cannot be created
     */
    public void writeRecipePDF(File file,
                                List<ShoppingListEntry> shoppingListItems) throws Exception {

        Document doc = new Document();

        // PdfWriter connects the doc to an OutputStream (a file in this case)
        PdfWriter.getInstance(doc, new FileOutputStream(file));

        doc.open();
        addRecipeContentToDocument(doc, shoppingListItems);
        doc.close();
    }

    /**
     * Tries to open the PDF file in the default PDF viewer.
     *
     * @param file to be opened
     */
    public void openPdfInViewer(File file) {
        if (file == null) {
            return;
        }
        try {
            if (!Desktop.isDesktopSupported()) {
                System.out.println("ERROR: Desktop not supported.");
                return;
            }

            Desktop desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.OPEN)) {
                System.out.println("ERROR: Opening PDF not supported.");
                return;
            }

            desktop.open(file);
        } catch (IOException e) {
            System.out.println("ERROR: Could not open PDF in viewer.");
            e.printStackTrace();
        }
    }

    /**
     * Sets properties of file chooser
     */
    public void setFileChooser(FileChooser chooser) {

        // FileChooser is a JavaFX helper that opens a normal "Save as" dialog
        chooser.setTitle(languages.bundle().getString("shopping.fileChooser.savePdf.title"));

        // This limits visible file types to *.pdf
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "PDF files (*.pdf)", "*.pdf"));

        // Suggest a default filename based on the recipe title
        String safeName = ("Shopping list (" + LocalDate.now() + ")").replaceAll("\\s+", "_").toLowerCase();
        chooser.setInitialFileName(safeName + ".pdf");
    }

    public void saveListToFile(File file, List<ShoppingListEntry> shoppingListItems){
        // If the user presses "Cancel", file will be null
        if (file == null) {
            System.out.println("PDF not saved.");
            return;
        }


        try {
            writeRecipePDF(file, shoppingListItems);
            System.out.println("Recipe saved as PDF" + file.getAbsolutePath());

            openPdfInViewer(file);
        } catch (Exception e) {
            System.out.println("ERROR: Could not save recipe PDF.");
            e.printStackTrace();
        }
    }

    /**
     * Prints sends the PDF created base of the list of ShoppingListItems
     * @param shoppingListItems The list of ShoppingListItems
     */
    public void printShoppingList(List<ShoppingListEntry> shoppingListItems) {
        File tempFile;

        try {
            // Create a temporary PDF file
            tempFile = File.createTempFile("shopping_list", ".pdf");

            writeRecipePDF(tempFile, shoppingListItems);
            System.out.println("Temporary shopping list PDF for printing:" + tempFile.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("ERROR: Could not create PDF for printing.");
            e.printStackTrace();
            return;
        }

        // Load the PDF with the PDFBox library
        try (PDDocument document = PDDocument.load(tempFile)) {
            // Create a PrinterJob (the Java printing system)
            PrinterJob job = PrinterJob.getPrinterJob();

            // Wrap the PDF so the printer knows how many pages etc.
            job.setPageable(new PDFPageable(document));

            // Show the print dialog (select printer, pages, etc.)
            if (job.printDialog()) {
                // Print the document
                job.print();
                System.out.println("PDF sent to printer.");
            } else {
                System.out.println("PDF not printed.");
            }
        } catch (Exception e) {
            System.out.println("ERROR: Could not send PDF to printer.");
            e.printStackTrace();
        }
    }

    /**
     * Sorts the list of shopping list items based on name, recipe name and category
     * @param items The list of shopping list item
     * @param sortingOption Which sorting option to use (1-3)
     * @throws IllegalArgumentException The exception when using an incorrect option
     */
    public void sort(List<ShoppingListEntry> items, int sortingOption) throws IllegalArgumentException{

        //we only allow 6 sorting options from 1-3
        if(sortingOption < 1 || sortingOption > 3){
            throw new IllegalArgumentException();
        }

        ArrayList<ShoppingListItem> shoppingListItems =  getShoppingListItems(items);

        switch (sortingOption) {
            case 1: //sort by name ASC
                shoppingListItems.sort(this::compareByNameASC);
                items.clear();
                items.addAll(shoppingListItems);
                break;
            case 2: //sort by recipe name ASC
                shoppingListItems.sort(this::compareByRecipeNameASC);
                items.clear();
                items.addAll(shoppingListItems);
                break;
            case 3: //sort by category ASC
                items.clear();
                items.addAll(shoppingListItems);
                addHeaders(items);
                items.sort(this::compareByCategoryASC);
                break;
        }
    }


    private void addHeaders(List<ShoppingListEntry> items) {
        HashSet<Category> categories = new HashSet<Category>();
        items.stream().filter(x -> x.getClass().equals(ShoppingListItem.class)).map(x -> (ShoppingListItem) x).forEach(x -> {
            if(x.getIngredient().ingredientType.getCategory() != null) {
                categories.add(x.getIngredient().ingredientType.getCategory());
            }else{
                categories.add(Other);
            }
        });
        categories.forEach(x -> {
            items.add(new ShoppingListHeader(x));
        });
    }

    /**
     * Returns positive value if item1 is bigger than item2 base on the name in ascending order
     * @param item1 Shopping list item
     * @param item2 Shopping list item
     * @return integer value
     */
    private int compareByNameASC(ShoppingListItem item1, ShoppingListItem item2){
        if(item1.getIngredient().ingredientType == null){
            return 1;
        }
        if(item2.getIngredient().ingredientType == null){
            return -1;
        }

        int compare = Comparator
                .nullsLast(String::compareTo)
                .compare(item1.getIngredient().ingredientType.name, item2.getIngredient().ingredientType.name);
        if (compare == 0) {
            return Comparator
                    .nullsLast(String::compareTo)
                    .compare(item1.getRecipeName(), item2.getRecipeName());
        }
        return compare;
    }

    /**
     * Returns positive value if item1 is bigger than item2 base on the recipe name in ascending order
     * @param item1 Shopping list item
     * @param item2 Shopping list item
     * @return integer value
     */
    private int compareByRecipeNameASC(ShoppingListItem item1,  ShoppingListItem item2){
        if(item1.getIngredient().ingredientType == null){
            return 1;
        }
        if(item2.getIngredient().ingredientType == null){
            return -1;
        }

        int compare = Comparator
                .nullsLast(String::compareTo)
                .compare(item1.getRecipeName(), item2.getRecipeName());
        if (compare == 0) {
            return compareByNameASC(item1, item2);
        }
        return compare;
    }

    /**
     * Returns positive value if item1 is bigger than item2 base on the category in ascending order
     * @param item1 Shopping list item
     * @param item2 Shopping list item
     * @return integer value
     */
    private int compareItemByCategoryASC(ShoppingListItem item1, ShoppingListItem item2){
        if(item1.getIngredient().ingredientType == null){
            return -1;
        }
        if(item2.getIngredient().ingredientType == null){
            return 1;
        }

        if(compareCategories(getCategory(item1), getCategory(item2)) == 0){
            return compareByNameASC(item1, item2);
        }
        return compareCategories(getCategory(item1), getCategory(item2));
    }

    /**
     * Returns positive value if item1 is bigger than item2 base on the category in ascending order
     * @param item1 Shopping list entry
     * @param item2 Shopping list entry
     * @return integer value
     */
    private int compareByCategoryASC(ShoppingListEntry item1, ShoppingListEntry item2){
        ResourceBundle b = languages.bundle();

        if(item1.getClass().equals(ShoppingListItem.class) && item2.getClass().equals(ShoppingListItem.class)){
            return compareItemByCategoryASC((ShoppingListItem) item1, (ShoppingListItem) item2);
        }
        if(item1.getClass().equals(ShoppingListHeader.class) && item2.getClass().equals(ShoppingListHeader.class)){
            return compareCategories(getCategory(item1), getCategory(item2));
        }
        if(item1.getClass().equals(ShoppingListHeader.class) && item2.getClass().equals(ShoppingListItem.class)){
            if(compareCategories(getCategory(item1), getCategory(item2)) == 0){
                return -1;
            }
            return compareCategories(getCategory(item1), getCategory(item2));
        }
        if(item1.getClass().equals(ShoppingListItem.class) && item2.getClass().equals(ShoppingListHeader.class)){
            if(compareCategories(getCategory(item1), getCategory(item2)) == 0){
                return 1;
            }
            return compareCategories(getCategory(item1), getCategory(item2));
        }
        return 0;
    }

    /**
     * Returns positive value if c1 is bigger than c2 base on the category in ascending order
     * @param c1 The first category
     * @param c2 The second category
     * @return integer value
     */
    private int compareCategories(Category c1, Category c2) {
        if (c1 == c2 || (c1 == null && c2 == Other) || (c1 == Other && c2 == null)) return 0;

        // Other always last
        if (c1 == Other || c1 == null) return 1;
        if (c2 == Other || c2 == null) return -1;

        ResourceBundle b = languages.bundle();
        return b.getString("common.category." + c1)
                .compareTo(b.getString("common.category." + c2));
    }

    private Category getCategory(ShoppingListEntry entry) {
        if (entry.getClass().equals(ShoppingListHeader.class)) {
            return ((ShoppingListHeader)entry).getCategory();
        }
        if (entry.getClass().equals(ShoppingListItem.class)) {
            return ((ShoppingListItem)entry).getIngredient().ingredientType.getCategory();
        }
        return Other;
    }
}
