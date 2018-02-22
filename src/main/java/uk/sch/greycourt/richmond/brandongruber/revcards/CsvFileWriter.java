package uk.sch.greycourt.richmond.brandongruber.revcards;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvFileWriter {

    public void writeProjects(File outputFile, List<Project> projects) throws IOException {
        FileWriter fileWriter = new FileWriter(outputFile);
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.EXCEL);

        csvPrinter.printRecord(ProjectDefinition.ID, ProjectDefinition.NAME, ProjectDefinition.DESCRIPTION);
        for (Project project : projects) {
            List<String> rowDataList = new ArrayList<>();
            rowDataList.add(project.getId());
            rowDataList.add(project.getName());
            rowDataList.add(project.getDescription());
            csvPrinter.printRecord(rowDataList);
        }

        fileWriter.flush();
        fileWriter.close();
        csvPrinter.close();

    }
}
