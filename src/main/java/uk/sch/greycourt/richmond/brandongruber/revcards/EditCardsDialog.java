package uk.sch.greycourt.richmond.brandongruber.revcards;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EditCardsDialog extends Dialog<List<RevCard>> {

    private ListView<RevCard> listView = new ListView<>();

    public EditCardsDialog(Project project) {
        setTitle("Edit RevCards for Project " + project.getName());
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(4));
        borderPane.setTop(createButtonBox());

        listView.getItems().addAll(project.getCardList());
        listView.setCellFactory(new Callback<ListView<RevCard>, ListCell<RevCard>>() {
            @Override
            public ListCell<RevCard> call(ListView<RevCard> param) {
                return new RevCardListCell();
            }
        });
        borderPane.setCenter(listView);


        getDialogPane().setContent(borderPane);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
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
        addCardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RevCardDialogue dialogue = new RevCardDialogue();
                Optional<RevCard> optional = dialogue.showAndWait();
                optional.ifPresent(project -> {
                    listView.getItems().addAll(optional.get());
                });
            }
        });

        Button editCardButton = new Button("Edit Card");
        editCardButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        editCardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int selectedIndex = listView.getSelectionModel().getSelectedIndex();
                RevCardDialogue dialogue = new RevCardDialogue(listView.getSelectionModel().getSelectedItem());
                Optional<RevCard> optional = dialogue.showAndWait();
                optional.ifPresent(project -> {
                    listView.getItems().set(selectedIndex, optional.get());
                });
            }
        });


        Button deleteCardButton = new Button("Remove Card");
        deleteCardButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        deleteCardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
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


    private static class RevCardListCell extends ListCell<RevCard> {
        @Override
        protected void updateItem(RevCard item, boolean empty) {
            super.updateItem(item, empty);
            setText(item == null ? null : item.getTitle());
        }
    }
}
