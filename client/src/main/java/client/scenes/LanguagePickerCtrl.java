package client.scenes;

import client.utils.LanguageOption;
import client.utils.LanguageService;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.util.List;

public class LanguagePickerCtrl {

    @FXML
    private ComboBox<LanguageOption> languageBox;

    private final LanguageService languages;
    private final MainCtrl mainCtrl;

    @Inject
    public LanguagePickerCtrl(LanguageService languages, MainCtrl mainCtrl) {
        this.languages = languages;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    public void initialize() {
        var options = List.of(
                new LanguageOption("en", "lang.en", "/flags/en.png"),
                new LanguageOption("nl", "lang.nl", "/flags/nl.png"),
                new LanguageOption("pt", "lang.pt", "/flags/pt.png")
        );

        languageBox.getItems().setAll(options);

        // customize how each item looks (with the flag and translated label)
        languageBox.setCellFactory(cb -> new LanguageCell(languages));
        // the dropdown list items
        languageBox.setButtonCell(new LanguageCell(languages));
        // the selected item

        selectTag(languages.getLanguageTag());

//        String currentTag = languages.getLanguageTag();
//
//        options.stream()
//                .filter(option -> option.tag().equalsIgnoreCase(currentTag))
//                .findFirst()    // first matching element wrapped in an Optional (in case there is no match)
//                .ifPresent(option -> languageBox.getSelectionModel().select(option));

        languageBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;
            if (oldVal != null && newVal.tag().equalsIgnoreCase(oldVal.tag())) return;
            // do nothing if the item is null or the language is the same
//            System.out.println("Selected language: " + newVal.tag());

            languages.setLanguageTag(newVal.tag());
            mainCtrl.applyTranslationsToAllScreens();
        });

        languages.languageTagProperty().addListener((obs, oldVal, newVal) -> {
            selectTag(newVal);

            // so the picker updates as well:
            languageBox.setButtonCell(new LanguageCell(languages));
            languageBox.setCellFactory(cb -> new LanguageCell(languages));
        });

    }

    private void selectTag(String tag){
        if (tag == null) tag = "en";
        for (var option : languageBox.getItems()) {
            if (option.tag().equalsIgnoreCase(tag)) {
                if (languageBox.getValue() == null || !languageBox.getValue().tag().equalsIgnoreCase("tag")) {
                    languageBox.getSelectionModel().select(option);
                }
                return;
            }
        }
    }

    private static class LanguageCell extends ListCell<LanguageOption> {
        // new class because we want each cell to have the flag image
        
        private final LanguageService languages;
        private final ImageView icon = new ImageView();

        LanguageCell(LanguageService languages) {
            this.languages = languages;
            icon.setFitHeight(16);
            icon.setFitWidth(16);
            icon.setPreserveRatio(true);
        }

        @Override
        protected void updateItem(LanguageOption item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null){
                setText(null);
                setGraphic(null);
                icon.setImage(null);
                return;
            }

//            setText(languages.bundle().getString(item.labelKey()));
            setText(languages.translate(item.labelKey()));

            var url = getClass().getResource(item.flagPath());
            icon.setImage(url == null ? null : new Image(url.toExternalForm()));
            setGraphic(icon);
        }
    }
}
