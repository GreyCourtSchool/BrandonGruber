package uk.sch.greycourt.richmond.brandongruber.revcards;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class RevCardViewer extends BorderPane {

    private final Label titleLabel = new Label();
    private final TextArea contentLabel = new TextArea();

    public RevCardViewer() {
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
