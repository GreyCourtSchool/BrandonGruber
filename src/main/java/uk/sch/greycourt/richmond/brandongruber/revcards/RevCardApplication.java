package uk.sch.greycourt.richmond.brandongruber.revcards;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.sch.greycourt.richmond.brandongruber.revcards.io.CsvFileReaderWriter;
import uk.sch.greycourt.richmond.brandongruber.revcards.io.RevisionCardReaderWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

/**
 * The main application class for RevCards.
 *
 * @see javafx.application.Application
 */
public class RevCardApplication extends Application {

    private static final String PROJECTS_FILE_PATH = System.getProperty("user.home") + File.separator + "projects.csv";
    private static final String CARDS_FILE_PATH = System.getProperty("user.home") + File.separator + "cards.csv";
    private Logger logger = LogManager.getLogger(getClass());

    private Set<Project> projects = new TreeSet<>();
    private Menu projectMenu;
    private final RevisionCardReaderWriter revisionCardReaderWriter = new CsvFileReaderWriter();
    private Project project;
    private VBox root;
    private RevCardViewer revCardViewer = new RevCardViewer();
    private Stage stage;

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
        this.stage = primaryStage;

        HBox menuHBox = new HBox();
        MenuBar menuBar = getMenuBar();
        menuHBox.getChildren().add(menuBar);

        root = new VBox();
        root.getChildren().add(menuHBox);
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("RevCards");
        primaryStage.setScene(scene);
        primaryStage.show();

        loadProjects();
        projects.forEach(new Consumer<Project>() {
            @Override
            public void accept(Project project) {
                RevCardApplication.this.addOpenProjectMenuItem(project);
            }
        });
    }

    private void addOpenProjectMenuItem(Project project) {
        MenuItem menuItem = new MenuItem(project.getName());
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RevCardApplication.this.project = project;
                stage.setTitle("RevCards - " + project.getName());
                try {
                    project.setCardsList(revisionCardReaderWriter.getCardsFor(CARDS_FILE_PATH, project));
                } catch (IOException e) {
                    String message = "Could not read cards for project";
                    logger.error(message, e);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Warning");
                    alert.setContentText(message);
                    alert.showAndWait();
                }
            }
        });
        projectMenu.getItems().addAll(menuItem);
    }

    private void loadProjects() {
        try {
            File file = new File(PROJECTS_FILE_PATH);
            if (!file.exists()) {
                logger.info("creating {}", PROJECTS_FILE_PATH);
                file.createNewFile();
                logger.info("Projects file {} written", PROJECTS_FILE_PATH);
            }

            projects.addAll(revisionCardReaderWriter.readProjects(PROJECTS_FILE_PATH));

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
        createProjectMenu();
        menuBar.getMenus().addAll(projectMenu, getCardsMenu(), getHelpMenu());
        return menuBar;
    }

    private void createProjectMenu() {
        this.projectMenu = new Menu("Projects");
        projectMenu.getItems().addAll(createNewProjectMenuItem());
        projectMenu.getItems().addAll(editProjectCardMenuItem());
        projectMenu.getItems().addAll(new SeparatorMenuItem());
    }

    private Menu getCardsMenu() {
        Menu menu = new Menu("Cards");
        menu.getItems().addAll(createNewCardMenuItem());
        menu.getItems().addAll(createEditCardMenuItem());
        menu.getItems().addAll(new SeparatorMenuItem());
        menu.getItems().addAll(createViewCardsMenuItem());
        return menu;
    }

    private MenuItem editProjectCardMenuItem() {
        MenuItem menuItem = new MenuItem("Edit Project");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                EditProjectDialog dialog = new EditProjectDialog(RevCardApplication.this.project);
                Optional<Project> optionalProject = dialog.showAndWait();
                optionalProject.ifPresent(project -> {
                    logger.info("Project edited " + project.getName());
                    RevCardApplication.this.projects.add(project);
                    writeProjects();
                });
            }
        });
        return menuItem;
    }

    private MenuItem createEditCardMenuItem() {
        MenuItem menuItem = new MenuItem("Edit Cards");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                EditCardDialog dialog = new EditCardDialog(RevCardApplication.this.project);
                Optional<List<RevCard>> optional = dialog.showAndWait();
                optional.ifPresent(project -> {
                    logger.info("Cards edited for project " + RevCardApplication.this.project);
                    RevCardApplication.this.project.setCardsList(optional.get());
                    writeCards();
                });
            }
        });
        return menuItem;
    }

    private MenuItem createViewCardsMenuItem() {
        MenuItem menuItem = new MenuItem("View Cards");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!root.getChildren().contains(revCardViewer)) {
                    root.getChildren().add(revCardViewer);
                    VBox.setVgrow(revCardViewer, Priority.ALWAYS);
                }
                revCardViewer.showCardsFor(project);
            }
        });
        return menuItem;
    }

    private MenuItem createNewCardMenuItem() {
        MenuItem newCardMenuItem = new MenuItem("New Card");
        newCardMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NewRevCardDialogue dialogue = new NewRevCardDialogue();
                Optional<RevCard> optionalCard = dialogue.showAndWait();
                optionalCard.ifPresent(project -> {
                    logger.info("Creating new RevCard " + project.getTitle());
                    RevCardApplication.this.project.addCard(project);
                    writeCards();

                });
            }
        });
        return newCardMenuItem;
    }

    private void writeCards() {
        try {
            revisionCardReaderWriter.writeCards(new File(CARDS_FILE_PATH), RevCardApplication.this.projects);
        } catch (IOException e) {
            String message = "Could not write projects";
            logger.error(message, e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

    private MenuItem createNewProjectMenuItem() {
        MenuItem newProjectMenuItem = new MenuItem("New Project");
        newProjectMenuItem.setOnAction(event -> {
            NewProjectDialog dialog = new NewProjectDialog();
            Optional<Project> optionalProject = dialog.showAndWait();
            optionalProject.ifPresent(project -> {
                // check that project does not already exist
                if (projects.contains(project)) {
                    String message = String.format("Project %s already exists", project.getName());
                    logger.info(message);

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText(message);
                    alert.showAndWait();
                    return;
                }

                logger.info("Creating new project " + project.getName());
                this.projects.add(project);
                RevCardApplication.this.addOpenProjectMenuItem(project);
                writeProjects();

            });
            // TODO save project in csv
        });
        return newProjectMenuItem;
    }

    private void writeProjects() {
        try {
            revisionCardReaderWriter.writeProjects(new File(PROJECTS_FILE_PATH), this.projects);
        } catch (IOException e) {
            String message = "Could not write projects";
            logger.error(message, e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

    private Menu getHelpMenu() {
        Menu help = new Menu("Help");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event -> Platform.exit());
        help.getItems().addAll(exit);
        return help;
    }
}
