package com.cgvsu.rasterizationfxapp;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import javax.swing.plaf.synth.ColorType;
import java.util.ArrayList;

import static java.lang.Math.abs;


public class RasterizationController {

    @FXML
    AnchorPane anchorPane;
    ArrayList<Point2D> points = new ArrayList<Point2D>();
    @FXML
    private Canvas canvas;



    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        grid();

        canvas.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                handlePrimaryClick(canvas.getGraphicsContext2D(), event);
            }
        });
    }

    private void handlePrimaryClick(GraphicsContext graphicsContext, MouseEvent event) {
        final Point2D clickPoint = new Point2D(event.getX(), event.getY());


        graphicsContext.setFill(Color.BLACK);
        final int POINT_RADIUS = 6;
        graphicsContext.fillOval(clickPoint.getX() - POINT_RADIUS, clickPoint.getY() - POINT_RADIUS, 2 * POINT_RADIUS, 2 * POINT_RADIUS);

        if (points.size() > 0) {
            final Point2D lastPoint = points.get(points.size() - 1);


            dda(lastPoint.getX(), clickPoint.getX(), lastPoint.getY(), clickPoint.getY());

        }


        points.add(clickPoint);
    }

    private void grid() {
        int n = 10;
        int p = 0;
        int c = 60;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        int len = c * n;
        for (int i = 0; i <= n; i++) {
            gc.strokeLine(0, p, len, p);
            gc.strokeLine(p, 0, p, len);
            p += c;
        }

    }

    private void dda(double x1, double x2, double y1, double y2) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double dx, dy, steps, x, y, k;
        double xc, yc;
        double r = 0;
        double color = 0;
        double colormax = 254;
        double difference;
        dx = x2 - x1;
        dy = y2 - y1;
        if (abs(dx) > abs(dy)) {
            steps = abs(dx);
        } else {
            steps = abs(dy);
        }
        xc = (dx / steps);
        yc = (dy / steps);
        x = x1;
        y = y1;


        double c = steps / colormax;
        double s = colormax / steps;
        double result2 = Math.ceil(c);
        double result1 = Math.floor(s);

         if (steps <= colormax) {
            for (k = 1; k <= steps; k++) {
                x = x + xc;
                y = y + yc;
                r++;
                if (steps < colormax) {
                    color = color + result1;
                    gc.setFill(Color.rgb(255, 0, (int) color));
                    r = 0;
                }
                if (r == result2) {
                    color = color + 1;
                    gc.setFill(Color.rgb(255, 0, (int) color));
                    r = 0;
                }
                gc.fillRect((int) x, (int) y, 8, 8);
            }
        }


        if (steps >= colormax) {
            for (k = 1; k <= steps; k++) {
                x = x + xc;
                y = y + yc;
                r++;
                if (steps <= colormax) {
                    color = color + result1;
                    gc.setFill(Color.rgb(255, 0, (int) color));
                    r = 0;
                }
                if (r == result2) {
                    color = color + 1;
                    gc.setFill(Color.rgb(255, 0, (int) color));
                    r = 0;
                }
                gc.fillRect((int) x, (int) y, 8, 8);
            }
        }


    }
}
