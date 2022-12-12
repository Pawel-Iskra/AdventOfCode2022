package day12;

import utils.MyUtilities;

import java.io.IOException;
import java.util.*;

public class HillClimbingAlgorithm {

    static class DirectedEdge {
        private int from;
        private int to;
        private int highToClimb;

        public DirectedEdge(int from, int to, int highToClimb) {
            this.from = from;
            this.to = to;
            this.highToClimb = highToClimb;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        public int getHighToClimb() {
            return highToClimb;
        }
    }

    static class CurrentDistanceToStart {
        private int from;
        private int to;
        private long dist;

        public CurrentDistanceToStart(int from, int to, long dist) {
            this.from = from;
            this.to = to;
            this.dist = dist;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        public long getDist() {
            return dist;
        }
    }

    static class DirectedGraph {
        private int vertices;
        private int edges;
        private List<DirectedEdge>[] adjacencyList;

        public DirectedGraph(int vertices) {
            this.vertices = vertices;
            this.edges = 0;
            this.adjacencyList = new List[vertices];
            for (int i = 0; i < vertices; i++) {
                adjacencyList[i] = new ArrayList<>();
            }
        }

        public int getVertices() {
            return vertices;
        }

        public void addEdge(DirectedEdge directedEdge) {
            adjacencyList[directedEdge.getFrom()].add(directedEdge);
            edges++;
        }

        public List<DirectedEdge> getDirectedEdgesFromNode(int vertex) {
            return adjacencyList[vertex];
        }
    }

    static class DijkstraShortestPath {
        private DirectedGraph directedGraph;
        private int vertices;
        private long[] shortestPaths;
        private int[] previous;
        private boolean[] visited;

        public DijkstraShortestPath(DirectedGraph directedGraph) {
            this.directedGraph = directedGraph;
            this.vertices = directedGraph.getVertices();
            this.visited = new boolean[vertices];
            this.shortestPaths = new long[vertices];
            this.previous = new int[vertices];
        }

        private void clearArraysBeforeDijkstraAlgorithmStart(int start) {
            visited = new boolean[vertices];
            previous = new int[vertices];
            previous[start] = start;
            for (int i = 0; i < vertices; i++) {
                shortestPaths[i] = Long.MAX_VALUE;
            }
            shortestPaths[start] = 0;
        }

        public void runModifiedDijkstraAlgorithm(int start) {
            clearArraysBeforeDijkstraAlgorithmStart(start);

            Queue<CurrentDistanceToStart> currentDistancesQueue =
                    new PriorityQueue<>(vertices, Comparator.comparingLong(CurrentDistanceToStart::getDist));
            currentDistancesQueue.add(new CurrentDistanceToStart(start, start, 0));

            while (!currentDistancesQueue.isEmpty()) {
                visited[start] = true;
                start = currentDistancesQueue.poll().getTo();

                List<DirectedEdge> directedEdgesFromNode = directedGraph.getDirectedEdgesFromNode(start);
                long currDistToSrc = shortestPaths[start];

                int numOfEdges = directedEdgesFromNode.size();
                for (int i = 0; i < numOfEdges; i++) {
                    DirectedEdge currentEdge = directedEdgesFromNode.get(i);
                    if (currentEdge.getHighToClimb() > 1) continue; // specific condition
                    long calcDist = currDistToSrc + 1;
                    int to = currentEdge.getTo();
                    if (calcDist < shortestPaths[to]) {
                        shortestPaths[to] = calcDist;
                        previous[to] = start;
                        if (!visited[to]) {
                            currentDistancesQueue.add(
                                    new CurrentDistanceToStart(currentEdge.getFrom(), currentEdge.getTo(), calcDist));
                        }
                    }
                }
            }
        }

        public long[] getShortestPaths() {
            return shortestPaths;
        }

        public int[] getPrevious() {
            return previous;
        }

        public boolean[] getVisited() {
            return visited;
        }

        public long getShortestPathFromStartToGivenVertex(int interestingVertex) {
            return shortestPaths[interestingVertex];
        }
    }

    public static DirectedGraph createDirectedGraphFromInputLines(List<String> heightMap) {
        int rows = heightMap.size();
        int columns = heightMap.get(0).length();
        int vertices = rows * columns;
        DirectedGraph directedGraph = new DirectedGraph(vertices);

        for (int i = 0; i < vertices; i++) {
            int currentRow = i / columns;
            String currentMapLine = heightMap.get(currentRow);
            int currentColumn = i - currentRow * columns;

            int upperRow = currentRow - 1;
            if (upperRow >= 0) { // go up
                int heightToClimb = heightMap.get(upperRow).charAt(currentColumn) - currentMapLine.charAt(currentColumn);
                directedGraph.addEdge(new DirectedEdge(i, (i - columns), heightToClimb));
            }
            int lowerRow = currentRow + 1;
            if (lowerRow < rows) { // go down
                int heightToClimb = heightMap.get(lowerRow).charAt(currentColumn) - currentMapLine.charAt(currentColumn);
                directedGraph.addEdge(new DirectedEdge(i, (i + columns), heightToClimb));
            }
            int rightColumn = currentColumn + 1;
            if (rightColumn < columns) { // go right
                int heightToClimb = currentMapLine.charAt(rightColumn) - currentMapLine.charAt(currentColumn);
                directedGraph.addEdge(new DirectedEdge(i, (i + 1), heightToClimb));

            }
            int leftColumn = currentColumn - 1;
            if (leftColumn >= 0) { // go left
                int heightToClimb = currentMapLine.charAt(leftColumn) - currentMapLine.charAt(currentColumn);
                directedGraph.addEdge(new DirectedEdge(i, (i - 1), heightToClimb));
            }
        }
        return directedGraph;
    }

    private static void goThroughAreaUsingMap(List<String> heightMap) {
        int rows = heightMap.size();
        int columns = heightMap.get(0).length();
        int startVertex = -1;
        int bestSignalVertex = -1;
        List<Integer> verticesWithA = new ArrayList<>();
        for (int i = 0; i < rows; i++) { // changes in map
            for (int j = 0; j < columns; j++) {
                if ('S' == heightMap.get(i).charAt(j)) {
                    startVertex = i * columns + j;
                    StringBuilder strb = new StringBuilder(heightMap.get(i));
                    strb.replace(j, j + 1, "a");
                    heightMap.set(i, strb.toString());
                } else if ('E' == heightMap.get(i).charAt(j)) {
                    bestSignalVertex = i * columns + j;
                    StringBuilder strb = new StringBuilder(heightMap.get(i));
                    strb.replace(j, j + 1, "z");
                    heightMap.set(i, strb.toString());
                }
                if ('a' == heightMap.get(i).charAt(j)) {
                    verticesWithA.add(i * columns + j);
                }
            }
        }

        // part 1
        DirectedGraph graphFromMap = createDirectedGraphFromInputLines(heightMap);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graphFromMap);
        dijkstraShortestPath.runModifiedDijkstraAlgorithm(startVertex);
        System.out.println("Part 1 = " + dijkstraShortestPath.getShortestPathFromStartToGivenVertex(bestSignalVertex));

        // part 2
        List<Long> possiblePathsStepsFromAGrids = new ArrayList<>();
        for (int i = 0; i < verticesWithA.size(); i++) {
            dijkstraShortestPath.runModifiedDijkstraAlgorithm(verticesWithA.get(i));
            possiblePathsStepsFromAGrids.add(dijkstraShortestPath.getShortestPathFromStartToGivenVertex(bestSignalVertex));
        }
        possiblePathsStepsFromAGrids.sort(Comparator.naturalOrder());
        System.out.println("Part 2 = " + possiblePathsStepsFromAGrids.get(0));
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day12/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);

        goThroughAreaUsingMap(inputLines);
    }
}
