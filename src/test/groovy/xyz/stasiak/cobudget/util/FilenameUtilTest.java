package xyz.stasiak.cobudget.util;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FilenameUtilTest {
    @Test
    void should_return_extension_of_file() {
        String filename = "file.txt";
        Optional<String> extension = FilenameUtil.getExtension(filename);
        assertThat(extension)
                .isNotEmpty()
                .hasValue("txt");
    }

    @Test
    void should_return_empty_optional_when_extension_does_not_exist() {
        String filename = "file";
        Optional<String> extension = FilenameUtil.getExtension(filename);
        assertThat(extension)
                .isEmpty();
    }
}