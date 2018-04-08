package uk.sch.greycourt.richmond.brandongruber.revcards.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import uk.sch.greycourt.richmond.brandongruber.revcards.Project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Writer of Project csv files.
 */
public class CsvFileWriter {

    /**
     * Writes projects to file.
     *
     * @param outputFile The {@link File} to write the projects to.
     * @param projects   The {@link List} of {@link Project} instances to write to file.
     * @throws IOException If the projects could not be written to file.
     */
    public void writeProjects(File outputFile, Collection<Project> projects) throws IOException {
        FileWriter fileWriter = new FileWriter(outputFile);
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.EXCEL);

        // Write the projects to the csv file.
        for (Project project : projects) {
            List<String> rowDataList = new ArrayList<>();
            rowDataList.add(project.getName());
            rowDataList.add(project.getDescription());
            csvPrinter.printRecord(rowDataList);
        }

        fileWriter.flush();
        fileWriter.close();
        csvPrinter.close();

    }
}
