package day04;

import utils.MyUtilities;

import java.io.IOException;
import java.util.List;

public class Day04 {

    private static long getResultForPartTwoRules(List<String> inputLines) {
        long result = 0;
        int size = inputLines.size();
        for (int i = 0; i < size; i++) {
            String line = inputLines.get(i);
            System.out.println(i + ". line = " + line);
            String[] pair = line.split(",");
            String[] first = pair[0].split("-");
            String[] second = pair[1].split("-");

            int firstStart = Integer.parseInt(first[0]);
            int firstEnd = Integer.parseInt(first[1]);
            int secondStart = Integer.parseInt(second[0]);
            int secondEnd = Integer.parseInt(second[1]);

            if((firstStart <= secondStart && firstEnd >= secondStart) ||
                    (secondStart <= firstStart && secondEnd >= firstStart)){
                System.out.println("HERE");
                result++;
            } else {
                System.out.println("HERE NOT");
            }
        }
        return result;
    }

    private static long getResultForPartOneRules(List<String> inputLines) {
        long result = 0;
        int size = inputLines.size();
        for (int i = 0; i < size; i++) {
            String line = inputLines.get(i);
           // System.out.println(i + ". line = " + line);
            String[] pair = line.split(",");
            String[] firstPair = pair[0].split("-");
            String[] secondPair = pair[1].split("-");

            int firstStart = Integer.parseInt(firstPair[0]);
            int firstEnd = Integer.parseInt(firstPair[1]);
            int secondStart = Integer.parseInt(secondPair[0]);
            int secondEnd = Integer.parseInt(secondPair[1]);

//            System.out.println("firstStart = " + firstStart);
//            System.out.println("firstEnd = " + firstEnd);
//            System.out.println("secondStart = " + secondStart);
//            System.out.println("secondEnd = " + secondEnd);

            if((firstStart <= secondStart && firstEnd >= secondEnd) ||
                    (firstStart >= secondStart && firstEnd <= secondEnd)){
                result++;
            }

        }
        return result;
    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day04/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);



        System.out.println("Part 1 = " + getResultForPartOneRules(inputLines));
        System.out.println("Part 2 = " + getResultForPartTwoRules(inputLines));
    }
}
