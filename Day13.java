import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class Day13 {

    static class TreeNode {
        TreeNode(int value) {
            this.value = value;
        }
        TreeNode(List<TreeNode> children) {
            this.children = children;
        }
        int value;
        List<TreeNode> children;
    }

    static int pos;
    static PacketComparator packetComparator = new PacketComparator();
    static List<TreeNode> packets = new ArrayList<TreeNode>();

    static int readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        List<TreeNode> left = null;
        int pairIndex = 1, total = 0;
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            if (line.length() == 0) {
                pairIndex++;
                continue;
            }
            pos = 0;
            if (left == null) {
                left = parseList(line);
                packets.add(new TreeNode(left));
            } else {
                List<TreeNode> right = parseList(line);
                packets.add(new TreeNode(right));
                if (packetComparator.compareLists(left, right) <= 0) { // in order
                    total += pairIndex;
                }
                left = null;
            }
        }
        return total;
    }

    static class PacketComparator implements Comparator<TreeNode> {
        public int compare(TreeNode left, TreeNode right) {
            if (left.children == null && right.children == null) { // Both are integers
                return Integer.compare(left.value, right.value);
            } else if (left.children != null && right.children != null) {
                return compareLists(left.children, right.children);
            } else if (left.children == null) {
                List<TreeNode> list = new ArrayList<TreeNode>();
                list.add(new TreeNode(left.value));
                return compareLists(list, right.children);
            } else  {
                List<TreeNode> list = new ArrayList<TreeNode>();
                list.add(new TreeNode(right.value));
                return compareLists(left.children, list);
            }
        }

        public int compareLists(List<TreeNode> left, List<TreeNode> right) {
            for (int i = 0; i < left.size(); i++) {
                TreeNode leftNode = left.get(i);
                if (i == right.size()) { // Right has fewer items
                    return 1; // Not the right order
                }
                TreeNode rightNode = right.get(i);
                int ordered = compare(leftNode, rightNode);
                if (ordered != 0) {                
                    return ordered; // Don't look any further
                }
            }
            return left.size() < right.size() ? -1 : 0;
        }
    }

    static List<TreeNode> parseList(String line) {
        if (line.charAt(pos) != '[') {
            System.out.println("Expecting '[' at: " + pos);
            return null;
        }
        pos++;
        List<TreeNode> list = new ArrayList<TreeNode>();
        while (true) { // parse a list of elements
            if (line.charAt(pos) == ']') { // Must be empty list
            } else if (line.charAt(pos) == '[') { // Element is nested list
                list.add(new TreeNode(parseList(line)));
            } else { // Number followed by comma
                int comma = line.indexOf(',', pos);
                int close = line.indexOf(']', pos);
                int end = comma == -1 || comma > close ? close : comma;
                int value = Integer.valueOf(line.substring(pos, end));
                list.add(new TreeNode(value));
                pos = end;
            }
            if (line.charAt(pos) == ',') {
                pos++; // Another element
            } else if (line.charAt(pos) == ']') {
                pos++; // End of list
                break;
            } else {
                System.out.println("Parsing error at: " + pos);
            }
        }
        return list;
    }

    public static void showList(List<TreeNode> nodes) {
        System.out.print("[");
        for (TreeNode node : nodes) {
            showTreeNode(node);
        }
        System.out.println("]");
    }

    public static void showTreeNode(TreeNode node) {
        if (node.children == null) {
            System.out.print(node.value + ",");
        } else {
            System.out.print("[");
            for (TreeNode child : node.children) {
                showTreeNode(child);
            }
            System.out.print("],");
        }        
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 13");
        int total = readFile(new File("data/day13.txt"));
        System.out.println("Total in right order: " + total);

        TreeNode dividerOne = packets.get(packets.size() - 1);
        TreeNode dividerTwo = packets.get(packets.size() - 2);
        packets.sort(packetComparator);
        int key = 1;
        for (int i = 0; i < packets.size(); i++) {
            TreeNode node = packets.get(i);
            if (packetComparator.compare(node, dividerOne) == 0 ||
                packetComparator.compare(node, dividerTwo) == 0) {
                key *= i + 1;
            }
        }
        System.out.println("Decoder key: " + key);
    }
}
