package day06;

import utils.MyUtilities;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TuningTrouble {

    private static long findStartOfPacketMarkerInDataStream(String dataStream, int lengthOfSequenceOfUniqueCharacters) {
        int dataStreamSize = dataStream.length();
        int numOfProceededCharacters = 0;
        Set<Character> uniqueChars;
        for (int i = 0; i < dataStreamSize - lengthOfSequenceOfUniqueCharacters; i++) {
            uniqueChars = new HashSet<>();
            for (int j = 0; j < lengthOfSequenceOfUniqueCharacters; j++) {
                uniqueChars.add(dataStream.charAt(i + j));
            }
            if (uniqueChars.size() == lengthOfSequenceOfUniqueCharacters) {
                numOfProceededCharacters = i + lengthOfSequenceOfUniqueCharacters;
                break;
            }
        }
        return numOfProceededCharacters;
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day06/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);

        System.out.println("Part 1 = " + findStartOfPacketMarkerInDataStream(inputLines.get(0), 4));
        System.out.println("Part 2 = " + findStartOfPacketMarkerInDataStream(inputLines.get(0), 14));
    }
}
