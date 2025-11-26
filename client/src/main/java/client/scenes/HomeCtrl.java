package client.scenes;

import com.google.inject.Inject;

import client.utils.ServerUtils;
import com.lowagie.text.pdf.PdfDocument;
import commons.*;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import java.awt.print.PrinterJob;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

public class HomeCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    // Root
    @FXML
    private AnchorPane rootPane;

    // Top bar
    @FXML
    private Button refreshButton;

    // Sidebar
    @FXML
    private TextField recipeSearchField;

    @FXML
    private ListView<Recipe> recipeListView;

    @FXML
    private Button addRecipeButton;

    @FXML
    private Button removeRecipeButton;

    // Title row
    @FXML
    private Label recipeTitleLabel;

    @FXML
    private Button editRecipeTitleButton;

    // Ingredients
    @FXML
    private Label ingredientsHeaderLabel;

    @FXML
    private ListView<Ingredient> ingredientListView;

    @FXML
    private Button removeIngredientButton;

    @FXML
    private Button addIngredientButton;

    @FXML
    private Button editIngredientButton;

    // Preparation
    @FXML
    private Label preparationHeaderLabel;

    @FXML
    private ListView<String> preparationStepListView;

    @FXML
    private Button removeStepButton;

    @FXML
    private Button addStepButton;

    @FXML
    private Button editStepButton;

    @FXML
    private Button downloadRecipeButton;

    @FXML
    private Button printRecipeButton;

    @Inject
    public HomeCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    @FXML
    private void initialize() {
        // Dummy content
        Ingredient tomato = new Ingredient("tomato", 400.0, Unit.G);
        Ingredient oliveOil = new Ingredient("olive oil", 30.0, Unit.ML);
        Ingredient onion = new Ingredient("onion", 100.0, Unit.G);
        List<Ingredient> tomatoSauceIngredients = new ArrayList<>(List.of(tomato, oliveOil, onion));
        Recipe tomatoSauce = new Recipe("Tomato Sauce", tomatoSauceIngredients,
                List.of("Do this and that","Heat up"));

        recipeListView.getItems().setAll(tomatoSauce);

        ingredientListView.getItems().setAll(tomato, oliveOil, onion);

        preparationStepListView.getItems().setAll(
                "Do this and that",
                "Heat up"
        );

        recipeTitleLabel.setText("[Dummy recipe selected]");
    }

    @FXML
    private void onRefresh() {
        // MISSING
    }

    @FXML
    private void onAddRecipe() {}

    @FXML
    private void onRemoveRecipe() {}

    private void writeRecipePDF(File file, String title, List<Ingredient> ingredients, List<String> steps) throws Exception {

        // Define fonts
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD);
        Font sectinFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        // doc is the document we're building
        Document doc = new Document();

        // PdfWriter connects the doc to an OutputStream (a file in this case)
        PdfWriter.getInstance(doc, new FileOutputStream(file));

        doc.open();
        doc.add(new Paragraph("Recipe: " + title, titleFont));
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Ingredients:", sectinFont));
        for (Ingredient i : ingredients){
            doc.add(new Paragraph(" • " + i, bodyFont));
        }
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Preparation:", sectinFont));
        for (int i = 0; i < steps.size(); i++) {
            Object step = steps.get(i);
            doc.add(new Paragraph(String.valueOf(i + 1) + ". " + step, bodyFont));
        }
        doc.close();

    }

    @FXML
    private void onDownloadRecipe() {
        String title = recipeTitleLabel.getText();

        if (title == null || title.isBlank()){
            throw new IllegalStateException("ERROR: No recipe selected. Cannot save PDF.");
        }

        var ingredients = ingredientListView.getItems();
        var steps = preparationStepListView.getItems();

        // FileChooser is a JavaFX helper that opens a normal "Save as" dialog
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save recipe as PDF");

        // This limits visible file types to *.pdf
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));

        // Suggest a default filename based on the recipe title
        String safeName = title.replaceAll("\\s+","_").toLowerCase();
        chooser.setInitialFileName(safeName + ".pdf");

        // Show the dialog
        File file = chooser.showSaveDialog(rootPane.getScene().getWindow());

        // If the user presses "Cancel", file will be null
        if (file == null) {
            System.out.println("PDF not saved.");
            return;
        };

        try{
            writeRecipePDF(file, title, ingredients, steps);
            System.out.println("Recipe saved as PDF" + file.getAbsolutePath());
        } catch (Exception e){
            System.out.println("ERROR: Could not save recipe PDF.");
            e.printStackTrace();
        }
    }


    @FXML
    private void onPrintRecipe() {
        String title = recipeTitleLabel.getText();

        if (title == null || title.isBlank()) {
            throw new IllegalStateException("ERROR: No recipe selected. Cannot print PDF.");
        }

        var ingredients = ingredientListView.getItems();
        var steps = preparationStepListView.getItems();

        try {
            // Create a temporary PDF file
            File tempFile = File.createTempFile("recipe", ".pdf");

            writeRecipePDF(tempFile, title, ingredients, steps);
            System.out.println("Temporary recipe PDF for printing:" + tempFile.getAbsolutePath());

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

            }
        } catch (Exception e) {
                System.out.println("ERROR: Could not generate PDF for printing.");
                e.printStackTrace();
            }
        }
    // MAYBE KEEP SOMETHING LIKE THIS FROM THE PROJECT TEMPLATE:
//    public void keyPressed(KeyEvent e) {
//        switch (e.getCode()) {
//            case ENTER:
//                ok();
//                break;
//            case ESCAPE:
//                cancel();
//                break;
//            default:
//                break;
//        }
//    }
}