package day03;

import utils.MyUtilities;

import java.io.IOException;
import java.util.List;

public class RucksackReorganization {

    private static long getResultForPartTwoRules(List<String> inputLines) {
        long result = 0;
        int size = inputLines.size();
        for (int i = 0; i < size; i = i + 3) {
            String firstRucksack = inputLines.get(i);
            String secondRucksack = inputLines.get(i + 1);
            String thirdRucksack = inputLines.get(i + 2);

            int itemPriority = 0;
            for (int j = 0; j < firstRucksack.length(); j++) {
                char currentChar = firstRucksack.charAt(j);
                String currentCharStr = String.valueOf(currentChar);
                if (secondRucksack.contains(currentCharStr) && thirdRucksack.contains(currentCharStr)) {
                    if (currentChar >= 'A' && currentChar <= 'Z') {
                        itemPriority = currentChar - 38;
                    } else {
                        itemPriority = currentChar - 96;
                    }
                    break;
                }
            }
            result += itemPriority;
        }
        return result;
    }

    private static long getResultForPartOneRules(List<String> inputLines) {
        long result = 0;
        for (String rucksack : inputLines) {
            int length = rucksack.length();
            String firstCompartment = rucksack.substring(0, length / 2);
            String secondCompartment = rucksack.substring(length / 2);

            int itemPriority = 0;
            for (int i = 0; i < firstCompartment.length(); i++) {
                char currentChar = firstCompartment.charAt(i);
                if (secondCompartment.contains(String.valueOf(currentChar))) {
                    if (currentChar >= 'A' && currentChar <= 'Z') {
                        itemPriority = currentChar - 38;
                    } else {
                        itemPriority = currentChar - 96;
                    }
                    break;
                }
            }
            result += itemPriority;
        }
        return result;
    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day03/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);

        System.out.println("Part 1 = " + getResultForPartOneRules(inputLines));
        System.out.println("Part 2 = " + getResultForPartTwoRules(inputLines));
    }
}
