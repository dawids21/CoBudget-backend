package xyz.stasiak.cobudget.util;

import java.util.Optional;

public class FilenameUtil {
    public static Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
