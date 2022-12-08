package day08;

import utils.MyUtilities;

import java.io.IOException;
import java.util.List;

public class TreetopTreeHouse {


    private static int getScenicScoreForCurrentTree(int row, int column, int[][] treeGrid) {
        int numOfTreesVisibleUp = 0;
        int numOfTreesVisibleDown = 0;
        int numOfTreesVisibleRight = 0;
        int numOfTreesVisibleLeft = 0;
        int rows = treeGrid.length;
        int columns = treeGrid[0].length;
        int treeHeight = treeGrid[row][column];
        for (int i = row - 1; i >= 0; i--) { // up
            numOfTreesVisibleUp++;
            if (treeHeight <= treeGrid[i][column]) break;
        }
        for (int i = row + 1; i < rows; i++) { // down
            numOfTreesVisibleDown++;
            if (treeHeight <= treeGrid[i][column]) break;
        }
        for (int i = column + 1; i < columns; i++) { // right
            numOfTreesVisibleRight++;
            if (treeHeight <= treeGrid[row][i]) break;
        }
        for (int i = column - 1; i >= 0; i--) { // left
            numOfTreesVisibleLeft++;
            if (treeHeight <= treeGrid[row][i]) break;
        }
        return numOfTreesVisibleUp * numOfTreesVisibleDown * numOfTreesVisibleRight * numOfTreesVisibleLeft;
    }

    private static int calculateHighestScenicScore(List<String> treeMap) {
        int highestScenicScore = 0;
        int[][] treeGrid = getTreeGrid(treeMap);
        int rows = treeGrid.length - 1;
        int columns = treeGrid[0].length - 1;
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < columns; j++) {
                int currentScenicScore = getScenicScoreForCurrentTree(i, j, treeGrid);
                if (currentScenicScore > highestScenicScore) {
                    highestScenicScore = currentScenicScore;
                }
            }
        }
        return highestScenicScore;
    }

    private static boolean checkIfTreeIsVisibleFromOutside(int row, int column, int[][] treeGrid) {
        int treeHeight = treeGrid[row][column];
        int rows = treeGrid.length;
        int columns = treeGrid[0].length;
        boolean isTreeVisibleFromOutside = true;
        for (int i = row - 1; i >= 0; i--) { // up
            if (treeHeight <= treeGrid[i][column]) {
                isTreeVisibleFromOutside = false;
                break;
            }
        }
        if (isTreeVisibleFromOutside) return true;
        isTreeVisibleFromOutside = true;
        for (int i = row + 1; i < rows; i++) { // down
            if (treeHeight <= treeGrid[i][column]) {
                isTreeVisibleFromOutside = false;
                break;
            }
        }
        if (isTreeVisibleFromOutside) return true;
        isTreeVisibleFromOutside = true;
        for (int i = column + 1; i < columns; i++) { // right
            if (treeHeight <= treeGrid[row][i]) {
                isTreeVisibleFromOutside = false;
                break;
            }
        }
        if (isTreeVisibleFromOutside) return true;
        isTreeVisibleFromOutside = true;
        for (int i = column - 1; i >= 0; i--) { // left
            if (treeHeight <= treeGrid[row][i]) {
                isTreeVisibleFromOutside = false;
                break;
            }
        }
        return isTreeVisibleFromOutside;
    }

    private static int[][] getTreeGrid(List<String> treeMap) {
        int rows = treeMap.size();
        int columns = treeMap.get(0).length();
        int[][] treeGrid = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            String currentLine = treeMap.get(i);
            for (int j = 0; j < columns; j++) {
                treeGrid[i][j] = currentLine.charAt(j) - 48;
            }
        }
        return treeGrid;
    }

    private static int calculateVisibleTrees(List<String> treeMap) {
        int counter = 2 * (treeMap.size() + treeMap.get(0).length() - 2);
        int[][] treeGrid = getTreeGrid(treeMap);
        int rows = treeGrid.length - 1;
        int columns = treeGrid[0].length - 1;
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < columns; j++) {
                if (checkIfTreeIsVisibleFromOutside(i, j, treeGrid)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day08/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);

        System.out.println("Part 1 = " + calculateVisibleTrees(inputLines));
        System.out.println("Part 2 = " + calculateHighestScenicScore(inputLines));
    }
}
