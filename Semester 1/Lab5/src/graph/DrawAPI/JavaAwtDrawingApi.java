package graph.DrawAPI;

import graph.GeometryModels.Circle;
import graph.GeometryModels.Line;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.*;
import java.util.List;

public class JavaAwtDrawingApi extends Frame implements DrawingApi {

    private static int width;
    private static int height;

    private static final List<Circle> circleList = new ArrayList<>();
    private static final List<Line> lineList = new ArrayList<>();

    public JavaAwtDrawingApi(int width, int height) {
        setTitle("Draw graph");
        JavaAwtDrawingApi.width = width;
        JavaAwtDrawingApi.height = height;
        setVisible(false);
        setSize(width, height);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }

    @Override
    public int getDrawingAreaWidth() {
        return width;
    }

    @Override
    public int getDrawingAreaHeight() {
        return height;
    }

    @Override
    public void drawCircle(int x, int y, int r) {
        circleList.add(new Circle(x, y, r));
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        lineList.add(new Line(x1, x2, y1, y2));
    }

    @Override
    public void finishDrawing() {
        repaint();
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        circleList.forEach(circle -> {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(Color.PINK);
                    g2.fill(new Ellipse2D.Double(circle.getX(), circle.getY(), circle.getR(), circle.getR()));
                }
        );
        lineList.forEach(line ->
        {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.DARK_GRAY);
            g2.draw(new Line2D.Double(line.getX1(), line.getY1(), line.getX2(), line.getY2()));
        });
    }
}
