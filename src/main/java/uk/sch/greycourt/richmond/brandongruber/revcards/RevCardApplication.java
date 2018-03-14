package uk.sch.greycourt.richmond.brandongruber.revcards;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.sch.greycourt.richmond.brandongruber.revcards.io.CsvFileReader;
import uk.sch.greycourt.richmond.brandongruber.revcards.io.CsvFileWriter;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * The main application class for RevCards.
 *
 * @see javafx.application.Application
 */
public class RevCardApplication extends Application {

    private static final String PROJECTS_FILE_PATH = System.getProperty("user.home") + File.separator + "projects.csv";
    private Logger logger = LogManager.getLogger(getClass());

    private Set<Project> projects = new TreeSet<>();
    private CsvFileReader csvFileReader = new CsvFileReader();
    private CsvFileWriter csvFileWriter = new CsvFileWriter();

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

        try {
            File file = new File(PROJECTS_FILE_PATH);
            if (!file.exists()) {
                logger.info("creating {}", PROJECTS_FILE_PATH);
                file.createNewFile();
                logger.info("Projects file {} written", PROJECTS_FILE_PATH);
            }

            projects.addAll(csvFileReader.readProjects(PROJECTS_FILE_PATH));
        } catch (IOException e) {
            String message = "Could not read projects";
            logger.error(message, e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
//            alert.setHeaderText("Look, a Warning Dialog");
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

    private MenuBar getMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(getProjectMenu(), getHelpMenu());
        return menuBar;
    }

    private Menu getProjectMenu() {
        Menu menu = new Menu("Project");
        MenuItem newProjectMenuItem = new MenuItem("New Project");
        newProjectMenuItem.setOnAction(event -> {
            NewProjectDialog newProjectDialogue = new NewProjectDialog();
            Optional<Project> optionalProject = newProjectDialogue.showAndWait();
            optionalProject.ifPresent(project -> {
                // check that project does not already exist
                if (projects.contains(project)) {
                    String message = String.format("Project %s already exists", project.getName());
                    logger.info(message);

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
//                    alert.setHeaderText("Look, a Warning Dialog");
                    alert.setContentText(message);
                    alert.showAndWait();
                }

                logger.info("Creating new project " + project.getName());
                try {
                    this.projects.add(project);
                    csvFileWriter.writeProjects(new File(PROJECTS_FILE_PATH), this.projects);
                } catch (IOException e) {
                    String message = "Could not write projects";
                    logger.error(message, e);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Warning");
//                    alert.setHeaderText("Look, a Warning Dialog");
                    alert.setContentText(message);
                    alert.showAndWait();
                }

            });
            // TODO save project in csv
        });
        menu.getItems().addAll(newProjectMenuItem);
        menu.getItems().addAll(new MenuItem("Open Project"));
        return menu;
    }

    private Menu getHelpMenu() {
        Menu help = new Menu("Help");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event -> Platform.exit());
        help.getItems().addAll(exit);
        return help;
    }
}
