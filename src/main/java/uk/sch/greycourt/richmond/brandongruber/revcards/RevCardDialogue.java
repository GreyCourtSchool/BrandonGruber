package uk.sch.greycourt.richmond.brandongruber.revcards;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

/**
 * {@link Dialog} extension for adding a new project.
 */
class NewRevCardDialogue extends Dialog<RevCard> {

    private final TextField titleTextField;
    private final TextArea contentTextArea;

    /**
     * Constructor.
     */
    NewRevCardDialogue() {
        setTitle("New RevCard");
        GridPane gridPane = new GridPane();
        gridPane.setHgap(4);
        gridPane.setVgap(4);
        gridPane.add(new Label("Title"), 0, 0, 1, 1);
        gridPane.add(new Label("Description"), 0, 1, 1, 1);
        titleTextField = new TextField();
        contentTextArea = new TextArea();
        gridPane.add(titleTextField, 1, 0, 1, 1);
        gridPane.add(contentTextArea, 1, 1, 1, 1);
        getDialogPane().setContent(gridPane);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        setResultConverter(param -> new RevCard(titleTextField.getText(), contentTextArea.getText()));
    }

}
