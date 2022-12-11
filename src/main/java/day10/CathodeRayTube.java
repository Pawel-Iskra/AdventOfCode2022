package day10;

import utils.MyUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CathodeRayTube {

    static class Machine {
        private List<String> commands;
        private int numOfClockTicks;
        private int valueX;
        private char[][] crtImage;
        private List<Integer> interestingCycles;
        private int currentCommandIndex;

        public Machine(List<String> commands, List<Integer> interestingCycles, int numOfClockTicks, int crtRows, int crtColumns) {
            this.commands = commands;
            this.numOfClockTicks = numOfClockTicks;
            this.valueX = 1;
            this.crtImage = new char[crtRows][crtColumns];
            this.interestingCycles = interestingCycles;
            this.currentCommandIndex = 0;
        }

        private void drawOnePixel(char[][] crtImage, int currentCycle, int valueX) {
            int crtColumns = crtImage[0].length;
            int spriteStart = valueX - 1;
            int spriteEnd = valueX + 1;
            int row = (currentCycle - 1) / crtColumns;
            int column = currentCycle - crtColumns * ((currentCycle - 1) / crtColumns) - 1;
            if (column >= spriteStart && column <= spriteEnd) {
                crtImage[row][column] = '#';
            } else {
                crtImage[row][column] = '.';
            }
        }

        private int executeCommand(int cyclesRequiredForCommandCounter) {
            String[] commandLine = commands.get(currentCommandIndex++).split(" ");
            String command = commandLine[0];
            int commandValue = 0;
            if (commandLine.length > 1) commandValue = Integer.parseInt(commandLine[1]);
            switch (command) {
                case "noop" -> cyclesRequiredForCommandCounter = 1;
                case "addx" -> {
                    valueX += commandValue;
                    cyclesRequiredForCommandCounter = 2;
                }
            }
            return cyclesRequiredForCommandCounter;
        }

        public void runMachineForNumOfCyclesOrListOfCommands() {
            int currentCycle = 0;
            int cyclesRequiredForCommandCounter = 1;
            List<Integer> signalStrengths = new ArrayList<>();
            int interestingCycleIndex = 0;
            int currentInterestingCycle = interestingCycles.get(interestingCycleIndex++);

            String[] commandLine = commands.get(currentCommandIndex).split(" ");
            String command = commandLine[0];
            if ("noop".equals(command)) cyclesRequiredForCommandCounter = 1;
            else if ("addx".equals(command)) cyclesRequiredForCommandCounter = 2;

            while (++currentCycle <= numOfClockTicks) {
                drawOnePixel(crtImage, currentCycle, valueX);

                if (currentCycle == currentInterestingCycle) {
                    signalStrengths.add(valueX * currentInterestingCycle);
                    if (interestingCycleIndex < interestingCycles.size()) {
                        currentInterestingCycle = interestingCycles.get(interestingCycleIndex++);
                    }
                }

                cyclesRequiredForCommandCounter--;
                if (cyclesRequiredForCommandCounter == 0) {
                    cyclesRequiredForCommandCounter = executeCommand(cyclesRequiredForCommandCounter);
                }
            }

            System.out.println("Part 1 = " + signalStrengths.stream().mapToInt(s -> s).sum());
            System.out.println("Part 2 = CRT image:");
            for (char[] chars : crtImage) {
                for (char oneChar : chars) {
                    System.out.print(oneChar + " ");
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day10/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);
        List<Integer> interestingCycles = List.of(20, 60, 100, 140, 180, 220);

        Machine machine = new Machine(inputLines, interestingCycles, 240, 6, 40);
        machine.runMachineForNumOfCyclesOrListOfCommands();
    }
}
