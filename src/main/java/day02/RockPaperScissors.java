package day02;

import utils.MyUtilities;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RockPaperScissors {

    enum Shape {
        ROCK("Rock", "X", "A", 1),
        PAPER("Paper", "Y", "B", 2),
        SCISSORS("Scissors", "Z", "C", 3);
        private String name;
        private String yourShapeCode;
        private String opponentShapeCode;
        private int pointValue;

        Shape(String name, String yourShapeCode, String opponentShapeCode, int pointValue) {
            this.name = name;
            this.yourShapeCode = yourShapeCode;
            this.opponentShapeCode = opponentShapeCode;
            this.pointValue = pointValue;
        }

        int getPointValue() {
            return pointValue;
        }

        String getYourShapeCode() {
            return yourShapeCode;
        }

        static Shape getShapeToGetSpecificResult(RoundResult roundResult, String opponentShapeCode) {
            Shape opponentShape = getShapeBasedOpponentCode(opponentShapeCode);
            switch (roundResult) {
                case WIN:
                    if (ROCK.equals(opponentShape)) return PAPER;
                    if (PAPER.equals(opponentShape)) return SCISSORS;
                    if (SCISSORS.equals(opponentShape)) return ROCK;
                case DRAW:
                    if (ROCK.equals(opponentShape)) return ROCK;
                    if (PAPER.equals(opponentShape)) return PAPER;
                    if (SCISSORS.equals(opponentShape)) return SCISSORS;
                case LOSE:
                    if (ROCK.equals(opponentShape)) return SCISSORS;
                    if (PAPER.equals(opponentShape)) return ROCK;
                    if (SCISSORS.equals(opponentShape)) return PAPER;
            }
            throw new IllegalArgumentException();
        }

        static Shape getShapeBasedYourCode(String yourShapeCode) {
            return Arrays.stream(values()).filter(shape -> shape.yourShapeCode.equals(yourShapeCode))
                    .findFirst().orElseThrow(IllegalArgumentException::new);
        }

        static Shape getShapeBasedOpponentCode(String opponentShapeCode) {
            return Arrays.stream(values()).filter(shape -> shape.opponentShapeCode.equals(opponentShapeCode))
                    .findFirst().orElseThrow(IllegalArgumentException::new);
        }

        static int getValueFromStringShapeCode(String shapeCode) {
            return getShapeBasedYourCode(shapeCode).getPointValue();
        }

        static RoundResult compareShapesAndGetResult(String yourShapeCode, String opponentShapeCode) {
            Shape opponentShape = getShapeBasedOpponentCode(opponentShapeCode);
            switch (getShapeBasedYourCode(yourShapeCode)) {
                case ROCK:
                    if (ROCK.equals(opponentShape)) return RoundResult.DRAW;
                    if (PAPER.equals(opponentShape)) return RoundResult.LOSE;
                    if (SCISSORS.equals(opponentShape)) return RoundResult.WIN;
                case PAPER:
                    if (ROCK.equals(opponentShape)) return RoundResult.WIN;
                    if (PAPER.equals(opponentShape)) return RoundResult.DRAW;
                    if (SCISSORS.equals(opponentShape)) return RoundResult.LOSE;
                case SCISSORS:
                    if (ROCK.equals(opponentShape)) return RoundResult.LOSE;
                    if (PAPER.equals(opponentShape)) return RoundResult.WIN;
                    if (SCISSORS.equals(opponentShape)) return RoundResult.DRAW;
            }
            throw new IllegalArgumentException();
        }

    }

    enum RoundResult {
        WIN(6, "Z"),
        DRAW(3, "Y"),
        LOSE(0, "X");
        private int pointValue;
        private String symbol;

        RoundResult(int pointValue, String symbol) {
            this.pointValue = pointValue;
            this.symbol = symbol;
        }

        static RoundResult getRoundResultFromSymbol(String roundResultSymbol) {
            return Arrays.stream(values()).filter(result -> result.symbol.equals(roundResultSymbol))
                    .findFirst().orElseThrow(IllegalArgumentException::new);
        }

        int getPointValue() {
            return pointValue;
        }

        static int getPointValueOfGameRoundResult(RoundResult roundResult) {
            return roundResult.getPointValue();
        }
    }

    private static long calculatePointsForRound(String yourShapeCode, String opponentShapeCode) {
        int shapePoints = Shape.getValueFromStringShapeCode(yourShapeCode);
        int roundResultPoints = RoundResult.getPointValueOfGameRoundResult(
                Shape.compareShapesAndGetResult(yourShapeCode, opponentShapeCode));
        return shapePoints + roundResultPoints;
    }

    private static long getResultForSecondPartRules(List<String> strategyGuide) {
        long gameScore = 0;
        for (String symbolsForRound : strategyGuide) {
            String[] shapeCodes = symbolsForRound.split(" ");
            String opponentShapeCode = shapeCodes[0];
            String roundResultSymbol = shapeCodes[1];
            RoundResult roundResult = RoundResult.getRoundResultFromSymbol(roundResultSymbol);
            Shape shapeThatYouNeed = Shape.getShapeToGetSpecificResult(roundResult, opponentShapeCode);
            String yourShapeCode = shapeThatYouNeed.getYourShapeCode();

            gameScore += calculatePointsForRound(yourShapeCode, opponentShapeCode);
        }
        return gameScore;
    }

    private static long getResultForFirstPartRules(List<String> strategyGuide) {
        long gameScore = 0;
        for (String symbolsForRound : strategyGuide) {
            String[] shapeCodes = symbolsForRound.split(" ");
            String yourShapeCode = shapeCodes[1];
            String opponentShapeCode = shapeCodes[0];

            gameScore += calculatePointsForRound(yourShapeCode, opponentShapeCode);
        }
        return gameScore;
    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day02/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);

        System.out.println("Part 1 = " + getResultForFirstPartRules(inputLines));
        System.out.println("Part 2 = " + getResultForSecondPartRules(inputLines));
    }
}
