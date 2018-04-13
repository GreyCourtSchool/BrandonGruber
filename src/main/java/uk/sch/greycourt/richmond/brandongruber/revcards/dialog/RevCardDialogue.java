package uk.sch.greycourt.richmond.brandongruber.revcards.dialog;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import uk.sch.greycourt.richmond.brandongruber.revcards.RevCard;

/**
 * {@link Dialog} extension for adding a new project.
 */
public class RevCardDialogue extends Dialog<RevCard> {

    private final TextField titleTextField = new TextField();
    private final TextArea contentTextArea = new TextArea();

    /**
     * Constructor to use when creating a new {@link RevCard} and editing an existing card
     */
    public RevCardDialogue() {
        setTitle("New RevCard");
        GridPane gridPane = new GridPane();
        gridPane.setHgap(4);
        gridPane.setVgap(4);
        gridPane.add(new Label("Title"), 0, 0, 1, 1);
        gridPane.add(new Label("Description"), 0, 1, 1, 1);


        gridPane.add(titleTextField, 1, 0, 1, 1);
        gridPane.add(contentTextArea, 1, 1, 1, 1);
        getDialogPane().setContent(gridPane);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(titleTextField.textProperty().isEmpty());

        setResultConverter(new Callback<ButtonType, RevCard>() {
            @Override
            public RevCard call(ButtonType param) {
                return new RevCard(titleTextField.getText(), contentTextArea.getText());
            }
        });
    }

    /**
     * Constructor to use when editing a {@link RevCard}
     *
     * @param revCard
     */
    public RevCardDialogue(RevCard revCard) {
        this();
        setTitle("Edit RevCard");

        // set the title and the content of the card being edited
        titleTextField.setText(revCard.getTitle());
        contentTextArea.setText(revCard.getContent());
    }
}
