package uk.sch.greycourt.richmond.brandongruber.revcards.io;

import uk.sch.greycourt.richmond.brandongruber.revcards.Project;

import java.io.File;
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

    void writeCards(File file, Set<Project> projects) throws IOException;
}
