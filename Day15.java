import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;
import java.math.BigInteger;

public class Day15 {


    static int selectedY = 2000000;
    static Set<Integer> beacons = new HashSet<Integer>();
    static Set<Integer> noBeacon = new HashSet<Integer>();

    static int boundary = 4000000; // 20
    static List<List<Point>> space = new ArrayList<List<Point>>();

    static void processSensorPartOne(Point sensor, Point beacon) {
        if (beacon.y == selectedY) {
            System.out.println("Adding beacon at: " + beacon.x);
            beacons.add(beacon.x);
        }
        int distance = Math.abs(beacon.x - sensor.x) + Math.abs(beacon.y - sensor.y);
        int dy = Math.abs(selectedY - sensor.y);
        if (dy <= distance) {
            int dx = distance - dy;
            int start = sensor.x - dx;
            for (int i = 0; i <= 2 * dx; i++) {
                noBeacon.add(start + i);
            }
        }
    }

    static void exclude(int y, int left, int right) {
//        System.out.println("At Y: " + y + ", exclude (" + left + ", " + right + ")");    
        if (y < 0 || y >= space.size()) {
            return;
        }
        List<Point> oldAvailable = space.get(y);
        List<Point> newAvailable = new ArrayList<Point>();
        for (Point oldRange : oldAvailable) {
            if (oldRange.x < left) { // Old range starts to the left of remove range.
                if (oldRange.y < left) { // Old range is left of remove range, just copy.
                    newAvailable.add(oldRange);
                } else { // Chunk of old range remains.
                    newAvailable.add(new Point(oldRange.x, left - 1));
                    if (oldRange.y > right) { // Remove range ends inside of old range, stop here.
                        newAvailable.add(new Point(right + 1, oldRange.y));
                    } else { // New range goes beyond this range, drop this one and continue to next.
                    }
                }
            } else if (oldRange.x == left) { // Old range starts where remove range starts.
                if (oldRange.y > right) { // Remove range ends inside of old range, stop here.
                    newAvailable.add(new Point(right + 1, oldRange.y));
                } else { // New range goes beyond this range, drop this one and continue to next.
                }
            } else { // oldRange.x > left       // Old range starts to the right of remove range.
                if (oldRange.x > right) { // Remove range is to the left of old range, just copy and done.
                    newAvailable.add(oldRange);
                } else if (oldRange.y < right) { // Old range disappears completely: " + oldRange
                } else { // Part of old range remains, continue." + oldRange
                    newAvailable.add(new Point(right + 1, oldRange.y));
                }
            }
        }
        space.set(y, newAvailable);
    }

    static void processSensorPartTwo(Point sensor, Point beacon) {
        int distance = Math.abs(beacon.x - sensor.x) + Math.abs(beacon.y - sensor.y);
        for (int i = 0; i <= distance; i++) {
            int y = sensor.y - (distance - i);
            int left = sensor.x - i;
            int right = sensor.x + i;
            exclude(y, left, right);
            y = sensor.y + distance - i;
            if (i != distance) {
                exclude(y, left, right);
            }
        }
    }

    static BigInteger getTuningFrequency() {
        BigInteger x = null, y = null;
        for (int i = 0; i < space.size(); i++) {
            if (space.get(i).size() == 0) {
                continue;
            }
            if (space.get(i).size() != 1 || x != null) {
                System.out.println("More than one solution.");
                return null; // Only one spot should remain
            }
            x = BigInteger.valueOf(space.get(i).get(0).x);
            y = BigInteger.valueOf(i);
        }
        return x.multiply(BigInteger.valueOf(4000000)).add(y);
    }

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        for (int i = 0; i <= boundary; i++) {
            List<Point> exclude = new ArrayList<Point>();
            exclude.add(new Point(0, boundary));
            space.add(exclude);
        }
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            String[] tokens = line.split(" ");
            int sensorX = Integer.valueOf(tokens[2].substring(0, tokens[2].length()-1).split("=")[1]);
            int sensorY = Integer.valueOf(tokens[3].substring(0, tokens[3].length()-1).split("=")[1]);
            System.out.print("Sensor (" + sensorX + ", " + sensorY + "), ");
            int beaconX = Integer.valueOf(tokens[8].substring(0, tokens[8].length()-1).split("=")[1]);
            int beaconY = Integer.valueOf(tokens[9].split("=")[1]);
            System.out.println("closest beacon at (" + beaconX + ", " + beaconY + ")");
            processSensorPartOne(new Point(sensorX, sensorY), new Point(beaconX, beaconY));
            processSensorPartTwo(new Point(sensorX, sensorY), new Point(beaconX, beaconY));
        }
        System.out.println("Beacon at: " + beacons);
        noBeacon.removeAll(beacons);
        System.out.println("Number of non-beacon positions in row (" + selectedY + "): "  + noBeacon.size());
        System.out.println("Tuning frequency: " + getTuningFrequency());
        ;
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 15");
        readFile(new File("data/day15.txt"));
    }
}
