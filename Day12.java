import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;

public class Day12 {

    static List<char[]> area;
    static Point end;

    static Point readFile(File file) throws FileNotFoundException {
        area = new ArrayList<char[]> ();
        Scanner fs = new Scanner(file);
        Point start = null;;
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == 'S') {
                    start = new Point(x, area.size());
                } else if (line.charAt(x) == 'E') {
                    end = new Point(x, area.size());
                }
            }
            area.add(line.toCharArray());
        }
        area.get(start.y)[start.x] = 'a';
        area.get(end.y)[end.x] = 'z';
        return start;
    }

    public static void tryNext(List<Point> level, char fromHeight, int toX, int toY) {
        if (toY < 0 || toY >= area.size()) {
            return; // moving off the grid
        }
        if (toX < 0 || toX >= area.get(toY).length) {
            return; // moving off the grid
        }
        char toHeight = area.get(toY)[toX];
        if (toHeight == ' ') { // Already processed
            return;
        }
        if (toHeight <= fromHeight + 1) {
            level.add(new Point(toX, toY));
        }
    }

    public static int bfs(Point start) {
        List<Point> level = new ArrayList<Point>();
        level.add(start);
        int moves = 0;
        while (!level.isEmpty()) {
            List<Point> nextLevel = new ArrayList<Point>();
            for (Point p : level) {
                if (p.equals(end)) {
                    return moves;
                }
                char fromHeight = area.get(p.y)[p.x];
                if (fromHeight != ' ') { // Because the list can contain duplicates
                    tryNext(nextLevel, fromHeight, p.x + 1, p.y);
                    tryNext(nextLevel, fromHeight, p.x - 1, p.y);
                    tryNext(nextLevel, fromHeight, p.x, p.y + 1);
                    tryNext(nextLevel, fromHeight, p.x, p.y - 1);
                    area.get(p.y)[p.x] = ' '; // Done with this one
                }
            }
            level = nextLevel;
            moves++;
        }
        return -1;
    }

    public static List<Point> findStartingPoints() {
        List<Point> starts = new ArrayList<Point>();
        for (int y = 0; y < area.size(); y++) {
            for (int x = 0; x < area.get(y).length; x++) {
                if (area.get(y)[x] == 'a') {
                    starts.add(new Point(x, y));
                }
            }
        }
        return starts;
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 12");
        Point start = readFile(new File("data/day12.txt"));
        System.out.println("Start: " + start);
        System.out.println("End: " + end);
        System.out.println("Grid: " + area.get(0).length + ", " + area.size());
        List<Point> starts = findStartingPoints();
        System.out.println("Number of starting points: " + starts.size());
        System.out.println("Shortest path from S: " + bfs(start));
        int min = area.size() * area.get(0).length;
        for (Point s : starts) {
            readFile(new File("data/day12.txt"));
            int length = bfs(s);
            if (length > 0 && length < min) {
                System.out.println("New shortest path from: " + s + ", length: " + length);
                min = length;
            }
        }
        System.out.println("Shortest path from all a's: " + min);
    }
}
