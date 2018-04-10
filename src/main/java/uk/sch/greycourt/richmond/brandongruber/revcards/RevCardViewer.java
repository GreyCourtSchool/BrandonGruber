package uk.sch.greycourt.richmond.brandongruber.revcards;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RevCardViewer extends BorderPane {

    private final Label titleLabel = new Label();
    private final TextArea contentLabel = new TextArea();

    public RevCardViewer() {
        setPadding(new Insets(4));
        titleLabel.setFont(new Font("Arial", 30));
        contentLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        contentLabel.setDisable(true);

        setTop(titleLabel);
        setCenter(contentLabel);
    }

    public void showCardsFor(Project project) {
        if (!project.getCardList().isEmpty()) {
            titleLabel.setText(project.getCardList().get(0).getTitle());
            contentLabel.setText(project.getCardList().get(0).getContent());
        }
    }

}
