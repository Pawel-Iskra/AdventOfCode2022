package day11;

import utils.MyUtilities;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MonkeyInTheMiddle {

    interface Operation {
        BigInteger calcNewItemValue(BigInteger oldItemValue, BigInteger secondOperand);
    }

    static class AddOperation implements Operation {
        public BigInteger calcNewItemValue(BigInteger oldItemValue, BigInteger secondOperand) {
            return oldItemValue.add(secondOperand);
        }
    }

    static class MultiplyOperation implements Operation {
        public BigInteger calcNewItemValue(BigInteger oldItemValue, BigInteger secondOperand) {
            return oldItemValue.multiply(secondOperand);
        }
    }

    static class Monkey {
        private int index;
        private List<BigInteger> items;
        private int inspectedItemsCounter;
        private String operationSymbol;
        private String secondOperandStr;
        private int testDivisibleBy;
        private int monkeyToThrowToIfTestTrue;
        private int monkeyToThrowToIfTestFalse;
        private Operation operation;

        public Monkey(int index, List<BigInteger> items, String operationSymbol, String secondOperandStr,
                      int testDivisibleBy, int monkeyToThrowToIfTestTrue, int monkeyToThrowToIfTestFalse) {
            this.index = index;
            this.items = items;
            this.operationSymbol = operationSymbol;
            this.secondOperandStr = secondOperandStr;
            this.inspectedItemsCounter = 0;
            this.testDivisibleBy = testDivisibleBy;
            this.monkeyToThrowToIfTestTrue = monkeyToThrowToIfTestTrue;
            this.monkeyToThrowToIfTestFalse = monkeyToThrowToIfTestFalse;
            this.operation = switch (this.operationSymbol) {
                case "+" -> new AddOperation();
                case "*" -> new MultiplyOperation();
                default -> throw new UnsupportedOperationException();
            };
        }

        public List<BigInteger> getItems() {
            return items;
        }

        public int getInspectedItemsCounter() {
            return inspectedItemsCounter;
        }

        public int getTestDivisibleBy() {
            return testDivisibleBy;
        }

        public int getMonkeyToThrowToIfTestTrue() {
            return monkeyToThrowToIfTestTrue;
        }

        public int getMonkeyToThrowToIfTestFalse() {
            return monkeyToThrowToIfTestFalse;
        }

        public void setItems(List<BigInteger> items) {
            this.items = items;
        }

        public void addItem(BigInteger item) {
            this.items.add(item);
        }

        public void addToCounter(int value) {
            inspectedItemsCounter += value;
        }

        public BigInteger calcNewItemValue(BigInteger oldValue) {
            return operation.calcNewItemValue(oldValue, getSecondOperand(oldValue));
        }

        private BigInteger getSecondOperand(BigInteger oldValue) {
            if (secondOperandStr.contains("old")) return new BigInteger(oldValue.toString());
            else return new BigInteger(secondOperandStr.strip());
        }
    }

    private static void runOneRound(Monkey[] monkeys, long commonDiv, boolean divideByTree) {
        for (Monkey currentMonkey : monkeys) {
            List<BigInteger> itemsValues = currentMonkey.getItems();
            currentMonkey.setItems(new ArrayList<>());
            currentMonkey.addToCounter(itemsValues.size());
            for (BigInteger itemValue : itemsValues) {
                BigInteger itemValueAfterOperation = currentMonkey.calcNewItemValue(itemValue);
                if (divideByTree) itemValueAfterOperation = itemValueAfterOperation.divide(new BigInteger("3"));
                boolean testIfDivisible =
                        itemValueAfterOperation.remainder(BigInteger.valueOf(currentMonkey.getTestDivisibleBy())).intValue() == 0;
                BigInteger newItemValueToThrow = itemValueAfterOperation.remainder(new BigInteger(String.valueOf(commonDiv)));
                if (testIfDivisible) monkeys[currentMonkey.getMonkeyToThrowToIfTestTrue()].addItem(newItemValueToThrow);
                else monkeys[currentMonkey.getMonkeyToThrowToIfTestFalse()].addItem(newItemValueToThrow);
            }
        }
    }

    private static Monkey[] createMonkeysFromInputData(List<String> inputLines) {
        int size = inputLines.size();
        int numOfMonkeys = (size + 1) / 7;
        Monkey[] monkeys = new Monkey[numOfMonkeys];
        for (int i = 0; i < numOfMonkeys; i++) {
            List<String> currentMonkeyData = inputLines.subList(i * 7, (i + 1) * 7 - 1);
            String lineWithMonkeyNum = currentMonkeyData.get(0);
            int monkeyNum = Integer.parseInt(lineWithMonkeyNum.substring(lineWithMonkeyNum.indexOf(" ") + 1, lineWithMonkeyNum.indexOf(":")));
            List<BigInteger> monkeyItems = new ArrayList<>();
            String[] items = currentMonkeyData.get(1).split(",");
            for (String item : items) {
                monkeyItems.add(BigInteger.valueOf(Long.parseLong(item.replaceAll("[^0-9]", ""))));
            }
            String lineWithOperation = currentMonkeyData.get(2);
            int indexOfOld = lineWithOperation.indexOf("old");
            String operationSymbol = lineWithOperation.substring(indexOfOld + 3, indexOfOld + 5).strip();
            String secondOperandStr = lineWithOperation.substring(indexOfOld + 5).strip();

            int testDivisibleBy = Integer.parseInt(
                    currentMonkeyData.get(3).substring(currentMonkeyData.get(3).indexOf("by") + 2).strip());
            int monkeyToThrowToIfTestTrue = Integer.parseInt(
                    currentMonkeyData.get(4).substring(currentMonkeyData.get(4).indexOf("monkey") + 6).strip());
            int monkeyToThrowToIfTestFalse = Integer.parseInt(
                    currentMonkeyData.get(5).substring(currentMonkeyData.get(5).indexOf("monkey") + 6).strip());
            monkeys[i] = new Monkey(monkeyNum, monkeyItems, operationSymbol, secondOperandStr,
                    testDivisibleBy, monkeyToThrowToIfTestTrue, monkeyToThrowToIfTestFalse);
        }
        return monkeys;
    }

    private static String goNumberOfMonkeyRounds(List<String> inputLines, int rounds, boolean divideByTree) {
        Monkey[] monkeys = createMonkeysFromInputData(inputLines);
        long commonDiv = Arrays.stream(monkeys)
                .map(Monkey::getTestDivisibleBy)
                .reduce(1, (a, b) -> a * b);
        for (int i = 0; i < rounds; i++) {
            runOneRound(monkeys, commonDiv, divideByTree);
        }
        List<Integer> inspectedItemsByMonkeys = new ArrayList<>();
        for (Monkey monkey : monkeys) {
            inspectedItemsByMonkeys.add(monkey.getInspectedItemsCounter());
        }
        inspectedItemsByMonkeys.sort(Collections.reverseOrder());
        BigInteger result = new BigInteger(String.valueOf(inspectedItemsByMonkeys.get(0)))
                .multiply(new BigInteger(String.valueOf(inspectedItemsByMonkeys.get(1))));
        return result.toString();
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day11/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);

        System.out.println("Part 1 = " + goNumberOfMonkeyRounds(inputLines, 20, true));
        System.out.println("Part 2 = " + goNumberOfMonkeyRounds(inputLines, 10000, false));
    }
}
