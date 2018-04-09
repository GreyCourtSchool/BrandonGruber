package uk.sch.greycourt.richmond.brandongruber.revcards.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import uk.sch.greycourt.richmond.brandongruber.revcards.Project;
import uk.sch.greycourt.richmond.brandongruber.revcards.ProjectCsvHeaders;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Reader of project csv files.
 */
public class CsvFileReaderWriter implements RevisionCardReaderWriter {


    @Override
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


    @Override
    public List<Project> readProjects(String fileName) throws IOException {
        List<Project> result = new ArrayList<>();
        Reader reader = new FileReader(fileName);
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(ProjectCsvHeaders.NAME, ProjectCsvHeaders.DESCRIPTION)
                .withTrim());
        for (CSVRecord record : csvParser.getRecords()) {
            String name = record.get(ProjectCsvHeaders.NAME);
            String description = record.get(ProjectCsvHeaders.DESCRIPTION);
            result.add(new Project(name, description));
        }
        return result;
    }
}
