package uk.sch.greycourt.richmond.brandongruber.revcards.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final String PROJECTS_FILE_PATH = System.getProperty("user.home") + File.separator + "projects.csv";
    private static final String CARDS_FILE_PATH = System.getProperty("user.home") + File.separator + "cards.csv";

    private Logger logger = LogManager.getLogger(getClass());

    public CsvFileReaderWriter() {
        createFile(PROJECTS_FILE_PATH);
        createFile(CARDS_FILE_PATH);
    }

    private void createFile(final String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                logger.info("creating {}", filePath);
                file.createNewFile();
                logger.info("File {} created", filePath);
            }
        } catch (IOException e) {
            logger.error("Could not create new file {}", filePath, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeProjects(Collection<Project> projects) throws IOException {
        File outputFile = new File(PROJECTS_FILE_PATH);

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
    public List<Project> readProjects() throws IOException {
        List<Project> result = new ArrayList<>();
        Reader reader = new FileReader(PROJECTS_FILE_PATH);

        // reads the projects from a csv file, each project is on a different line and has a name and description
        CSVParser csvParser = new CSVParser(reader,
                CSVFormat.DEFAULT.withHeader(CsvFileHeaders.PROJECT_NAME, CsvFileHeaders.PROJECT_DESCRIPTION)
                        .withTrim());
        for (CSVRecord record : csvParser.getRecords()) {
            String name = record.get(CsvFileHeaders.PROJECT_NAME);
            String description = record.get(CsvFileHeaders.PROJECT_DESCRIPTION);
            result.add(new Project(name, description));
        }
        return result;
    }

    @Override
    public void writeCards(Set<Project> projects) throws IOException {
        File outputFile = new File(CARDS_FILE_PATH);
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
    public List<RevCard> getCardsFor(Project project) throws IOException {
        List<RevCard> result = new ArrayList<>();

        Reader reader = new FileReader(CARDS_FILE_PATH);
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withHeader(CsvFileHeaders.PROJECT_NAME, CsvFileHeaders.CARD_TITLE, CsvFileHeaders.CARD_CONTENT)
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
