package ru.karachev.sqlforschool.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Scanner;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ViewProviderImplTest {

    @InjectMocks
    private ViewProvider viewProvider;

    @Mock
    private Scanner scanner;

    @Test
    void readStringShouldReturnStringWhichIsInputFromConsole() {
        String expected = "hello";
        when(scanner.nextLine()).thenReturn("hello");
        String actual = viewProvider.readString();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void readIntShouldReturnIntegerWhichIsInputFromConsole() throws IOException {
        int expected = 254;
        when(scanner.nextInt()).thenReturn(254);
        int actual = viewProvider.readInt();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void printShouldPrintInConsoleMessageWhenGettingMessage() throws Exception {
        String input = "hello";
        String output = tapSystemOut(()->{
            viewProvider.print(input);
        });

        assertThat(input).isEqualTo(output.trim());
    }
    
    @Test
    void printErrorShouldPrintErrorMessage() throws Exception {
        String errorMessage = "No such command, please repeat";
        String output = tapSystemOut(()->{
            viewProvider.printError();
        });
        
        assertThat(errorMessage).isEqualTo(output.trim());
    }
}
