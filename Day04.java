import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Point;

public class Day04 {

    static boolean fullyContains(Point first, Point second) {
        return first.x <= second.x && first.y >= second.y;
    }

    static boolean overlaps(Point first, Point second) {
        return first.x <= second.x && first.y >= second.x ||
               first.x >= second.x && first.x <= second.y;
    }

    static Point getRange(String range) {
        String[] pair = range.split("-");
        Point point = new Point(Integer.valueOf(pair[0]),
                                Integer.valueOf(pair[1]));
        return point;
    }

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        int countContained = 0, overlapping = 0;
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
            String[] pair = line.split(",");
            Point first = getRange(pair[0]);
            Point second = getRange(pair[1]);
            if (fullyContains(first, second) ||
                fullyContains(second, first)) {
                countContained++;
            }
            if (overlaps(first, second)) {
                overlapping++;
            }
        }
        System.out.println("Fully contained pairs: " + countContained);
        System.out.println("Overlapping pairs: " + overlapping);
    }

    static void play() {
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 4");
        readFile(new File("data/day4.txt"));
        play();
    }
}
