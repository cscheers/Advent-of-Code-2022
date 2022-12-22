import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

public class Day17 {

    static int chamberWidth = 7;
    static int rockCount = 10;

    static int[] tops = new int[chamberWidth];
    static int maxTop = 0;
    static String jets;

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
            jets = line;
        }
    }

    static Point[] rock;
    static int rockCol, rockRow;
    static List<Point[]> rocks = new ArrayList<Point[]>();

    static Point[] createFirstRock() {
        Point[] rock = new Point[4];
        for (int col = 0; col < rock.length; col++) {
            rock[col] = new Point(0, 1);
        }
        return rock;
    }

    static Point[] createSecondRock() {
        Point[] rock = new Point[3];
        rock[0] = new Point(1, 2);
        rock[1] = new Point(0, 3);
        rock[2] = new Point(1, 2);
        return rock;
    }

    static Point[] createThirdRock() {
        Point[] rock = new Point[3];
        rock[0] = new Point(0, 1);
        rock[1] = new Point(0, 1);
        rock[2] = new Point(0, 3);
        return rock;
    }

    static Point[] createFourthRock() {
        Point[] rock = new Point[1];
        rock[0] = new Point(0, 4);
        return rock;
    }

    static Point[] createFifthRock() {
        Point[] rock = new Point[2];
        rock[0] = new Point(0, 2);
        rock[1] = new Point(0, 2);
        return rock;
    }

    static void createRocks() {
        rocks.add(createFirstRock());
        rocks.add(createSecondRock());
        rocks.add(createThirdRock());
        rocks.add(createFourthRock());
        rocks.add(createFifthRock());
    }

    static int jetIndex = 0;

    static char getNextJet() {
        if (jetIndex == jets.length()) {
            jetIndex = 0;
        }
        return jets.charAt(jetIndex++);
    }

    static int rockIndex = 0;

    static Point[] getNextRock() {
        if (rockIndex == rocks.size()) {
            rockIndex = 0;
        }
        return rocks.get(rockIndex++);
    }

    static void doJet() {
        char pattern = getNextJet();
        System.out.println("Pattern: " + pattern);
        if (pattern == '<') {
            if (rockCol > 0) {
                System.out.println("Move rock left by one.");
                int rockLeftEdge = rockRow + rock[0].x;
                int topNextToRock = tops[rockCol - 1];
                System.out.println("Can rock at: " + rockLeftEdge + ", move left to: " + topNextToRock);
                if (topNextToRock > rockLeftEdge) {
                    System.out.println("Rock can not move left due to blocking rock.");
                } else {
                    rockCol--;
                }
            } else {
                System.out.println("Rock can not move left.");
            }
        } else { // pattern == '>'
            if (rockCol + rock.length < chamberWidth) {
                System.out.println("Move rock right by one.");
                int rockRightEdge = rockRow + rock[rock.length - 1].x;
                int topNextToRock = tops[rockCol + rock.length];
                System.out.println("Can rock at: " + rockRightEdge + ", move right to: " + topNextToRock);
                if (topNextToRock > rockRightEdge) {
                    System.out.println("Rock can not move right due to blocking rock.");
                } else {
                    rockCol++;
                }
            } else {
                System.out.println("Rock can not move right.");
            }
        }
    }

    static boolean drop() {
        for (int dx = 0; dx < rock.length; dx++) {
            int x = rockCol + dx;
            int y = rockRow + rock[dx].x;
            System.out.println("Rock izz at (" + x + ", " + y + ")");
            System.out.println("Top izz at (" + x + ", " + tops[x] + ")");
            if (y < 0) {
                System.out.println("*** stop ...");
                return false;
            }
            if (y == tops[x]) {
                return false;
            }
        }
        return true;
    }

    static void addRock() {
        for (int dx = 0; dx < rock.length; dx++) {
            int x = rockCol + dx;
            int y = rockRow + rock[dx].x;
            System.out.println("Rock is at (" + x + ", " + y + ")");
            System.out.println("Top is at (" + x + ", " + tops[x] + ")");
            tops[x] = rockRow + rock[dx].y;
            maxTop = Math.max(maxTop, tops[x]);
            System.out.println("New top is at (" + x + ", " + tops[x] + ")");
        }
    }

    static void showTop() {
        System.out.print("Top is at: ");
        for (int x = 0; x < tops.length; x++) {
            System.out.print(tops[x] + ", ");
        }
        System.out.println("  --> max: " + maxTop);
    }

    static void play() {
        createRocks();

        while (rockCount-- > 0) {
            rock = getNextRock();
            rockCol = 2;
            rockRow = maxTop + 3;

            doJet();
            while (drop()) {
                rockRow--;
                System.out.println("Rock dropped to: " + rockRow);
                doJet();
            }
            System.out.println("--> Rock stopped dropping.");
            addRock();
            showTop();
        }
//        showTop();
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 17");
        readFile(new File("data/day17.txt"));
        play();
    }
}
