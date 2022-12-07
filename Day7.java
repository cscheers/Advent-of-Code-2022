import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.LinkedList;

public class Day7 {

    static Directory root = new Directory("/", null);
    static int total = 0, free = 0;

    static class Directory {
        Directory(String name, Directory parent) {
            this.name = name;
            this.parent = parent;
            if (parent != null) {
                parent.subDirs.add(this);
            }
            this.subDirs = new LinkedList<Directory>();
        }
        String name;
        List<Directory> subDirs;
        Directory parent;
        int size;
    }

    static void readFile(File file) throws FileNotFoundException {
        Directory current = null;
        Scanner fs = new Scanner(file);
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
            String[] tokens = line.split(" ");
            if (tokens[0].equals("$")) {
                if (tokens.length == 2) { // ls
                } else if (tokens[2].equals("..")) { // cd up one level
                    current = current.parent;
                } else if (tokens[2].equals("/")) { // cd root
                    current = root;
                } else { // cd subdir
                    current = new Directory(tokens[2], current);
                }
            } else if (tokens[0].equals("dir")) { // ls a dir
            } else { // ls a file
                current.size += Integer.valueOf(tokens[0]);
            }
        }
    }

    static int getDirSize(Directory dir) {
        for (Directory subDir : dir.subDirs) {
            dir.size  += getDirSize(subDir);
        }
        if (dir.size <= 100000) {
            total += dir.size;
        }
        return dir.size;
    }

    static void getFreeDir(Directory dir, int freeAtLeast, boolean verbose) {
        for (Directory subDir : dir.subDirs) {
            getFreeDir(subDir, freeAtLeast, verbose);
        }
        if (dir.size > freeAtLeast) {
            if (verbose) {
                System.out.println("Candidate dir: " + dir.name + ", size: " + dir.size);
            }
            if (free == 0 || dir.size < free) {
                free = dir.size;
            }
        }
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 7");
        readFile(new File("data/day7.txt"));
        int unused = 70000000 - getDirSize(root);
        System.out.println("Sum of directories with total size of at most 100000: " + total);
        int freeAtLeast = 30000000 - unused;
        System.out.println("Need to free up at least: " + freeAtLeast);
        getFreeDir(root, freeAtLeast, false);
        System.out.println("Free one directory with size: " + free);
    }
}
