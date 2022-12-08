import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Day08 {

    static List<String> forest = new ArrayList<String>();
    static char[][] visible;

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
            forest.add(line);
        }
    }

    static void scanRow(int row, int from, int to, int step) {
        String trees = forest.get(row);
        char prevHeight = trees.charAt(from);
        for (int i = from + step; i != to; i += step) {
            char tree = trees.charAt(i);
            if (tree > prevHeight) {
                visible[row][i] = '1';
                prevHeight = tree;
            }
        }
    }

    static void scanColumn(int col, int from, int to, int step) {
        char prevHeight = forest.get(from).charAt(col);
        for (int i = from + step; i != to; i += step) {
            char tree = forest.get(i).charAt(col);
            if (tree > prevHeight) {
                visible[i][col] = '1';
                prevHeight = tree;
            }
        }
    }

    static int countVisible() {
        visible = new char[forest.size()][forest.get(0).length()];
        for (char[] row : visible) {
            Arrays.fill(row, '0');
        }
        for (int row = 1; row < forest.size() - 1; row++) {
            scanRow(row, 0, forest.get(row).length() - 1, 1);
            scanRow(row, forest.get(row).length() - 1, 0, -1);
        }
        for (int col = 1; col < forest.get(0).length() - 1; col++) {
            scanColumn(col, 0, forest.size() - 1, 1);
            scanColumn(col, forest.size() - 1, 0, -1);
        }        
        int count = 2 * visible.length + 2 * visible[0].length - 4;
        for (char[] row : visible) {
            for (int i = 1; i < row.length - 1; i++) {
                if (row[i] == '1') {
                    count++;
                }
            }
        }
        return count;
    }

    static int findRowDistance(int row, int from, int to, int step) {
        String trees = forest.get(row);
        char current = trees.charAt(from), distance = 0;
        for (int i = from + step; i != to; i += step) {
            char tree = trees.charAt(i);
            if (tree >= current) {
                return distance + 1;
            }
            distance++;
        }
        return distance;
    }

    static int findColumnDistance(int col, int from, int to, int step) {
        char current = forest.get(from).charAt(col), distance = 0;
        for (int i = from + step; i != to; i += step) {
            char tree = forest.get(i).charAt(col);
            if (tree >= current) {
                return distance + 1;
            }
            distance++;
        }
        return distance;
    }

    static int tryTree(int row, int col) {
        int right = findRowDistance(row, col, forest.get(3).length(), 1);
        int left = findRowDistance(row, col, -1, -1);
        int down = findColumnDistance(col, row, forest.size(), 1);
        int up = findColumnDistance(col, row, -1, -1);
        int score = left * right * up * down;
        return left * right * up * down;
    }
    
    static int findScenic() {
        int max = 0;
        for (int row = 1; row < forest.size() - 1; row++) {
            for (int col = 1; col < forest.get(row).length() - 1; col++) {
                max = Math.max(max, tryTree(row, col));
            }
        }
        return max;
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 8");
        readFile(new File("data/day8.txt"));
        System.out.println("Number of visible: " + countVisible());
        System.out.println("Best scenic score: " + findScenic());
    }
}
