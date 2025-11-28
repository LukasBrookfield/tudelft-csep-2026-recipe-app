package client.scenes;

import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private Button removeRecipeButton;

    @FXML
    private Button addRecipeButton;

    // Title row
    @FXML
    private Label recipeTitleLabel;

    @FXML
    private TextField recipeTitleField;

    @FXML
    private Button editRecipeButton;

    @FXML
    private Separator mainSeparator;

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

    @FXML
    private TextField editIngredientNameField;

    @FXML
    private TextField editIngredientAmountField;

    @FXML
    private ChoiceBox<String> editUnitBox;

    @FXML
    private Button cancelEditIngredientButton;

    @FXML
    private Button doneEditIngredientButton;

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

    @FXML
    private TextField editStepField;

    @FXML
    private Button cancelEditStepButton;

    @FXML
    private Button doneEditStepButton;

    @FXML
    private Button cancelEditButton;

    @FXML
    private Button doneEditButton;

    @Inject
    public HomeCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    /**
     * Changes the scene between viewing and editing a recipe
     * @param value false for viewing mode, true for editing mode
     */
    private void changeViewEditMode(boolean value) {
        recipeTitleLabel.setVisible(!value);
        recipeTitleField.setVisible(value);
        editRecipeButton.setVisible(!value);

        removeIngredientButton.getParent().setVisible(value);
        removeStepButton.getParent().setVisible(value);

        cancelEditButton.setVisible(value);
        doneEditButton.setVisible(value);

        addRecipeButton.setVisible(!value);
        removeRecipeButton.setVisible(!value);

        recipeSearchField.setDisable(value);
        recipeListView.setDisable(value);
    }

    /**
     * Changes the scene between viewing and editing the preparation steps
     * @param value false for viewing mode, true for editing mode
     */
    private void changeStepViewEditMode(boolean value) {
        removeStepButton.getParent().setVisible(!value);
        editStepField.getParent().setVisible(value);
        removeStepButton.getParent().setMouseTransparent(value);
    }

    /**
     * Changes the scene between viewing and editing the ingredients
     * @param value false vor viewing mode, true for editing mode
     */
    private void changeIngredientViewEditMode(boolean value) {
        removeIngredientButton.getParent().setVisible(!value);
        editIngredientNameField.getParent().setVisible(value);
        removeIngredientButton.getParent().setMouseTransparent(value);
    }

    /**
     * Sets the title, ingredient list and preparation step list based on which
     * recipe is selected
     */
    private void setLabelsAndFields() {
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
        if (recipe != null) {
            recipeTitleLabel.setText(recipe.name);

            List<Ingredient> ingredients = new ArrayList<>(recipe.ingredients);
            ingredientListView.setItems(FXCollections.observableList(ingredients));

            List<String> steps = new ArrayList<>(recipe.steps);
            preparationStepListView.setItems(FXCollections.observableList(steps));
        }
    }

    /**
     * Initializes the home screen with default values
     */
    @FXML
    private void initialize() {
        changeIngredientViewEditMode(false);
        changeStepViewEditMode(false);
        changeViewEditMode(false);
        recipeTitleField.setVisible(false);

        editUnitBox.getItems().addAll("", "G", "ML", "TBSP", "TSP", "PINCH",
                "HANDFUL", "TO_TASTE");

        onRefresh();

        recipeListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldRecipe, newRecipe) -> {
                    onRefresh();
                }
        );
    }

    /**
     * On action method for the Refresh button
     * It refreshes the scene and also gets used automatically in some
     * places, so there is actually no manual refresh needed (button will be
     * removed later)
     */
    @FXML
    private void onRefresh() {
        recipeListView.refresh();

        boolean empty = recipeListView.getItems().isEmpty();
        mainSeparator.getParent().setVisible(!empty);

        setLabelsAndFields();
    }

    /**
     * On action method for the Add Recipe Button
     * Adds an empty recipe (default name, ingredient and step lists are null) and
     * then selects this recipe
     */
    @FXML
    private void onAddRecipe() {
        Recipe recipe = new Recipe("New recipe");

        recipeListView.getItems().add(recipe);
        recipeListView.getSelectionModel().select(
                recipeListView.getItems().size() - 1
        );
    }

    /**
     * On action method for the Remove Recipe button
     * It removes the currently selected recipe
     */
    @FXML
    private void onRemoveRecipe() {
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
        recipeListView.getItems().remove(recipe);
        onRefresh();
    }


    /**
     * Generates a PDF file from the recipe information.
     * @param doc the document to write to
     * @param title the title of the recipe
     * @param ingredients the list of ingredients belonging to the recipe
     * @param steps the ordered list of preparation steps
     * @throws Exception if the content cannot be added to the document
     */
    private void addRecipeContentToDocument(Document doc,
                                            String title, List<Ingredient> ingredients,
                                            List<String> steps) throws Exception {

        // Define fonts
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD);
        Font sectinFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

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
    }


    /**
     * Generates a PDF file from the previously created doc (with the recipe content).
     * @param file the PDF to write to
     * @param title the title of the recipe
     * @param ingredients the list of ingredients belonging to the recipe
     * @param steps the ordered list of preparation steps
     * @throws Exception if the PDF cannot be created
     */
    private void writeRecipePDF(File file, String title,
                                List<Ingredient> ingredients, List<String> steps) throws Exception {

        Document doc = new Document();

        // PdfWriter connects the doc to an OutputStream (a file in this case)
        PdfWriter.getInstance(doc, new FileOutputStream(file));

        doc.open();
        addRecipeContentToDocument(doc, title, ingredients, steps);
        doc.close();
    }

    /**
     * Tries to open the PDF file in the default PDF viewer.
     * @param file to be opened
     */
    private void openPdfInViewer(File file) {
        if (file == null){
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
     * Download button:
     * Asks the user where to save the PDF, then writes the recipe PDF there.
     */
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
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "PDF files (*.pdf)", "*.pdf"));

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

            openPdfInViewer(file);
        } catch (Exception e){
            System.out.println("ERROR: Could not save recipe PDF.");
            e.printStackTrace();
        }
    }


    /**
     * Print button:
     * Creates a temporary PDF file and sends it to the OS printer.
     */
    @FXML
    private void onPrintRecipe() {
        String title = recipeTitleLabel.getText();

        if (title == null || title.isBlank()) {
            throw new IllegalStateException("ERROR: No recipe selected. Cannot print PDF.");
        }

        var ingredients = ingredientListView.getItems();
        var steps = preparationStepListView.getItems();

        File tempFile;

        try {
            // Create a temporary PDF file
            tempFile = File.createTempFile("recipe", ".pdf");

            writeRecipePDF(tempFile, title, ingredients, steps);
            System.out.println("Temporary recipe PDF for printing:" + tempFile.getAbsolutePath());
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

    /**
     * On action method for the Edit Recipe button
     * Changes the scene to Edit Mode
     */
    @FXML
    private void onEditRecipeButton() {
        changeViewEditMode(true);
        recipeTitleField.setText(recipeTitleLabel.getText());
    }

    /**
     * On action method for the Remove Ingredient Button
     * Removes the currently selected ingredient (if any), note that a change like
     * this only affects the recipe if the user presses 'Done' later
     */
    @FXML
    private void onRemoveIngredientButton() {
        if (ingredientListView.getItems().isEmpty()) {
            System.out.println("There is no ingredient to remove.");
            return;
        }
        if (ingredientListView.getSelectionModel().getSelectedItem() == null) {
            System.out.println("There is no ingredient selected.");
            return;
        }
        int index = ingredientListView.getSelectionModel().getSelectedIndex();
        ingredientListView.getItems().remove(index);
    }

    /**
     * On action method for the Add Ingredient button
     * Adds a new ingredient to the ListView with a default name. Amount and Unit are
     * set to null. It then selects this ingredient
     */
    @FXML
    private void onAddIngredientButton() {
        ingredientListView.getItems().add(new Ingredient("New ingredient"));
        ingredientListView.getSelectionModel().select(
                ingredientListView.getItems().size() - 1
        );
    }

    /**
     * On action method for the Edit Ingredient button
     * Changes the scene to Edit Ingredient Mode
     */
    @FXML
    private void onEditIngredientButton() {
        if (ingredientListView.getItems().isEmpty()) {
            System.out.println("There is no ingredient to edit.");
            return;
        }
        if (ingredientListView.getSelectionModel().getSelectedItem() == null) {
            System.out.println("There is no ingredient selected.");
            return;
        }
        changeIngredientViewEditMode(true);

        Ingredient ingredient = ingredientListView.getSelectionModel().getSelectedItem();
        editIngredientNameField.setText(ingredient.name);
        if (ingredient.amount != null) {
            editIngredientAmountField.setText(String.valueOf(ingredient.amount));
        } else {
            editIngredientAmountField.setText("");
        }
        if (ingredient.unit != null) {
            editUnitBox.setValue(ingredient.unit.name());
        } else {
            editUnitBox.setValue("");
        }

        cancelEditButton.setVisible(false);
        doneEditButton.setVisible(false);
    }

    /**
     * On action method for the Cancel Edit Ingredient button
     */
    @FXML
    private void onCancelEditIngredientButton() {
        changeIngredientViewEditMode(false);
        cancelEditButton.setVisible(true);
        doneEditButton.setVisible(true);
    }

    /**
     * On action method for the Done Edit Ingredient button
     * Note that this only changes the recipe if the user presses 'Done' later
     */
    @FXML
    private void onDoneEditIngredientButton() {
        if (editIngredientNameField.getText().isEmpty()) {
            System.out.println("A name is required.");
            return;
        }
        if (!editUnitBox.getValue().equals("TO_TASTE")
                && editIngredientAmountField.getText().isEmpty()) {
            System.out.println("This unit needs an amount.");
            return;
        }
        if (editUnitBox.getValue().equals("TO_TASTE")
                && !editIngredientAmountField.getText().isEmpty()) {
            System.out.println("This unit cannot have an amount.");
            return;
        }

        int index = ingredientListView.getSelectionModel().getSelectedIndex();
        Ingredient ingredient = ingredientListView.getItems().get(index);

        ingredient.name = editIngredientNameField.getText();
        if (!editIngredientAmountField.getText().isEmpty()) {
            ingredient.amount = Double.parseDouble(editIngredientAmountField.getText());
        } else {
            ingredient.amount = null;
        }
        if (!editUnitBox.getValue().isEmpty()) {
            ingredient.unit = Unit.valueOf(editUnitBox.getValue());
        } else {
            ingredient.unit = null;
        }
        ingredientListView.getItems().set(index, ingredient);

        changeIngredientViewEditMode(false);
        cancelEditButton.setVisible(true);
        doneEditButton.setVisible(true);
    }

    /**
     * On action method for the Remove Step button
     * Removes the currently selected step (if any)
     */
    @FXML
    private void onRemoveStepButton() {
        if (preparationStepListView.getItems().isEmpty()) {
            System.out.println("There is no preparation step to remove.");
            return;
        }
        if (preparationStepListView.getSelectionModel().getSelectedItem() == null) {
            System.out.println("There is no preparation step selected.");
            return;
        }
        int index = preparationStepListView.getSelectionModel().getSelectedIndex();
        preparationStepListView.getItems().remove(index);
    }

    /**
     * On action method for the Add Step button
     * Adds a new step to the ListView with a default name
     */
    @FXML
    private void onAddStepButton() {
        preparationStepListView.getItems().add("New step");
        preparationStepListView.getSelectionModel().select(
                preparationStepListView.getItems().size() - 1
        );
    }

    /**
     * On action method for the Edit Step button
     */
    @FXML
    private void onEditStepButton() {
        if (preparationStepListView.getItems().isEmpty()) {
            System.out.println("There is no preparation step to edit.");
            return;
        }
        if (preparationStepListView.getSelectionModel().getSelectedItem() == null) {
            System.out.println("There is no preparation step selected.");
            return;
        }
        changeStepViewEditMode(true);

        String step = preparationStepListView.getSelectionModel().getSelectedItem();
        editStepField.setText(step);

        cancelEditButton.setVisible(false);
        doneEditButton.setVisible(false);
    }

    /**
     * On action method for the Cancel Edit Step button
     */
    @FXML
    private void onCancelEditStepButton() {
        changeStepViewEditMode(false);
        cancelEditButton.setVisible(true);
        doneEditButton.setVisible(true);
    }

    /**
     * On action method for the Done Edit Step button
     */
    @FXML
    private void onDoneEditStepButton() {
        if (editStepField.getText().isEmpty()) {
            System.out.println("The step cannot be empty.");
            return;
        }
        int index = preparationStepListView.getSelectionModel().getSelectedIndex();
        preparationStepListView.getItems().set(index, editStepField.getText());

        changeStepViewEditMode(false);
        cancelEditButton.setVisible(true);
        doneEditButton.setVisible(true);
    }

    /**
     * On action method for the Cancel Edit Button
     * The original recipe is not changed
     */
    @FXML
    private void onCancelEditButton() {
        onRefresh();
        changeViewEditMode(false);
    }

    /**
     * On action method for the Done Edit button
     * All the changes are added to the selected recipe
     */
    @FXML
    private void onDoneEditButton() {
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
        recipe.name = recipeTitleField.getText();
        recipe.ingredients = ingredientListView.getItems();
        recipe.steps = preparationStepListView.getItems();
        onRefresh();
        changeViewEditMode(false);
    }
}