package graph.DrawAPI;


public interface DrawingApi {
    int getDrawingAreaWidth();
    int getDrawingAreaHeight();
    void drawCircle(int x, int y, int r);
    void drawLine(int x1, int y1, int x2, int y2);
    void finishDrawing();
}
