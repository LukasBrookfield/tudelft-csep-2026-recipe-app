package client.utils;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import commons.Ingredient;
import commons.Recipe;
import commons.ShoppingListItem;
import commons.Unit;
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
import java.util.List;

public class ShoppingListUtils {

    private final IngredientScaling ingredientScaling;
    private final LanguageService languages;

    @Inject
    public ShoppingListUtils(IngredientScaling ingredientScaling, LanguageService languages) {
        this.ingredientScaling = ingredientScaling;
        this.languages = languages;
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
                            i.amount = scale * i.amount;
                            return new ShoppingListItem(i);
                        })
                        .toList()
        );
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

        try{
            Double.parseDouble(amount);
        }catch(Exception e){
            System.out.println("Invalid amount");
            return false;
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
            ingredient.amount = Double.parseDouble(amount);
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
     * @param ingredientListView The list of ingredients
     * @param recipe The recipe from which ingredients comes from
     * @param user The user
     */
    public void confirmAddingIngredients(List<ShoppingListItem> ingredientListView, Recipe recipe, UserConfig user){
        ingredientListView.forEach(ingredient -> {
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
            return ingredientScaling.format(item.getIngredient(), 1.0);
        } else {
            return ingredientScaling.format(item.getIngredient(), 1.0) +
                    " (" + item.getRecipeName() + ")";
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
                                            List<ShoppingListItem> shoppingListItems) throws Exception {

        // Define fonts
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD);
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
        Font sectinFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Font recipeFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC);

        doc.open();
        doc.add(new Paragraph("Shopping list", titleFont));
        doc.add(new Paragraph(LocalDate.now().toString(), dateFont));
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Items:", sectinFont));
        doc.add(new Paragraph(" "));
        for (ShoppingListItem i : shoppingListItems) {
            Paragraph p = new Paragraph();

            doc.add(new Paragraph(" "));
            p.add(new Chunk(" • " + ingredientScaling.format(i.getIngredient(), 1), bodyFont));
            if(i.getRecipeName() != null){
                p.add(new Chunk(" " + i.getRecipeName(), recipeFont));
            }
            doc.add(p);
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
                                List<ShoppingListItem> shoppingListItems) throws Exception {

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

    public void saveListToFile(File file, List<ShoppingListItem> shoppingListItems){
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
    public void printShoppingList(List<ShoppingListItem> shoppingListItems) {
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
}
