package graph.GraphModel;

import graph.DrawAPI.DrawingApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EdgeListGraph extends Graph {

    public EdgeListGraph(DrawingApi drawApi) {
        super(drawApi, drawApi.getDrawingAreaWidth() / 2, drawApi.getDrawingAreaHeight() / 2);
    }

    @Override
    public void initializeGraph() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Write number of vertexes:");
        numberOfVertexes = Integer.parseInt(reader.readLine());
        System.out.println("Write number of edges:");
        int k = Integer.parseInt(reader.readLine());
        System.out.println("Write list of edges:");
        String[] parts;
        for (int i = 0; i < k; i++) {
            parts = reader.readLine().split(" ");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Expected two numbers for edge");
            }
            List<Integer> edge = Arrays.stream(parts).map(str -> Integer.parseInt(str) - 1
            ).filter(x -> {
                if (x >= numberOfVertexes) {
                    throw new IllegalArgumentException("Vertex " + (x + 1) + " is out of range.");
                }
                return true;
            }).collect(Collectors.toList());
            edges.add(edge);
        }
    }
}

