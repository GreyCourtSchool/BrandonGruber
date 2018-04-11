package uk.sch.greycourt.richmond.brandongruber.revcards.dialog;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import uk.sch.greycourt.richmond.brandongruber.revcards.Project;

/**
 * {@link Dialog} extension for adding a new or editing an existing project.
 */
public class ProjectDialog extends Dialog<Project> {

    private final TextField nameTextField = new TextField();
    private final TextField descriptionTextField = new TextField();

    /**
     * Constructor.
     */
    public ProjectDialog() {
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

        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(nameTextField.textProperty().isEmpty());

        // the result converter returns a new project if the ok button was pressed
        setResultConverter(new Callback<ButtonType, Project>() {
            @Override
            public Project call(ButtonType param) {
                return new Project(nameTextField.getText(), descriptionTextField.getText());
            }
        });
    }

    /**
     * Constructor used when editing an existing project.
     * @param project
     */
    public ProjectDialog(Project project) {
        this();

        this.nameTextField.setText(project.getName());
        this.descriptionTextField.setText(project.getDescription());
    }
}
