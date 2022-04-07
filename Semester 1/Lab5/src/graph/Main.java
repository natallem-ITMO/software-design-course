package graph;


import graph.DrawAPI.DrawingApi;
import graph.DrawAPI.JavaAwtDrawingApi;
import graph.DrawAPI.JavaFxDrawingApi;
import graph.GraphModel.*;

import java.io.IOException;

public class Main {
    private static final int WIDTH = 1500;
    private static final int HEIGHT = 1500;

    public static void main(String[] args) throws IOException, Exception {
        DrawingApi api;
        if (args[0].equals("JavaFx")) {
            api = new JavaFxDrawingApi(WIDTH, HEIGHT);
        } else if (args[0].equals("JavaAwt")) {
            api = new JavaAwtDrawingApi(WIDTH, HEIGHT);
        } else {
            throw new IllegalArgumentException("Incorrect input arguments. No graphic API.");
        }
        Graph graph;
        if (args[1].equals("Matrix")) {
            graph = new MatrixGraph(api);
        } else if (args[1].equals("EdgeList")) {
            graph = new EdgeListGraph(api);
        } else {
            throw new IllegalArgumentException("Incorrect input arguments. No graph construct method.");
        }
        graph.initializeGraph();
        graph.drawGraph();
    }
}
