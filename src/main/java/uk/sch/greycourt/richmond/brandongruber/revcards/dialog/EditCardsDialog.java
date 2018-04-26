package uk.sch.greycourt.richmond.brandongruber.revcards.dialog;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import uk.sch.greycourt.richmond.brandongruber.revcards.Project;
import uk.sch.greycourt.richmond.brandongruber.revcards.RevCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * {@link Dialog} for editing the RevCards for a project. Allows the cards to be added, edited and deleted.
 */
public class EditCardsDialog extends Dialog<List<RevCard>> {

    // a list view component which displays the cards for a project
    private ListView<RevCard> listView = new ListView<>();

    /**
     * Constructor - creates a new dialog to edit the cards for the specified project.
     *
     * @param project the {@link Project} to edit the cards for.
     */
    public  EditCardsDialog(Project project) {
        setTitle("Edit RevCards for Project " + project.getName());
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(4));
        // add buttons to the top
        borderPane.setTop(createButtonBox());

        listView.getItems().addAll(project.getCardList());
        listView.setCellFactory(new Callback<ListView<RevCard>, ListCell<RevCard>>() {
            @Override
            public ListCell<RevCard> call(ListView<RevCard> param) {
                return new RevCardListCell();
            }
        });
        // add the listView component to the center
        borderPane.setCenter(listView);


        getDialogPane().setContent(borderPane);

        // we just need an ok and a cancel button for the dialog
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // the result converter is used to return the updated card list if the dialog ok button is clicked
        setResultConverter(new Callback<ButtonType, List<RevCard>>() {
            @Override
            public List<RevCard> call(ButtonType param) {
                return new ArrayList<>(listView.getItems());
            }
        });
    }

    private HBox createButtonBox() {
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(4);
        Button addCardButton = new Button("Add Card");

        // add a handler for when the add card button is clicked
        addCardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RevCardDialogue dialogue = new RevCardDialogue();
                Optional<RevCard> optional = dialogue.showAndWait();

                // if the add card dialog ok button was clicked, add the card to the list view
                optional.ifPresent(new Consumer<RevCard>() {
                    @Override
                    public void accept(RevCard project) {
                        listView.getItems().addAll(optional.get());
                    }
                });
            }
        });

        Button editCardButton = new Button("Edit Card");
        editCardButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        // add a handler for when the edit card button is clicked
        editCardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int selectedIndex = listView.getSelectionModel().getSelectedIndex();

                // show the revcard dialog to edit the card
                RevCardDialogue dialogue = new RevCardDialogue(listView.getSelectionModel().getSelectedItem());
                Optional<RevCard> optional = dialogue.showAndWait();
                optional.ifPresent(new Consumer<RevCard>() {
                    @Override
                    public void accept(RevCard project) {
                        // replace the old card with the edited card
                        listView.getItems().set(selectedIndex, optional.get());
                    }
                });
            }
        });


        Button deleteCardButton = new Button("Remove Card");
        deleteCardButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        // add a handler which is fired when the button is clicked
        deleteCardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // show a simple conformation dialog to the user
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete RevCard Confirmation");
                alert.setHeaderText("Delete RevCard '" + listView.getSelectionModel().getSelectedItem().getTitle() + "'");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
                }
            }
        });

        buttonBox.getChildren().addAll(addCardButton, editCardButton, deleteCardButton);
        return buttonBox;
    }


    /**
     * cell renderer for the list view component which displays cards
     */
    private static class RevCardListCell extends ListCell<RevCard> {
        @Override
        protected void updateItem(RevCard item, boolean empty) {
            super.updateItem(item, empty);

            // show the card title if the card is not null
            setText(item == null ? null : item.getTitle());
        }
    }
}
