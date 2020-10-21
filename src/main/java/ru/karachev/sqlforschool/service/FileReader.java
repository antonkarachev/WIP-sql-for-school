package ru.karachev.sqlforschool.service;

import org.apache.log4j.Logger;
import ru.karachev.sqlforschool.exception.FileReaderException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReader {

    private static final Logger LOGGER = Logger.getLogger(FileReader.class);
    
    private static final String COMPLETE_MESSAGE = " complete";
    private static final String FAILED_MESSAGE = " failed";
    private static final String READING_MESSAGE = "Reading ";

    public List<String> readFile(String filePath) {

        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            LOGGER.info(READING_MESSAGE + filePath + COMPLETE_MESSAGE);
            return stream.collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.error(READING_MESSAGE + filePath + FAILED_MESSAGE, e);
            throw new FileReaderException(READING_MESSAGE + filePath + FAILED_MESSAGE, e);
        }

    }
}
