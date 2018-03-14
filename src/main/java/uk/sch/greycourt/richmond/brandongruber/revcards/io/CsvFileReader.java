package uk.sch.greycourt.richmond.brandongruber.revcards.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import uk.sch.greycourt.richmond.brandongruber.revcards.Project;
import uk.sch.greycourt.richmond.brandongruber.revcards.ProjectCsvHeaders;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Reader of project csv files.
 */
public class CsvFileReader {

    /**
     * Provides access to projects in the project csv file.
     *
     * @return A {@link List} of {@link Project} instances or an empty list if there are no projects.
     * @throws IOException If the project csv file could not be found.
     * @param fileName
     */
    public List<Project> readProjects(String fileName) throws IOException {
        List<Project> result = new ArrayList<>();
        Reader in = new FileReader(fileName);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
        for (CSVRecord record : records) {
            String name = record.get(ProjectCsvHeaders.NAME);
            String description = record.get(ProjectCsvHeaders.DESCRIPTION);
            result.add(new Project( name, description));
        }
        return result;
    }
}
