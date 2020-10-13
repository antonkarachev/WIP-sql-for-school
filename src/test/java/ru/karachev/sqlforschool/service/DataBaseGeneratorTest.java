package ru.karachev.sqlforschool.service;

import org.junit.jupiter.api.Test;
import ru.karachev.sqlforschool.context.ContextInjector;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DataBaseGeneratorTest {

    ContextInjector contextInjector = new ContextInjector();
    private DataBaseGenerator dataBaseGenerator = new DataBaseGenerator(contextInjector.getConnector());

    @Test
    void generateDataBaseShouldThrowExceptionWhenGettingWrongFilePathToScript() {
        String scriptFilePath = "";
        assertThatThrownBy(() -> dataBaseGenerator.generateDataBase(scriptFilePath))
                .isInstanceOf(RuntimeException.class);
    }
}