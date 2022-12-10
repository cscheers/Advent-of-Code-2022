import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Day09 {

    static int tailVisits;
    static Set<String> positions;

    static void addTailVisit(Point tail) {
        positions.add(tail.toString());
        tailVisits++;
    }

    static boolean follow(Point head, Point tail) {
        if (head.x == tail.x) { // Both in same column
            if (head.y - tail.y == 2) { // Move tail up
                tail.y++;
                return true;
            } else if (tail.y - head.y == 2) { // Move tail down
                tail.y--;
                return true;
            }
        } else if (head.y == tail.y) { // Both in same row
            if (head.x - tail.x == 2) { // Move tail right
                tail.x++;
                return true;
            } else if (tail.x - head.x == 2) { // Move tail left
                tail.x--;
                return true;
            }
        } else { // Needs diagonal move
            if (head.x - tail.x == 2 ||
                tail.x - head.x == 2) {
                if (head.x - tail.x == 2) { // Move tail right
                    tail.x++;
                } else { // Move tail left
                    tail.x--;
                }
                if (head.y > tail.y) { // Move tail up
                    tail.y++;
                } else { // Move tail down
                    tail.y--;
                }
                return true;
           } else if (head.y - tail.y == 2 ||
                      tail.y - head.y == 2) {
                if (head.y - tail.y == 2) { // Move tail up
                    tail.y++;
                } else { // Move tail down
                    tail.y--;
                }
                if (head.x > tail.x) { // Move tail right
                    tail.x++;
                } else { // Move tail left
                    tail.x--;
                }
                return true;
            }
        }
        return false;
    }

    static boolean move(Point[] rope, String direction, int steps) {
        for (int step = 0; step < steps; step++) {
            Point head = rope[0];
            switch (direction) {
                case "R":
                    head.x += 1;
                    break;
                case "L":
                    head.x -= 1;
                    break;
                case "U":
                    head.y += 1;
                    break;
                case "D":
                    head.y -= 1;
                    break;
            }
            for (int knot = 1; knot < rope.length; knot++) {
                Point tail = rope[knot];
                if (follow(head, tail)) {
                    if (knot == rope.length - 1) {
                        addTailVisit(tail);
                    }
                }
                head = tail;
            }
            Point tail = rope[rope.length - 1];
//            System.out.println("===> head: " + head + ", tail: " + tail);
        }
        return true;
    }

    static void readFile(File file, int knots) throws FileNotFoundException {
        Point[] rope = new Point[knots];
        for (int i = 0; i < rope.length; i++) {
            rope[i] = new Point(0, 0);
        }
        tailVisits = 1;
        positions = new HashSet<String>();
        positions.add(rope[0].toString());
        Scanner fs = new Scanner(file);
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            String[] tokens = line.split(" ");
            String direction = tokens[0];
            int steps = Integer.valueOf(tokens[1]);
//            System.out.println("===> Move: " + direction + ", steps: " + steps);
            if (!move(rope, direction, steps)) {
                break;
            }
        }
        System.out.println("Number of tail visits: " + tailVisits);
        System.out.println("Number of tail positions: " + positions.size());
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 9");
        File file = new File("data/day9.txt");
        readFile(file, 2);
        readFile(file, 10);
    }
}
