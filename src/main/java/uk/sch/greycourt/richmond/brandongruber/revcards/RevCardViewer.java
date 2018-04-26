package uk.sch.greycourt.richmond.brandongruber.revcards;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RevCardViewer extends BorderPane {

    private final Label titleLabel = new Label();
    private final TextArea contentLabel = new TextArea();
    private final Button previousCardButton = new Button("<");
    private final Button nextCardButton = new Button(">");

    private RevCard currentCard;
    private Project project;

    public RevCardViewer() {
        setPadding(new Insets(4));
        titleLabel.setFont(new Font("Arial", 30));
        contentLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        contentLabel.setDisable(true);
        contentLabel.setWrapText(true);

        HBox hBox = new HBox();

        HBox left = new HBox();
        left.setAlignment(Pos.CENTER_LEFT);
        left.getChildren().add(titleLabel);

        HBox right = new HBox();
        right.setSpacing(4);
        right.setAlignment(Pos.CENTER_RIGHT);

        previousCardButton.setDisable(true);
        nextCardButton.setDisable(true);
        previousCardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int currentIndex = project.getCardList().indexOf(currentCard);
                showCard(project.getCardList().get(currentIndex == 0 ? project.getCardList().size() - 1 : currentIndex - 1));
            }
        });
        nextCardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int currentIndex = project.getCardList().indexOf(currentCard);
                showCard(project.getCardList().get(currentIndex == project.getCardList().size() - 1 ? 0 : currentIndex + 1));
            }
        });
        right.getChildren().addAll(previousCardButton, nextCardButton);

        HBox.setHgrow(hBox, Priority.ALWAYS);
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);

        hBox.getChildren().addAll(left, right);
        setTop(hBox);
        setCenter(contentLabel);
    }

    public void showCardsFor(Project project) {
        this.project = project;
        previousCardButton.setDisable(project.getCardList().isEmpty());
        nextCardButton.setDisable(project.getCardList().isEmpty());

        if (!project.getCardList().isEmpty()) {
            showCard(project.getCardList().get(0));
        }
    }

    private void showCard(RevCard revCard) {
        this.currentCard = revCard;
        titleLabel.setText(revCard.getTitle());
        contentLabel.setText(revCard.getContent());
    }

}
