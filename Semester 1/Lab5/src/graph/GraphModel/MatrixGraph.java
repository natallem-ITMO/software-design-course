package graph.GraphModel;

import graph.DrawAPI.DrawingApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MatrixGraph extends Graph {

    public MatrixGraph(DrawingApi drawApi) {
        super(drawApi, drawApi.getDrawingAreaWidth() / 2, drawApi.getDrawingAreaHeight() / 2);
    }

    @Override
    public void initializeGraph() throws IOException {
        System.out.println("Write number of vertices:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        numberOfVertexes = Integer.parseInt(reader.readLine());
        System.out.println("Write matrix:");
        for (int i = 0; i < numberOfVertexes; i++) {
            String[] line = reader.readLine().split(" ");
            if (line.length != numberOfVertexes) {
                throw new IllegalArgumentException("Incorrect number of data in matrix in line " + (i + 1));
            }
            int finalI = i;
            List<List<Integer>> new_edges = IntStream.range(0, numberOfVertexes).filter(j -> {
                if (!line[j].equals("1") && !line[j].equals("0")) {
                    throw new IllegalArgumentException("Incorrect number of data in matrix in line " + (finalI + 1));
                }
                return line[j].equals("1");
            }).mapToObj(j -> Arrays.asList(finalI, j)).collect(Collectors.toList());
            edges.addAll(new_edges);
        }
    }

}