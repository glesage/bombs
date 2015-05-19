package hmk;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author WMarrero
 */
public class Solution {

    private static class Point {
        public int x, y;
        public boolean seen;
        public Point parent;

        public Point() {
            this.seen = false;
            this.parent = null;
        }

        public Point(int x, int y) {
            this();
            this.x = x;
            this.y = y;
        }


        public Point(int x, int y, Point parent) {
            this();
            this.x = x;
            this.y = y;
            this.parent = parent;
        }

        public Point(String input) {
            this(Integer.parseInt(input.split(",")[1]),
                    Integer.parseInt(input.split(",")[0]));
        }

        public String directionsTo(Point p) {
            if (this.x - p.x == -1) return "L";
            if (this.x - p.x == 1) return "R";
            if (this.y - p.y == -1) return "U";
            if (this.y - p.y == 1) return "D";
            return "X";
        }

        @Override
        public int hashCode() {
            return this.toString().hashCode();
        }

        @Override
        public String toString() {
            return Integer.toString(this.y) + "," + Integer.toString(this.x);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Point)) return false;
            if (obj == this) return true;

            Point p = (Point) obj;
            return (this.x == p.x && this.y == p.y);
        }
    }

    /**
     * Starts with the start element, and spread around attempting to find the goal point, using depth first search
     *
     * @param start The point to start at
     * @param goal The goal point
     * @param size The size of the map
     * @param bombs The bombs/walls/obstacles of the map
     * @return the goal point that we found, from which we can get all parents and build the path back
     */
    public static Point buildMapFromStart(Point start, Point goal, int size,  HashMap<Integer, Point> bombs) {

        Point current = new Point();

        Point subB = new Point();
        int subKey = 0;

        Queue<Point> queue = new LinkedList<Point>();
        queue.add(start);

        while (!queue.isEmpty()) {
            current = queue.poll();

            // If a point has been visited when you observe it, don't do anything with it
            if (current.seen) continue;

            // Add its valid neighbours to the queue and set the parent to the current point
            if (current.x < size) {
                subKey = (Integer.toString(current.y) + "," + Integer.toString(current.x+1)).hashCode();
                if (!bombs.containsKey(subKey)) {
                    subB = new Point(current.x+1, current.y, current);
                    if (subB.equals(goal)) {
                        current = subB;
                        break;
                    }
                    if (!queue.contains(subB)) queue.add(subB);
                }
            }
            if (current.x > 0) {
                subKey = (Integer.toString(current.y) + "," + Integer.toString(current.x-1)).hashCode();
                if (!bombs.containsKey(subKey)) {
                    subB = new Point(current.x-1, current.y, current);
                    if (subB.equals(goal)) {
                        current = subB;
                        break;
                    }
                    if (!queue.contains(subB)) queue.add(subB);
                }
            }
            if (current.y < size) {
                subKey = (Integer.toString(current.y+1) + "," + Integer.toString(current.x)).hashCode();
                if (!bombs.containsKey(subKey)) {
                    subB = new Point(current.x, current.y+1, current);
                    if (subB.equals(goal)) {
                        current = subB;
                        break;
                    }
                    if (!queue.contains(subB)) queue.add(subB);
                }
            }
            if (current.y > 0) {
                subKey = (Integer.toString(current.y-1) + "," + Integer.toString(current.x)).hashCode();
                if (!bombs.containsKey(subKey)) {
                    subB = new Point(current.x, current.y-1, current);
                    if (subB.equals(goal)) {
                        current = subB;
                        break;
                    }
                    if (!queue.contains(subB)) queue.add(subB);
                }
            }

            // Mark the point as seen
            current.seen = true;
        }
        return current;
    }

    /**
     * Main solve function that is called by the testing class.
     *
     * Here we setup the problem:
     * - Get the start & goal points
     * - Build the bombs/walls/obstacles list
     * - Call the solving function
     *
     * @param data
     * @return a ArrayList<String> answer
     */
	public static ArrayList<String> solve(ArrayList<String> data) {

        HashMap<Integer, Point> bombs = new HashMap<>();

        // First you get the size of grid
        int size = Integer.parseInt(data.get(0));

        // Second you get starting point
        Point start = new Point(data.get(1));

        // Third you get the goal point
        Point goal = new Point(data.get(2));

        // All the following points are bombs
        for (int i = 3; i < data.size(); i++) {
            Point bomb = new Point(data.get(i));
            bombs.put(bomb.hashCode(), bomb);
        }

        long startTime = System.nanoTime();
        // Build the path and return the final point
        Point answer = buildMapFromStart(start, goal, size, bombs);
        long endTime = System.nanoTime();

        System.out.println("Grid size: " + Integer.toString(size) + " took " + Integer.toString((int)TimeUnit.NANOSECONDS.toMillis((endTime - startTime))) + " ms");

        // Build the response string
        return buildResponseFromPoint(answer);
	}

    private static ArrayList<String> buildResponseFromPoint(Point p) {

        // The recover the shortest path by going backward from the goal
        String path = "";
        while (p.parent != null) {
            path = p.directionsTo(p.parent) + path;
            p = p.parent;
        }
        ArrayList<String> result = new ArrayList<String>();
        result.add(path);
        return result;
    }
}
