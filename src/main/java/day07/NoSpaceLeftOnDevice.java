package day07;

import utils.MyUtilities;

import java.io.IOException;
import java.util.*;

/*
 * it works however it is big mess now :P
 * but one day I will be here back to clean it up
 * and make it beautiful and shining :D
 */
public class NoSpaceLeftOnDevice {

    static class DirectoriesSystem {
        private Map<Integer, Directory> directories;
        private Map<Integer, List<Integer>> adjacencyMap;
        private boolean[] visited;

        public DirectoriesSystem() {
            this.adjacencyMap = new HashMap<>();
            this.directories = new HashMap<>();
        }

        public void addDirectoryToMap(int index, Directory directory) {
            directories.put(index, directory);
        }

        public void addFileToDirectory(int index, File file) {
            Directory directory = directories.get(index);
            directory.addFile(file);
            directories.put(index, directory);
        }

        public int getIndexOfDirectoryByOuterDirIndexAndName(int outerDirIndex, String name) {
            List<Integer> indexesOfInnerDirectories = adjacencyMap.get(outerDirIndex);
            List<Directory> innerDirectories = indexesOfInnerDirectories.stream()
                    .map(index -> directories.get(index))
                    .toList();
            return innerDirectories.stream()
                    .filter(dir -> dir.getName().equals(name))
                    .map(Directory::getIndex)
                    .toList().get(0);
        }


        public void addDirectoryRelationAsTheirIndexesRelation(int from, int to) {
            List<Integer> currentList;
            if (adjacencyMap.containsKey(from)) {
                currentList = new ArrayList<>(adjacencyMap.get(from));
                currentList.add(to);
                adjacencyMap.put(from, currentList);
            } else {
                currentList = new ArrayList<>();
                currentList.add(to);
                adjacencyMap.put(from, List.of(to));
            }
        }

        public void printAdjacencyMap() {
            adjacencyMap.forEach((key, value) -> {
                System.out.println(key + " -> " + value.toString());
            });
        }

        public void printDirectoriesMap() {
            directories.forEach((key, value) -> {
                System.out.println(key + " -> " + value.toString());
            });
        }

        private boolean areAllInnerDirsVisited(int currentDirIndex) {
            List<Integer> innerDirsIndexes = adjacencyMap.get(currentDirIndex);
            if (innerDirsIndexes == null) return true;
            for (int currentIndex : innerDirsIndexes) {
                if (!visited[currentIndex]) return false;
            }
            return true;
        }

        private void calculateDirSizesByDfsTraversal(int startIndex) {
            if (areAllInnerDirsVisited(startIndex)) {
                visited[startIndex] = true;
                int totalDirSize = 0;
                Directory currentDir = directories.get(startIndex);
                List<Directory> innerDirs = adjacencyMap.getOrDefault(startIndex, new ArrayList<>()).stream()
                        .map(index -> directories.get(index))
                        .toList();
                int innerDirsTotalSize = innerDirs.stream()
                        .map(Directory::getTotalDirSize)
                        .mapToInt(Integer::intValue).sum();
                totalDirSize = currentDir.getFilesSize() + innerDirsTotalSize;
                currentDir.setTotalDirSize(totalDirSize);
                directories.put(startIndex, currentDir);
            }

            for (int currentDirIndex : adjacencyMap.getOrDefault(startIndex, new ArrayList<>())) {
                if (!visited[currentDirIndex]) {
                    calculateDirSizesByDfsTraversal(currentDirIndex);
                }
            }
        }

        public void calculateSizesOfAllDirectories() {
            int numOfDirs = directories.size();
            visited = new boolean[numOfDirs];
            while (!visited[0]) {
                calculateDirSizesByDfsTraversal(0);
            }
        }

        public long calculateSumSizeOfAllFilesBelowLimit(int limit) {
            return directories.values().stream()
                    .map(dir -> dir.getTotalDirSize())
                    .filter(totalSize -> totalSize <= limit)
                    .mapToInt(Integer::intValue).sum();
        }

        public int findDirectoryToDelete(int totalDiskSpace, int spaceNeededForUpdate) {
            int sizeOfDirToRemove = Integer.MAX_VALUE;
            int minimumDiff = Integer.MAX_VALUE;
            int leftTotalSpace = totalDiskSpace - directories.get(0).getTotalDirSize();
            int requireSpaceToRemove = spaceNeededForUpdate - leftTotalSpace;
            for (Directory currentDir : directories.values()) {
                int currentDirSize = currentDir.getTotalDirSize();
                if (currentDirSize >= requireSpaceToRemove) {
                    int currentDiff = currentDirSize - requireSpaceToRemove;
                    if (currentDiff < minimumDiff) {
                        minimumDiff = currentDiff;
                        sizeOfDirToRemove = currentDirSize;
                    }
                }
            }
            return sizeOfDirToRemove;
        }
    }

    static class File {
        private String name;
        private int size;

        public File(String name, int size) {
            this.name = name;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public int getSize() {
            return size;
        }

        @Override
        public String toString() {
            return "File{" +
                    "name='" + name + '\'' +
                    ", size=" + size +
                    '}';
        }
    }

    static class Directory {
        private int index;
        private String name;
        private int filesSize;
        private List<File> files;
        private int totalDirSize;

        public Directory(int index, String name) {
            this.index = index;
            this.name = name;
            this.files = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public int getFilesSize() {
            return filesSize;
        }

        public List<File> getFiles() {
            return files;
        }

        public int getIndex() {
            return index;
        }

        public void addFile(File file) {
            this.files.add(file);
            this.filesSize += file.getSize();
        }

        public int getTotalDirSize() {
            return totalDirSize;
        }

        public void setTotalDirSize(int totalDirSize) {
            this.totalDirSize = totalDirSize;
        }

        @Override
        public String toString() {
            return "Directory{" +
                    "index=" + index +
                    ", name='" + name + '\'' +
                    ", totalDirSize=" + totalDirSize +
                    ", filesSize=" + filesSize +
                    ", files=" + files +
                    '}';
        }
    }

    private static void goOverTerminalOutputAndCreateFilesAndDirectoriesAndAllStaffNeededHeHe(List<String> inputLines) {
        DirectoriesSystem directoriesSystem = new DirectoriesSystem();
        int dirIndex = 0;
        int size = inputLines.size();
        List<String> linesInsideDir = new ArrayList<>();
        String dirName;
        Directory currenDir;
        Stack<Integer> directoriesIndexStack = new Stack<>();
        directoriesIndexStack.push(dirIndex);
        for (int i = 1; i < size; i++) {
            String line = inputLines.get(i);
            if (line.contains("$ cd ..")) {
                directoriesIndexStack.pop();
                continue;
            }
            if (line.contains("$ cd")) {
                String currentDirectoryName = line.substring(line.indexOf("cd") + 2).strip();
                int outerDirIndex = directoriesIndexStack.peek();
                int currentDirIndex = directoriesSystem.getIndexOfDirectoryByOuterDirIndexAndName(outerDirIndex, currentDirectoryName);
                directoriesIndexStack.push(currentDirIndex);
            }
            if (line.contains("$ ls")) {
                String previousLine = inputLines.get(i - 1);
                dirName = previousLine.substring(previousLine.indexOf("cd") + 2).strip();
                int currentDirIndex;
                if (dirName.equals("/")) {
                    currentDirIndex = 0;
                    currenDir = new Directory(dirIndex, dirName);
                    directoriesSystem.addDirectoryToMap(dirIndex, currenDir);
                    dirIndex++;
                } else {
                    int currentDirIndexFromStack = directoriesIndexStack.pop();
                    int outerDirIndex = directoriesIndexStack.peek();
                    currentDirIndex = directoriesSystem.getIndexOfDirectoryByOuterDirIndexAndName(outerDirIndex, dirName);
                    directoriesIndexStack.push(currentDirIndexFromStack);
                }
                String lineInsideDir;
                linesInsideDir = new ArrayList<>();
                while (!(lineInsideDir = inputLines.get(++i)).contains("$")) {
                    linesInsideDir.add(lineInsideDir);
                    if (i == size - 1) break;
                }
                i--;
                for (String lineInDir : linesInsideDir) {
                    if (!lineInDir.contains("dir")) {
                        String fileName = lineInDir.split(" ")[1].strip();
                        int fileSize = Integer.parseInt(lineInDir.split(" ")[0].strip());
                        directoriesSystem.addFileToDirectory(currentDirIndex, new File(fileName, fileSize));
                    } else {
                        String currentDirName = lineInDir.split(" ")[1];
                        Directory insideDirectory = new Directory(dirIndex, currentDirName);
                        directoriesSystem.addDirectoryToMap(dirIndex, insideDirectory);
                        directoriesSystem.addDirectoryRelationAsTheirIndexesRelation(currentDirIndex, insideDirectory.getIndex());
                        dirIndex++;
                    }
                }
            }
        }
        directoriesSystem.calculateSizesOfAllDirectories();
        System.out.println("Part 1 = " + directoriesSystem.calculateSumSizeOfAllFilesBelowLimit(100000));
        System.out.println("Part 2 = " + directoriesSystem.findDirectoryToDelete(70000000, 30000000));
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day07/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);

        goOverTerminalOutputAndCreateFilesAndDirectoriesAndAllStaffNeededHeHe(inputLines);
    }
}
