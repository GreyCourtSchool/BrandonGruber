package uk.sch.greycourt.richmond.brandongruber.revcards;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * {@link Dialog} extension for adding a new project.
 */
class NewProjectDialog extends Dialog<Project> {

    private final TextField nameTextField = new TextField();
    private final TextField descriptionTextField = new TextField();

    /**
     * Constructor.
     */
    NewProjectDialog() {
        setTitle("New Project");
        GridPane gridPane = new GridPane();
        gridPane.setHgap(4);
        gridPane.setVgap(4);
        gridPane.add(new Label("Title"), 0, 0, 1, 1);
        gridPane.add(new Label("Description"), 0, 1, 1, 1);
        gridPane.add(nameTextField, 1, 0, 1, 1);
        gridPane.add(descriptionTextField, 1, 1, 1, 1);
        getDialogPane().setContent(gridPane);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        setResultConverter(param -> new Project(nameTextField.getText(), descriptionTextField.getText()));
    }

}
