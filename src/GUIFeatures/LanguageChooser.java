package GUIFeatures;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class LanguageChooser extends ComboBox {

    public static final String PROMPT_TEXT = "Choose Language";

    public LanguageChooser(double xPos, double yPos) {
        super();
        ObservableList<String> languages = FXCollections.observableArrayList(
                "English",
                "Chinese",
                "French",
                "German",
                "Italian",
                "Portuguese",
                "Russian",
                "Spanish",
                "Urdu"
        );
        this.getItems().addAll(languages);
        this.getStyleClass().add("combo-box");
        this.setPromptText(PROMPT_TEXT);
        this.setLayoutX(xPos);
        this.setLayoutY(yPos);
    }
}
