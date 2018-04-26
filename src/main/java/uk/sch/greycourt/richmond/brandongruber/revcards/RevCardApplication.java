package uk.sch.greycourt.richmond.brandongruber.revcards;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
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
import uk.sch.greycourt.richmond.brandongruber.revcards.dialog.EditCardsDialog;
import uk.sch.greycourt.richmond.brandongruber.revcards.dialog.ProjectDialog;
import uk.sch.greycourt.richmond.brandongruber.revcards.dialog.RevCardDialogue;
import uk.sch.greycourt.richmond.brandongruber.revcards.io.CsvFileReaderWriter;
import uk.sch.greycourt.richmond.brandongruber.revcards.io.RevisionCardReaderWriter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

/**
 * The main application class for RevCards.
 *
 * {@link Application} a javafx application. javafx is the current standard way to build a gui application in java.
 *
 * @see javafx.application.Application
 */
public class RevCardApplication extends Application {


    private Logger logger = LogManager.getLogger(getClass());

    private Set<Project> projects = new TreeSet<>();
    private Menu projectMenu;
    private final RevisionCardReaderWriter revisionCardReaderWriter = new CsvFileReaderWriter();
    private SimpleObjectProperty<Project> projectProperty = new SimpleObjectProperty<>();
    private VBox root;
    private RevCardViewer revCardViewer = new RevCardViewer();
    private Stage stage;

    /**
     * The main entry point for the application. The main method is the standard way of lunching a java application.
     *
     * @param args
     */
    public static void main(String[] args) {
        // launch the javafx GUI application.
        launch(args);
    }

    /**
     * this is called by javafx just before the GUI is shown, so prepare the components to be displayed.
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        HBox menuHBox = new HBox();
        // create a small menu structure
        MenuBar menuBar = getMenuBar();
        menuHBox.getChildren().add(menuBar);

        root = new VBox();
        root.setSpacing(4);
        root.getChildren().addAll(menuHBox, revCardViewer);
        VBox.setVgrow(revCardViewer, Priority.ALWAYS);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("RevCards");
        primaryStage.setScene(scene);
        primaryStage.show();

// load the projects from the project csv file
        loadProjects();
        // add a menu item for each project
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
                RevCardApplication.this.projectProperty.set(project);
                setTitleFor(project);
                try {
                    project.setCardsList(revisionCardReaderWriter.getCardsFor(project));
                    revCardViewer.showCardsFor(project);
                } catch (IOException e) {
                    String message = "Could not read cards for projectProperty";
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

    private void setTitleFor(Project project) {
        stage.setTitle("RevCards - " + project.getName());
    }

    private void loadProjects() {
        try {
            projects.addAll(revisionCardReaderWriter.readProjects());

        } catch (IOException e) {
            String message = "Could not read projects";
            logger.error(message, e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
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
        projectMenu.getItems().addAll(editProjectMenuItem());
        projectMenu.getItems().addAll(new SeparatorMenuItem());
    }

    private MenuItem editProjectMenuItem() {
        MenuItem menuItem = new MenuItem("Edit Project");
        menuItem.disableProperty().bind(projectProperty.isNull());
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Project existingProject = RevCardApplication.this.projectProperty.get();
                ProjectDialog dialog = new ProjectDialog(existingProject);
                Optional<Project> optionalProject = dialog.showAndWait();
                optionalProject.ifPresent((Project project) -> {
                    logger.info("Project edited " + project.getName());
                    project.setCardsList(existingProject.getCardList());
                    RevCardApplication.this.projects.remove(existingProject);
                    RevCardApplication.this.projects.add(project);
                    setTitleFor(project);
                    writeProjects();
                    writeCards();
                });
            }
        });
        return menuItem;
    }

    private MenuItem createNewProjectMenuItem() {
        MenuItem menuItem = new MenuItem("New Project");
        menuItem.setOnAction(event -> {
            ProjectDialog dialog = new ProjectDialog();
            Optional<Project> optionalProject = dialog.showAndWait();
            optionalProject.ifPresent(new Consumer<Project>() {
                @Override
                public void accept(Project project) {
                    // check that projectProperty does not already exist
                    if (projects.contains(project)) {
                        String message = String.format("Project %s already exists", project.getName());
                        logger.info(message);

                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setContentText(message);
                        alert.showAndWait();
                        return;
                    }

                    logger.info("Creating new projectProperty " + project.getName());
                    RevCardApplication.this.projects.add(project);
                    RevCardApplication.this.addOpenProjectMenuItem(project);
                    RevCardApplication.this.writeProjects();

                }
            });
        });
        return menuItem;
    }

    private void writeProjects() {
        try {
            revisionCardReaderWriter.writeProjects(this.projects);
        } catch (IOException e) {
            String message = "Could not write projects";
            logger.error(message, e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setContentText(message);
            alert.showAndWait();
        }
    }


    private Menu getCardsMenu() {
        Menu menu = new Menu("Cards");
        menu.getItems().addAll(createNewCardMenuItem());
        menu.getItems().addAll(createEditCardsMenuItem());
        return menu;
    }


    private MenuItem createEditCardsMenuItem() {
        MenuItem menuItem = new MenuItem("Edit Cards");
        menuItem.disableProperty().bind(projectProperty.isNull());
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                EditCardsDialog dialog = new EditCardsDialog(RevCardApplication.this.projectProperty.get());
                Optional<List<RevCard>> optional = dialog.showAndWait();
                optional.ifPresent((List<RevCard> project) -> {
                    logger.info("Cards edited for projectProperty " + RevCardApplication.this.projectProperty.get().getName());
                    RevCardApplication.this.projectProperty.get().setCardsList(optional.get());
                    writeCards();
                    revCardViewer.showCardsFor(RevCardApplication.this.projectProperty.get());
                });
            }
        });
        return menuItem;
    }


    private MenuItem createNewCardMenuItem() {
        MenuItem menuItem = new MenuItem("New Card");
        menuItem.disableProperty().bind(projectProperty.isNull());
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RevCardDialogue dialogue = new RevCardDialogue();
                Optional<RevCard> optionalCard = dialogue.showAndWait();
                optionalCard.ifPresent(project -> {
                    logger.info("Creating new RevCard " + project.getTitle());
                    RevCardApplication.this.projectProperty.get().addCard(project);
                    writeCards();

                });
            }
        });
        return menuItem;
    }

    private void writeCards() {
        try {
            revisionCardReaderWriter.writeCards(RevCardApplication.this.projects);
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
