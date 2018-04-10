package uk.sch.greycourt.richmond.brandongruber.revcards;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class EditCardDialog extends Dialog<List<RevCard>> {

    public EditCardDialog(Project project) {
        setTitle("Edit RevCards for Project " + project.getName());
        BorderPane borderPane = new BorderPane();

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(new Button("Add Card"),new Button("Edit Card"), new Button("Remove Card"));
        borderPane.setTop(buttonBox);
        borderPane.setCenter(createListView(project));
        getDialogPane().setContent(borderPane);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        setResultConverter(new Callback<ButtonType, List<RevCard>>() {
            @Override
            public List<RevCard> call(ButtonType param) {
                return new ArrayList<>(listView.getItems());
            }
        });
    }

    private ListView<RevCard> createListView(Project project) {
        ListView<RevCard> listView = new ListView<>();
        listView.getItems().addAll(project.getCardList());
        listView.setCellFactory(new Callback<ListView<RevCard>, ListCell<RevCard>>() {
            @Override
            public ListCell<RevCard> call(ListView<RevCard> param) {
                return new RevCardListCell();
            }
        });
        return listView;
    }

    private static class RevCardListCell extends ListCell<RevCard> {
        @Override
        protected void updateItem(RevCard item, boolean empty) {
            super.updateItem(item, empty);
            setText(item == null ? null : item.getTitle());
        }
    }
}
