package uk.sch.greycourt.richmond.brandongruber.revcards.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import uk.sch.greycourt.richmond.brandongruber.revcards.Project;
import uk.sch.greycourt.richmond.brandongruber.revcards.RevCard;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Reader of project csv files.
 */
public class CsvFileReaderWriter implements RevisionCardReaderWriter {


    @Override
    public void writeProjects(File outputFile, Collection<Project> projects) throws IOException {

        // writes the projects to the specified file in CSV (comma separated values) format, with each project on a new line
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(outputFile), CSVFormat.EXCEL)) {
            // Write the projects to the csv file.
            for (Project project : projects) {
                // project has a name and a description
                List<String> rowDataList = new ArrayList<>();
                rowDataList.add(project.getName());
                rowDataList.add(project.getDescription());
                csvPrinter.printRecord(rowDataList);
            }
        }
    }


    @Override
    public List<Project> readProjects(String fileName) throws IOException {
        List<Project> result = new ArrayList<>();
        Reader reader = new FileReader(fileName);

        // reads the projects from a csv file, each project is on a different line and has a name and description
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(CsvFileHeaders.PROJECT_NAME, CsvFileHeaders.PROJECT_DESCRIPTION)
                .withTrim());
        for (CSVRecord record : csvParser.getRecords()) {
            String name = record.get(CsvFileHeaders.PROJECT_NAME);
            String description = record.get(CsvFileHeaders.PROJECT_DESCRIPTION);
            result.add(new Project(name, description));
        }
        return result;
    }

    @Override
    public void writeCards(File outputFile, Set<Project> projects) throws IOException {
        outputFile.createNewFile();

        // write the revision cards to a seperate CSV file to the projects, each card is on a different line, and the first field is the unique name of the project
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(outputFile), CSVFormat.EXCEL)) {
            for (Project project : projects) {
                for (RevCard revCard : project.getCardList()) {
                    List<String> rowDataList = new ArrayList<>();
                    rowDataList.add(project.getName());
                    rowDataList.add(revCard.getTitle());
                    rowDataList.add(revCard.getContent());
                    csvPrinter.printRecord(rowDataList);
                }
            }
        }
    }

    @Override
    public List<RevCard> getCardsFor(String fileName, Project project) throws IOException {
        List<RevCard> result = new ArrayList<>();
        Reader reader = new FileReader(fileName);
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(CsvFileHeaders.PROJECT_NAME, CsvFileHeaders.CARD_TITLE, CsvFileHeaders.CARD_CONTENT)
                .withTrim());
        for (CSVRecord record : csvParser.getRecords()) {
            String projectName = record.get(CsvFileHeaders.PROJECT_NAME);

            // we only want to return revision cards for     the specified project
            if (projectName.equals(project.getName())) {
                String title = record.get(CsvFileHeaders.CARD_TITLE);
                String description = record.get(CsvFileHeaders.CARD_CONTENT);
                result.add(new RevCard(title, description));
            }
        }
        return result;
    }

}
