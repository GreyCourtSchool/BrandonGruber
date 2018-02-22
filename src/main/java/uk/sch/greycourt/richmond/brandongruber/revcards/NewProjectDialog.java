package uk.sch.greycourt.richmond.brandongruber.revcards;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 * {@link Dialog} extension for adding a new project.
 */
class NewProjectDialog extends Dialog<Pair<String, String>> {

    private final TextField titleTextField;
    private final TextField descriptionTextField;

    /**
     * Constructor.
     */
    NewProjectDialog() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(4);
        gridPane.setVgap(4);
        gridPane.add(new Label("Title"), 0, 0, 1, 1);
        gridPane.add(new Label("Description"), 0, 1, 1, 1);
        titleTextField = new TextField();
        descriptionTextField = new TextField();
        gridPane.add(titleTextField, 1, 0, 1, 1);
        gridPane.add(descriptionTextField, 1, 1, 1, 1);
        getDialogPane().setContent(gridPane);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        setResultConverter(param -> new Pair<>(titleTextField.getText(), descriptionTextField.getText()));
    }

}
