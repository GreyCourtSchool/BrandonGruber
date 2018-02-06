package uk.sch.greycourt.richmond.brandongruber.revcards;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Optional;

public class RevCardApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        MenuBar menuBar = new MenuBar();
        Menu project = new Menu("Project");
        MenuItem newProjectMenuItem = new MenuItem("New Project");
        newProjectMenuItem.setOnAction(event -> {
            NewProjectDialogue newProjectDialogue = new NewProjectDialogue();
            Optional<Pair<String, String>> optionalPair = newProjectDialogue.showAndWait();
            optionalPair.ifPresent(stringStringPair -> System.out.println("Creating new project" + stringStringPair.getKey()));
            // TODO save project in csv
        });
        project.getItems().addAll(newProjectMenuItem);
        project.getItems().addAll(new MenuItem("Open Project"));
        Menu help = new Menu("Help");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event -> Platform.exit());
        help.getItems().addAll(exit);
        menuBar.getMenus().addAll(project, help);
        root.getChildren().add(menuBar);
//        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("RevCards");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
