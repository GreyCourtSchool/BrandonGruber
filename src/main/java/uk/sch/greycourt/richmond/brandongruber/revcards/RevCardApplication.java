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

/**
 * The main application class for RevCards.
 *
 * @see javafx.application.Application
 */
public class RevCardApplication extends Application {

    /**
     * The main entry point for the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        MenuBar menuBar = getMenuBar();
        root.getChildren().add(menuBar);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("RevCards");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar getMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(getProjectMenu(), getHelpMenu());
        return menuBar;
    }

    private Menu getProjectMenu() {
        Menu project = new Menu("Project");
        MenuItem newProjectMenuItem = new MenuItem("New Project");
        newProjectMenuItem.setOnAction(event -> {
            NewProjectDialog newProjectDialogue = new NewProjectDialog();
            Optional<Pair<String, String>> optionalPair = newProjectDialogue.showAndWait();
            optionalPair.ifPresent(stringStringPair -> {
                System.out.println("Creating new project " + stringStringPair.getKey());

            });
            // TODO save project in csv
        });
        project.getItems().addAll(newProjectMenuItem);
        project.getItems().addAll(new MenuItem("Open Project"));
        return project;
    }

    private Menu getHelpMenu() {
        Menu help = new Menu("Help");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event -> Platform.exit());
        help.getItems().addAll(exit);
        return help;
    }
}
