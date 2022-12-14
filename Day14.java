import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.awt.Point;

public class Day14 {

    static Point createInterval(int from, int to) {
        return from < to ? new Point(from, to) : new Point(to, from);
    }

    static boolean overlaps(Point first, Point second) {
        return first.x <= second.x && first.y >= second.x ||
               first.x >= second.x && first.x <= second.y;
    }

    static void addInterval(Map<Integer, List<Point>> levels, int level, int from, int to) {
        if (!levels.containsKey(level)) {
            levels.put(level, new ArrayList<Point>());
        }
        Point newInterval = createInterval(from, to);
        List<Point> intervals = levels.get(level);
        for (Point interval : intervals) {
            if (overlaps(newInterval, interval)) {
                if (newInterval.x >= interval.x && newInterval.y <= interval.y) {
                    return; // Interval already included, don't add it.
                } else if (newInterval.x == interval.y ) {
                    interval.y = newInterval.y; // Concatenate intervals
                    return;
                } else if (newInterval.y == interval.y ) { // Same y
                    if (newInterval.x < interval.x) {
                        interval.x = newInterval.x; // Take the larger x
                    }
                    return;
                } else {
                    System.out.println("Overlapping interval --> " + newInterval + ", " + interval);
                }
            }
        }
        intervals.add(newInterval);
    }

    static Map<Integer, List<Point>> horizontal, vertical;
    static int maxY = 0;

    static void readFile(File file) throws FileNotFoundException {
        horizontal = new TreeMap<Integer, List<Point>>();
        vertical = new TreeMap<Integer, List<Point>>();
        Scanner fs = new Scanner(file);
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            String[] xys = line.split(" -> ");
            String[] from = xys[0].split(",");
            int fromX = Integer.valueOf(from[0]);
            int fromY = Integer.valueOf(from[1]);
            for (int i = 1; i < xys.length; i++) {
                String[] to = xys[i].split(",");
                int toX = Integer.valueOf(to[0]);
                int toY = Integer.valueOf(to[1]);
                maxY = Math.max(maxY, fromY);
                maxY = Math.max(maxY, toY);
                if (fromY == toY) {
                    addInterval(horizontal, fromY, fromX, toX);
                } else {
                    int minY = Math.min(fromY, toY);
                    addInterval(horizontal, minY, fromX, fromX); // Top rock only
                    addInterval(vertical, fromX, fromY, toY);
                }
                fromX = toX;
                fromY = toY;
            }
        }
//        System.out.println("Horizontal intervals:");
//        showLevels(horizontal);
//        System.out.println("Vertical intervals:");
//        showLevels(vertical);
//        System.out.println("maxY: " + maxY);
    }

    static void showLevels(Map<Integer, List<Point>> levels) {
        for (Map.Entry<Integer, List<Point>> entry : levels.entrySet()) {
            int level = entry.getKey();
            List<Point> intervals = entry.getValue();
            System.out.print("Intervals at level: " + level + ": ");
            showIntervals(intervals);
            System.out.println();
        }
    }

    static void showIntervals(List<Point> intervals) {
        for (Point interval : intervals) {
            System.out.print("(" + interval.x + ", " + interval.y + "), ");
        }
    }

    static boolean intervalsContain(List<Point> intervals, int value) {
        for (Point interval : intervals) {
            if (value >= interval.x && value <= interval.y) {
                return true;
            }
        }
        return false;
    }

    static boolean tryMoveDiagonal(int hLevel, int y) {
        List<Point> intervals = vertical.get(hLevel); // Find vertical intervals at hLevel
        return intervals == null || !intervalsContain(intervals, y);
    }

    static Point drop(int x, int y, boolean bottomLess) {
        for (Map.Entry<Integer, List<Point>> entry : horizontal.entrySet()) {
            int vLevel = entry.getKey(); // Move down to next vertical (y) level
            List<Point> intervals = entry.getValue();
            for (Point interval : intervals) {
                if (x >= interval.x && x <= interval.y) { // Found rock at vLevel
                    if (tryMoveDiagonal(x - 1, vLevel) &&
                        !intervalsContain(intervals, x - 1)) { // Continue down
                        x = x - 1;
                    } else if (tryMoveDiagonal(x + 1, vLevel) &&
                               !intervalsContain(intervals, x + 1)) { // Continue down
                        x = x + 1;
                    } else { // Can't move left or right, drop down to vLevel - 1
                        return new Point(x, vLevel - 1);
                    }
                    y = vLevel; // drop diagonally to vLevel
                    break;
                }
            }
        }
        return bottomLess ? null : new Point(x, maxY + 1);
    }

    public static void partOne(File file) throws FileNotFoundException {
        readFile(file);
        Point sand = drop(500, 0, true);
        int units = 0;
        for (; sand != null; units++) {
            addInterval(horizontal, sand.y, sand.x, sand.x);
            sand = drop(500, 0, true);
        }
        System.out.println("Part One Units: " + units);
    }

    public static void partTwo(File file) throws FileNotFoundException {
        readFile(file);
        Point sand = drop(500, 0, false);
        int units;
        for (units = 1; sand.x != 500 || sand.y != 0; units++) {
            addInterval(horizontal, sand.y, sand.x, sand.x);
            sand = drop(500, 0, false);
        }
        System.out.println("Part Two Units: " + units);
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 14");
        File file = new File("data/day14.txt");
        partOne(file);
        partTwo(file);
    }
}
