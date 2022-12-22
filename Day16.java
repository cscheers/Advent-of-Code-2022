import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class Day16 {

    static class Valve {
        Valve(int rate, List<String> next) {
            this.rate = rate;
            this.next = next;
        }
        int rate;
        List<String> next;
        Map<String, Integer> distances;
    }

    static Map<String, Valve> valves = new HashMap<String, Valve>();
    static Set<String> opened = new HashSet<String>();
    static int maxFlow = 0;

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            String[] tokens = line.split(" ");
            String valve = tokens[1];
            int rate = Integer.valueOf(tokens[4].substring(0, tokens[4].length()-1).split("=")[1]);
            List<String> next = new ArrayList<String>();
            for (int i = 9; i < tokens.length; i++) {
                next.add(tokens[i].split(",")[0]);
            }
            valves.put(valve, new Valve(rate, next));
//            System.out.println("Valve: " + valve + ", rate: " + rate + ", next: " + next);
        }
    }

    public static String findNextValve(Set<String> accounted, Map<String, Integer> distances) {
        int min = Integer.MAX_VALUE;
        String valve = null;
        for (Map.Entry<String, Integer> entry : distances.entrySet()) {
            String name = entry.getKey();
            Integer distance = entry.getValue();
            if (!accounted.contains(name) && distance < min) {
                min = distance;
                valve = name;
            }
        }
        return valve;
    }

    public static Map<String, Integer> shortestPath(String src) {
        Set<String> accounted = new HashSet<String>();
        Map<String, Integer> distances = new HashMap<String, Integer>();
        distances.put(src, 0);
        String valve = src;
        while (valve != null) {
            for (String next : valves.get(valve).next) {
                if (accounted.contains(next)) {
                    continue; // Shortest path to here already known.
                }
                int toCurrent = distances.get(valve);
                if (distances.containsKey(next)) {
                    int toNext = distances.get(next);
                    if (toCurrent + 1 >= toNext) {
                        continue; // Don't update distance
                    }
                }                
                distances.put(next, toCurrent + 1);
            }
            accounted.add(valve); // All nbrs of current have been visited.
            valve = findNextValve(accounted, distances);
        }
        distances.remove(src); // Exclude node itself
        Set<String> zeros = new HashSet<String>();
        for (String v : distances.keySet()) {
            if (valves.get(v).rate == 0) {
                zeros.add(v);
            }
        }
        distances.keySet().removeAll(zeros); // Remove all zero rate valves
//        System.out.println("Distances from: " + src + ": " + distances);
        return distances;
    }

    static LinkedList<String> path = new LinkedList<String>();

    public static void tryValve(String name, int remaining, int flow) {
        maxFlow = Math.max(maxFlow, flow);
        Valve valve = valves.get(name);
        if (valve.distances == null) {
            valve.distances = shortestPath(name);
        }
        for (Map.Entry<String, Integer> entry : valve.distances.entrySet()) {
            String next = entry.getKey();
            Integer distance = entry.getValue();
            Valve nextValve = valves.get(next);
            if (nextValve.rate == 0) {
                continue;
            }
            if (opened.contains(next)) {
                continue;
            }
            if (remaining - distance - 1 <= 0) {
                continue;
            }
            path.add(next);
            opened.add(next);
            tryValve(next, remaining - distance - 1, flow + nextValve.rate * (remaining - distance - 1));
            opened.remove(next);
            path.removeLast();
        }
    }

    public static void tryTwo(String one, String two, int remOne, int remTwo, int flow) {
        maxFlow = Math.max(maxFlow, flow);
        Valve valve = valves.get(remOne < remTwo ? two : one);
        for (Map.Entry<String, Integer> entry : valve.distances.entrySet()) {
            String next = entry.getKey();
            Integer distance = entry.getValue();
            Valve nextValve = valves.get(next);
            if (nextValve.rate == 0) {
                continue;
            }
            if (opened.contains(next)) {
                continue;
            }
            int remaining = (remOne < remTwo ? remTwo : remOne) - distance - 1;
            if (remaining <= 0) {
                continue;
            }
            opened.add(next);
            if (remOne < remTwo) {
                tryTwo(one, next, remOne, remaining, flow + nextValve.rate * remaining);
            } else {
                tryTwo(next, two, remaining, remTwo, flow + nextValve.rate * remaining);
            }
            opened.remove(next);
        }
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 16");
        readFile(new File("data/day16.txt"));
        tryValve("AA", 30, 0);
        System.out.println("Max flow single: " + maxFlow);

        long startTime = System.currentTimeMillis();
        maxFlow = 0;
        tryTwo("AA", "AA", 26, 26, 0);
        long endTime = System.currentTimeMillis() - startTime;
        System.out.println("Max flow two: " + maxFlow + " in: " + endTime + "ms");
    }
}
