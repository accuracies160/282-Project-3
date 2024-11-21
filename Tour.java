import java.io.*;
import java.util.*;

public class Tour {
    private static class Edge {
        String to;
        int cost;

        Edge(String to, int cost) {
            this.to = to;
            this.cost = cost;
        }
    }

    private final Map<String, List<Edge>> graph = new HashMap<>();
    private int bestCost = Integer.MAX_VALUE;
    private List<String> bestPath = new ArrayList<>();

    public void quickTour(String start) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("travel.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                graph.computeIfAbsent(parts[0], k -> new ArrayList<>())
                     .add(new Edge(parts[1], Integer.parseInt(parts[2])));
            }
        }

        Set<String> visited = new HashSet<>();
        visited.add(start);
        dfs(start, start, visited, 0, new ArrayList<>(List.of(start)));
        
        System.out.println("Path: " + String.join(", ", bestPath));
        System.out.println("Total Tour Time: " + bestCost);
    }

    private void dfs(String current, String start, Set<String> visited, int cost, List<String> path) {
        if (visited.size() == graph.keySet().size()) {
            for (Edge edge : graph.getOrDefault(current, Collections.emptyList())) {
                if (edge.to.equals(start)) {
                    int totalCost = cost + edge.cost;
                    if (totalCost < bestCost) {
                        bestCost = totalCost;
                        bestPath = new ArrayList<>(path);
                        bestPath.add(start);
                    }
                    return;
                }
            }
        }

        for (Edge edge : graph.getOrDefault(current, Collections.emptyList())) {
            if (!visited.contains(edge.to)) {
                visited.add(edge.to);
                path.add(edge.to);
                dfs(edge.to, start, visited, cost + edge.cost, path);
                path.remove(path.size() - 1);
                visited.remove(edge.to);
            }
        }
    }
}
