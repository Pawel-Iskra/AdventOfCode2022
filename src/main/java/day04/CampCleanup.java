package day04;

import utils.MyUtilities;

import java.io.IOException;
import java.util.List;

public class CampCleanup {

    private static void goOverBigListOfTheSectionAssignmentsForElfs(List<String> listOfSectionAssignmentsForElfs) {
        long counterOfPairsThatOneSectionIsWithinSecondOne = 0;
        long counterOfPairsThatOverlapsAtAll = 0;
        int size = listOfSectionAssignmentsForElfs.size();
        for (int i = 0; i < size; i++) {
            String line = listOfSectionAssignmentsForElfs.get(i);
            String[] pair = line.split(",");
            String[] first = pair[0].split("-");
            String[] second = pair[1].split("-");

            int firstStart = Integer.parseInt(first[0]);
            int firstEnd = Integer.parseInt(first[1]);
            int secondStart = Integer.parseInt(second[0]);
            int secondEnd = Integer.parseInt(second[1]);

            if ((firstStart <= secondStart && firstEnd >= secondEnd) ||
                    (firstStart >= secondStart && firstEnd <= secondEnd)) {
                counterOfPairsThatOneSectionIsWithinSecondOne++;
            }

            if ((firstStart <= secondStart && firstEnd >= secondStart) ||
                    (secondStart <= firstStart && secondEnd >= firstStart)) {
                counterOfPairsThatOverlapsAtAll++;
            }
        }
        System.out.println("Part 1 = " + counterOfPairsThatOneSectionIsWithinSecondOne);
        System.out.println("Part 2 = " + counterOfPairsThatOverlapsAtAll);
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day04/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);
        goOverBigListOfTheSectionAssignmentsForElfs(inputLines);
    }
}
