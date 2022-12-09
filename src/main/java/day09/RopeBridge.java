package day09;

import utils.MyUtilities;

import java.io.IOException;
import java.util.List;

public class RopeBridge {

    static class Position {
        private int x;
        private int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    private static int countOnesInGrid(int[][] grid) {
        int counter = 0;
        for (int[] ints : grid) {
            for (int j = 0; j < ints.length; j++) {
                if (ints[j] == 1) counter++;
            }
        }
        return counter;
    }

    private static Position moveTail(Position positionHead, Position positionTail, int[][] grid, boolean markOnGrid) {
        int posHeadX = positionHead.getX();
        int posHeadY = positionHead.getY();
        int posTailX = positionTail.getX();
        int posTailY = positionTail.getY();
        // move tail right/left
        if ((posHeadY == posTailY) && (Math.abs(posHeadX - posTailX) > 1)) {
            if (posHeadX > posTailX) posTailX++;
            else posTailX--;
        }
        // move tail up/down
        if ((posHeadX == posTailX) && (Math.abs(posHeadY - posTailY) > 1)) {
            if (posHeadY > posTailY) posTailY++;
            else posTailY--;
        }
        // move tail diagonally
        if ((Math.abs(posHeadX - posTailX) + Math.abs(posHeadY - posTailY)) > 2) {
            if (posHeadX > posTailX) posTailX++;
            else posTailX--;
            if (posHeadY > posTailY) posTailY++;
            else posTailY--;
        }
        if (markOnGrid) grid[posTailY][posTailX] = 1;
        positionTail.setX(posTailX);
        positionTail.setY(posTailY);
        return positionTail;
    }

    private static Position moveHead(Position positionHead, String command) {
        switch (command) {
            case "U" -> positionHead.setY(positionHead.getY() + 1);
            case "D" -> positionHead.setY(positionHead.getY() - 1);
            case "R" -> positionHead.setX(positionHead.getX() + 1);
            case "L" -> positionHead.setX(positionHead.getX() - 1);
        }
        return positionHead;
    }

    private static long getNumberOfPositionsThatTailVisitedAtLeastOnce(List<String> inputLines, int lengthOfTail, int sizeOfGridForRope) {
        int[][] grid = new int[sizeOfGridForRope][sizeOfGridForRope];
        int startX = sizeOfGridForRope / 2;
        int startY = sizeOfGridForRope / 2;
        Position positionHead = new Position(startX, startY);
        Position[] tailParts = new Position[lengthOfTail];
        for (int i = 0; i < lengthOfTail; i++) {
            tailParts[i] = new Position(startX, startY);
        }
        grid[startY][startX] = 1;
        for (String currentLine : inputLines) {
            String command = currentLine.substring(0, currentLine.indexOf(" "));
            int moves = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ")).strip());

            boolean markOnGrid;
            for (int j = 0; j < moves; j++) {
                moveHead(positionHead, command);
                Position firstParam;
                for (int k = 0; k < lengthOfTail; k++) {
                    if (k == 0) firstParam = positionHead;
                    else firstParam = tailParts[k - 1];
                    markOnGrid = (k == lengthOfTail - 1);
                    tailParts[k] = moveTail(firstParam, tailParts[k], grid, markOnGrid);
                }
            }
        }
        return countOnesInGrid(grid);
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day09/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);

        System.out.println("Part 1 = " + getNumberOfPositionsThatTailVisitedAtLeastOnce(inputLines, 1, 1000));
        System.out.println("Part 1 = " + getNumberOfPositionsThatTailVisitedAtLeastOnce(inputLines, 9, 1000));
    }
}
