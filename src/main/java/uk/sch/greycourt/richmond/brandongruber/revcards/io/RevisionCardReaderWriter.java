package uk.sch.greycourt.richmond.brandongruber.revcards.io;

import uk.sch.greycourt.richmond.brandongruber.revcards.Project;
import uk.sch.greycourt.richmond.brandongruber.revcards.RevCard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface RevisionCardReaderWriter {

    /**
     * Writes projects to file.
     *
     * @param outputFile The {@link File} to write the projects to.
     * @param projects   The {@link List} of {@link Project} instances to write to file.
     * @throws IOException If the projects could not be written to file.
     */
    void writeProjects(File outputFile, Collection<Project> projects) throws IOException;

    /**
     * Provides access to projects in the project csv file.
     *
     * @param fileName
     * @return A {@link List} of {@link Project} instances or an empty list if there are no projects.
     * @throws IOException If the project csv file could not be found.
     */
    List<Project> readProjects(String fileName) throws IOException;

    /**
     * write the entire list of revision cards from all projects to the specified file,
     *
     * @param file
     * @param projects
     * @throws IOException
     */
    void writeCards(File file, Set<Project> projects) throws IOException;


    /**
     * gets the list of cards for the specified project
     *
     * @param fileName
     * @param project
     * @return
     * @throws IOException
     */
    List<RevCard> getCardsFor(String fileName, Project project) throws IOException;
}
