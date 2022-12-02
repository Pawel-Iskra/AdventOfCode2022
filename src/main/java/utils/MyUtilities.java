package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public final class MyUtilities {

    private MyUtilities() {
    }

    public static List<String> getInputLines(String pathToFile) throws IOException {
        try (FileReader fileReader = new FileReader(pathToFile);
             BufferedReader br = new BufferedReader(fileReader)) {
            return br.lines().collect(Collectors.toList());
        }
    }
}
