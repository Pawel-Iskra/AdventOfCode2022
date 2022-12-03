package day03;

import utils.MyUtilities;

import java.io.IOException;
import java.util.List;

public class RucksackReorganization {

    private static long getResultForPartTwo(List<String> inputLines){
        System.out.println("PART TWO:");
        long result = 0;
        int size = inputLines.size();
        for (int i = 0; i < size; i= i+3) {
            String first = inputLines.get(i);
            String second = inputLines.get(i + 1);
            String third = inputLines.get(i + 2);

            int value = 0;
            for (int j = 0; j < first.length(); j++) {
                char currentChar = first.charAt(j);
                String currentCharStr = String.valueOf(currentChar);
                if(second.contains(currentCharStr) && third.contains(currentCharStr)){
                    System.out.println("currentCharStr = " + currentCharStr);
                    if(currentChar >= 'A' && currentChar <= 'Z'){
                        value = currentChar - 38;
                    } else {
                        value = currentChar - 96;
                    }
                    result += value;
                    break;
                }
            }
        }

        return result;
    }

    private static long getResult(List<String> inputLines) {
        long result = 0;

        int size = inputLines.size();
        for (int i = 0; i < size; i++) {
            String rucksack = inputLines.get(i);
            int length = rucksack.length();
            String firstCompartment = rucksack.substring(0, length / 2);
            String secondCompartment = rucksack.substring(length / 2);

//            System.out.println("firstCompartment = " + firstCompartment);
//            System.out.println("secondCompartment = " + secondCompartment);

            int sumOfRucksack = 0;
            for (int j = 0; j < firstCompartment.length(); j++) {
                char currentChar = firstCompartment.charAt(j);
                int value = 0;
                if(secondCompartment.contains(String.valueOf(currentChar))){
//                    System.out.println("currentChar = " + currentChar);
                    if(currentChar >= 'A' && currentChar <= 'Z'){
                        value = currentChar - 38;
                    } else {
                        value = currentChar - 96;
                    }
                    sumOfRucksack= value;
                }
            }
            System.out.println("sumOfRucksack = " + sumOfRucksack);

            result +=sumOfRucksack;
        } // end inputlinesa

        return result;
    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day03/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);
        int size = inputLines.size();
        for (int i = 0; i < size; i++) {
            String line = inputLines.get(i);
            System.out.println(i + ". " + line);
        }
        System.out.println();

        long resultt = getResult(inputLines);
        System.out.println("resultt = " + resultt);

        long resultPartTwo = getResultForPartTwo(inputLines);
        System.out.println("resultPartTwo = " + resultPartTwo);


    }
}
