package ru.karachev.sqlforschool.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DataBaseGeneratorTest {

    @InjectMocks
    private DataBaseGenerator dataBaseGenerator;
    
    @Mock
    private DBConnector connector;

    @Test
    void generateDataBaseShouldThrowExceptionWhenGettingWrongFilePathToScript() {
        String scriptFilePath = "";
        assertThatThrownBy(() -> dataBaseGenerator.generateDataBase(scriptFilePath))
                .isInstanceOf(RuntimeException.class);
    }
}
