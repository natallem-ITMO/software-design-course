package graph.GraphModel;

import graph.DrawAPI.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract public class Graph {
    private static final int DISTANCE_BETWEEN_VERTEXES = 100;
    private static final int CIRCLE_RADIUS = 50;
    private final int CENTER_X;
    private final int CENTER_Y;

    protected int numberOfVertexes;
    protected List<List<Integer>> edges = new ArrayList<>();

    private final DrawingApi drawApi;

    public Graph(DrawingApi drawApi, int center_x, int center_y) {
        this.drawApi = drawApi;
        this.CENTER_X = center_x;
        this.CENTER_Y = center_y;
    }

    public abstract void initializeGraph() throws IOException;

    public void drawGraph() {
        List<Integer> xs = Arrays.asList(new Integer[numberOfVertexes]);
        List<Integer> ys = Arrays.asList(new Integer[numberOfVertexes]);
        placeVertexes(xs, ys);
        edges.forEach(edge -> {
            int u = edge.get(0);
            int v = edge.get(1);
            drawEdge(xs.get(u), ys.get(u), xs.get(v), ys.get(v));
        });
        drawApi.finishDrawing();
    }

    private void placeVertexes(List<Integer> x, List<Integer> y) {
        int i = 0;
        if (drawCircleInCell(0, 0, i++, x, y)) {
            return;
        }
        for (int l = 1; ; ++l) {
            int dy = -l;
            int dx;
            for (dx = -l; dx <= l; ++dx) {
                if (drawCircleInCell(dx, dy, i++, x, y)) {
                    return;
                }
            }
            dx = l;
            for (dy = -l + 1; dy <= l; ++dy) {
                if (drawCircleInCell(dx, dy, i++, x, y)) {
                    return;
                }
            }
            dy = l;
            for (dx = l - 1; dx >= -l; --dx) {
                if (drawCircleInCell(dx, dy, i++, x, y)) {
                    return;
                }
            }
            dx = -l;
            for (dy = l - 1; dy > -l; --dy) {
                if (drawCircleInCell(dx, dy, i++, x, y)) {
                    return;
                }
            }
        }
    }

    private boolean drawCircleInCell(int dx, int dy,  int i, List<Integer> x, List<Integer> y) {
        if (i >= numberOfVertexes) {
            return true;
        }
        x.set(i, Graph.this.CENTER_X + DISTANCE_BETWEEN_VERTEXES * dx);
        y.set(i, Graph.this.CENTER_Y + DISTANCE_BETWEEN_VERTEXES * dy);
        drawApi.drawCircle(x.get(i), y.get(i), CIRCLE_RADIUS);
        return false;
    }

    private void drawEdge(int x1, int y1, int x2, int y2) {
        drawApi.drawLine(x1 + 20, y1 + 20, x2 + 20, y2 + 20);
    }

}

