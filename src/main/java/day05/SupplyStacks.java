package day05;

import utils.MyUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SupplyStacks {

    private static void workWithCraneOnCrateStacks(boolean multipleCratesAtOnce, List<String> linesWithPlannedMoves, List<String>[] crateStacks) {
        for (int i = 0; i < linesWithPlannedMoves.size(); i++) {
            String currentMove = linesWithPlannedMoves.get(i);
            int howManyCratesToTake = Integer.parseInt(
                    currentMove.substring(0, currentMove.indexOf("from")).replaceAll("[^0-9.]", ""));
            int from = Integer.parseInt(
                    currentMove.substring(currentMove.indexOf("from"), currentMove.indexOf("to")).replaceAll("[^0-9.]", ""));
            int to = Integer.parseInt(
                    currentMove.substring(currentMove.indexOf("to")).replaceAll("[^0-9.]", ""));

            List<String> currentElements = new ArrayList<>(
                    crateStacks[from - 1].subList(crateStacks[from - 1].size() - howManyCratesToTake, crateStacks[from - 1].size()));
            List<String> newList = crateStacks[from - 1].subList(0, crateStacks[from - 1].size() - howManyCratesToTake);
            crateStacks[from - 1] = new ArrayList<>(newList);
            if (!multipleCratesAtOnce) {
                Collections.reverse(currentElements);
            }
            crateStacks[to - 1].addAll(currentElements);
        }
    }

    private static List<String>[] getCrateStacksFromLinesWithCrateStacks(List<String> linesWithCrateStacks) {
        String[] lastLineWithCrateStacks = linesWithCrateStacks.get(linesWithCrateStacks.size() - 1).split(" ");
        int numberOfCrateStacks = Integer.parseInt(lastLineWithCrateStacks[lastLineWithCrateStacks.length - 1].strip());
        List<String>[] crateStacks = new ArrayList[numberOfCrateStacks];
        for (int i = 0; i < numberOfCrateStacks; i++) {
            crateStacks[i] = new ArrayList<>();
        }
        for (int i = 0; i < linesWithCrateStacks.size() - 1; i++) {
            String stack = linesWithCrateStacks.get(i);
            int stackLineLength = stack.length();
            int numberOfCrateStacksInLine = (stackLineLength + 1) / 4;
            int oneStackLength = (stackLineLength / numberOfCrateStacksInLine) + 1;

            for (int j = 0; j < numberOfCrateStacksInLine; j++) {
                String currentStack = stack.substring(j * oneStackLength, (j + 1) * oneStackLength - 1);
                if (!currentStack.isBlank()) {
                    crateStacks[j].add(currentStack.substring(currentStack.indexOf("[") + 1, currentStack.indexOf("]")));
                }
            }
        }
        Arrays.stream(crateStacks).forEach(Collections::reverse);
        return crateStacks;
    }

    private static String craneWorkWithPlannedMoves(List<String> inputLines, boolean multipleCratesAtOnce) {
        int indexOfBlankLine = 0;
        for (String line : inputLines) {
            if (line.isBlank()) break;
            indexOfBlankLine++;
        }
        List<String> linesWithCrateStacks = inputLines.subList(0, indexOfBlankLine);
        List<String> linesWithPlannedMoves = inputLines.subList(indexOfBlankLine + 1, inputLines.size());

        List<String>[] crateStacks = getCrateStacksFromLinesWithCrateStacks(linesWithCrateStacks);
        workWithCraneOnCrateStacks(multipleCratesAtOnce, linesWithPlannedMoves, crateStacks);

        List<String> topCratesFromStacks = Arrays.stream(crateStacks).map(stack -> stack.get(stack.size() - 1)).toList();
        StringBuilder strb = new StringBuilder();
        for (String topCrateSymbol : topCratesFromStacks) {
            strb.append(topCrateSymbol);
        }
        return strb.toString();
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day05/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);

        System.out.println("Part 1 = " + craneWorkWithPlannedMoves(inputLines, false));
        System.out.println("Part 2 = " + craneWorkWithPlannedMoves(inputLines, true));
    }
}
