
/**
 * Three different methods to check for the shortest distance to travel between cities.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
public class PA5Main {
    /**
     * Loops through each city to find the closest neighboring city and then
     * takes the chosen city out of the list and loops through it again till
     * there are no cities left to check.
     * 
     * @param trip03
     * @param graph
     * @return trip03
     */
    public static Trip mine(Trip trip03, DGraph graph) {
        trip03.chooseNextCity(1); // First city
        int currCity = 1; // Chosen city
        for (int k = 2; k < graph.getNumNodes(); k++) {
            int num = 0; // Index of soon to be chosen city
            double min = 99999; // Longest distance
            for (Integer i : trip03.citiesLeft()) { // Loops through the cities
                                                    // left
                if (trip03.isCityAvailable(i)
                        && (graph.getWeight(currCity, i) < min)) {
                    min = graph.getWeight(currCity, i); // New distance
                    num = i;
                }
            }
            currCity = num; // New chosen city
            trip03.chooseNextCity(num); // Visited city
            if (trip03.citiesLeft().isEmpty()) {
                return trip03;
            }
        }
        return trip03;
    }

    /**
     * Loops through each city to find the closest neighboring city and then
     * finds the closest city and adds to the 'cities visited' and through to
     * it's neighboring cities to find the closest one.
     * 
     * @param trip02
     * @param graph
     * @return trip02
     */
    public static Trip heuristic(Trip trip02, DGraph graph) {
        trip02.chooseNextCity(1); // First city
        int currCity = 1; // Chosen city
        for (int k = 2; k < graph.getNumNodes(); k++) {
            int num = 0; // Index of soon to be chosen city
            double min = 99999; // Longest distance
            for (Integer i : graph.getNeighbors(currCity)) { // Loops through
                                                             // all its
                                                             // neighbors
                if (trip02.isCityAvailable(i) && (graph.getWeight(currCity,
                        i) < min)) {
                    min = graph.getWeight(currCity, i); // New distance
                    num = i;
                }
            }
            currCity = num; // New chosen city
            trip02.chooseNextCity(num); // Visited city
            if (trip02.citiesLeft().isEmpty()) {
                return trip02;
            }
        }
        return trip02;
    }

    /**
     * Recurses through each city and picks the one with the lowest cost route.
     * 
     * @param graph
     * @param trip
     * @param minTrip
     * @return minTrip
     */
    public static Trip enumerate(DGraph graph, Trip trip, Trip minTrip) {
        if (trip.isPossible(graph) || trip.citiesLeft().size() == 0) {
            if (trip.tripCost(graph) < minTrip.tripCost(graph)) {
                minTrip.copyOtherIntoSelf(trip);
            }
        }
        if (trip.tripCost(graph) < minTrip.tripCost(graph)) { // Different
                                                              // combinations of
                                                              // cities
            for (Integer i : trip.citiesLeft()) {
                trip.chooseNextCity(i);
                enumerate(graph, trip, minTrip);
                trip.unchooseLastCity();
                }
            }
        return minTrip;
        }
    public static void main(String[] args) {
        try {
            int row = 0;
            int col = 0;
            String[] list;
            // mtx file from the command line
            Scanner input = new Scanner(new File(args[0]));
            // Gets how many rows and columns are in the file
            while (input.hasNextLine()) {
                String line = input.nextLine();
                if (!line.startsWith("%")) {
                    row += 1;
                    list = line.split(" ");
                    col = list.length;
                }
            }
            // To store every unique city
            Set<Integer> cities = new HashSet<Integer>();
            // Graph with the perfect amount of edges from each city
            DGraph graph = new DGraph(Math.max(row, col));
            // mtx file from the command line
            Scanner in02 = new Scanner(new File(args[0]));
            // Adds the appropriate data into the graph
            while (in02.hasNextLine()) {
                String line02 = in02.nextLine();
                if (!line02.startsWith("%")) {
                    String[] list02 = line02.split("\\s+");
                    int x = Integer.parseInt(list02[0]);
                    int y = Integer.parseInt(list02[1]);
                    cities.add(x);
                    cities.add(y);
                    Double w = Double.parseDouble(list02[2]);
                    if (x != y && w != 0) {
                        graph.addEdge(x, y, w);
                    }
                }
            }
            // Different trips from each method
            // Recursion
            Trip trip = new Trip(cities.size());
            trip.chooseNextCity(1);
            Trip minTrip = new Trip(cities.size());
            // Heuristic
            Trip trip02 = new Trip(cities.size());
            // Mine
            Trip trip03 = new Trip(cities.size());
            // Based on what was in the command line
            // THe program prints out the appropriate data
            for (int j = 0; j < args.length; j++) {
                if (args[j].equals("HEURISTIC")) {
                    heuristic(trip02, graph);
                    System.out.println(trip02.toString(graph));
                } else if (args[j].equals("BACKTRACK")) {
                    enumerate(graph, trip, minTrip);
                    System.out.println(minTrip.toString(graph));
                } else if (args[j].equals("MINE")) {
                    mine(trip03, graph);
                    System.out.println(trip03.toString(graph));
                } else if (args[j].equals("TIME")) {
                    long startTime = System.nanoTime();
                    trip = heuristic(trip02, graph);
                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime) / 1000000;
                    System.out.println("heuristic: cost = " + trip.tripCost(graph) + ", "
                            + duration + " milliseconds");
                    long start = System.nanoTime();
                    trip = mine(trip03, graph);
                    long end = System.nanoTime();
                    long time = (end - start) / 1000000;
                    System.out.println("mine: cost = " + trip.tripCost(graph)
                            + ", " + time + " milliseconds");
                    long star = System.nanoTime();
                    trip = enumerate(graph, trip, minTrip);
                    long en = System.nanoTime();
                    long dura = (en - star) / 1000000;
                    System.out
                            .println("backtrack: cost = " + trip.tripCost(graph)
                                    + ", " + dura + " milliseconds");
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}