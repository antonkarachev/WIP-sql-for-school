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

    public List<String> readFile(String filePath) {

        try (Stream<String> data = Files.lines(Paths.get(filePath))) {
            LOGGER.info("Reading " + filePath + " complete");
            return data.collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.error("Reading " + filePath + " failed", e);
            throw new FileReaderException("Reading " + filePath + " failed", e);
        }

    }
}
