package day01;

import utils.MyUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalorieCounting {

    private static List<Integer> getEachElfCalories(List<String> caloriesList) {
        List<Integer> elfCalories = new ArrayList<>();
        int currentSum = 0;
        for (String line : caloriesList) {
            if (line.isBlank()) {
                elfCalories.add(currentSum);
                currentSum = 0;
            } else {
                currentSum += Integer.parseInt(line);
            }
        }
        return elfCalories;
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day01/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);

        List<Integer> elfCalories = getEachElfCalories(inputLines);
        elfCalories.sort(Collections.reverseOrder());

        System.out.println("Part 1 = " + elfCalories.get(0));
        System.out.println("Part 2 = " + (elfCalories.get(0) + elfCalories.get(1) + elfCalories.get(2)));
    }
}
