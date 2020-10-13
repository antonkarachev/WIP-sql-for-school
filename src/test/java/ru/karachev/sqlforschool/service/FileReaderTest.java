package ru.karachev.sqlforschool.service;

import org.junit.jupiter.api.Test;
import ru.karachev.sqlforschool.exception.FileReaderException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileReaderTest {

    private FileReader fileReader = new FileReader();

    @Test
    void readFileShouldReturnListOfStringsWhenGettingFilePath() {
        String filePath = "src/test/resources/courses.txt";
        List<String> expected = new ArrayList<>();
        expected.add("biology_biological things");
        expected.add("history_historical stuff");
        List <String> actual = fileReader.readFile(filePath);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void readShouldThrowExceptionWhenGettingWrongFilePath(){
        String filePath = "";
        assertThatThrownBy(()-> fileReader.readFile(filePath))
                .isInstanceOf(FileReaderException.class);
    }
}