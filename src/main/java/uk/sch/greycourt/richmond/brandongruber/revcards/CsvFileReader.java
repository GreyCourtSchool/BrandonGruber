package uk.sch.greycourt.richmond.brandongruber.revcards;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CsvFileReader {

    public List<Project> readProjects() throws IOException {
        List<Project> result = new ArrayList<>();
        Reader in = new FileReader("path/to/file.csv");
        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
        for (CSVRecord record : records) {
            String id = record.get(ProjectDefinition.ID);
            String name = record.get(ProjectDefinition.NAME);
            String description = record.get(ProjectDefinition.DESCRIPTION);
            result.add(new Project(id, name, description));
        }
        return result;
    }
}
