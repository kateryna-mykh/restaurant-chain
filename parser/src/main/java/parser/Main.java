package parser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import parser.dto.ItemDto;
import parser.dto.RestaurantDto;
import parser.dto.StatisticsReportDto;
import parser.exception.IncorrectParamsException;
import parser.service.FileWriter;
import parser.service.impl.ProcessJsonFile;
import parser.service.impl.StorageToItemDtoListConverter;
import parser.service.impl.XmlWriter;

public class Main {
    private static final String JSON_EXTENSION = "json";
    private static String readFolder;
    private static String writeFilePath;
    public static String atribute;

    public static void main(String[] args) {
        readFolder = args[0];
        atribute = args[1];

        checkInputArgsValid(readFolder, atribute);

        File folder = new File(readFolder);
        File[] jsonFiles = folder
                .listFiles(file -> !file.isDirectory() && file.getName().endsWith(JSON_EXTENSION));
       
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // future list uses to control that all threads finished, before report creation
        List<Future<?>> futuers = new ArrayList<>(jsonFiles.length);
        for (File currfile : jsonFiles) {
            futuers.add(executorService.submit(new ProcessJsonFile(currfile)));
        }
        executorService.shutdown();
        for (Future<?> future : futuers) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Exception has occure", e);
            }
        }
        List<ItemDto> reportList = new StorageToItemDtoListConverter().convert();
        FileWriter writer = new XmlWriter();

        writeFilePath = new StringBuilder()
                .append(readFolder)
                .append("/statistics/statistics_by_")
                .append(atribute)
                .append(".xml").toString();
        writer.jacksonAnnotation2Xml(writeFilePath, new StatisticsReportDto(reportList));
    }

    private static void checkInputArgsValid(String readFolder, String atributeValue) {
        if (readFolder == null || atributeValue == null || readFolder.isEmpty()
                || atributeValue.isEmpty()) {
            throw new IncorrectParamsException("Path to folder and atribute should be defined");
        }
        if (!Files.exists(Paths.get(readFolder))) {
            throw new IncorrectParamsException("Path to folder not exist");
        }
        try {
            RestaurantDto.class.getDeclaredField(atributeValue);
        } catch (NoSuchFieldException | SecurityException e) {
            throw new IncorrectParamsException("Atribute not exist " + atributeValue);
        }
    }
}
