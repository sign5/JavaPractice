/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javapractice;

import java.util.Random;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author sign5
 */
public final class MazeNode extends Rectangle implements Cloneable {

    private static Random rnd = new Random(100);
    private boolean nodeIsWall;
    private boolean nodeIsEntrance;
    private boolean nodeIsExit;
    private boolean visited;
    private boolean active;
    private Color color;
    private double cost;
    private double nodeCost;
    private int rowIndex;
    private int columnIndex;
    private int ID;

    public MazeNode(int row, int column, int nodeID, int width, int height) {
        nodeIsWall = false;
        nodeIsEntrance = false;
        nodeIsExit = false;
        visited = false;
        active = false;
        setColor(Color.WHITE);
        cost = 0;
        int tmpCost = rnd.nextInt(100);
        nodeCost = tmpCost;

        String hex = String.format("#%02x%02x%02x", tmpCost, tmpCost, tmpCost);
        this.setStyle("-fx-background-color: " + hex + ";");
        rowIndex = row;
        columnIndex = column;
        ID = nodeID;
        setWidth(width);
        setHeight(height);
    }

    MazeNode(MazeNode other) {
        nodeIsWall = other.nodeIsWall;
        nodeIsEntrance = other.nodeIsEntrance;
        nodeIsExit = other.nodeIsExit;
        visited = other.visited;
        active = other.active;
        setColor(other.color);
        cost = other.cost;
        nodeCost = other.nodeCost;
        rowIndex = other.rowIndex;
        columnIndex = other.columnIndex;
        ID = other.ID;
        setWidth(other.getWidth());
        setHeight(other.getHeight());
    }

    @Override
    public MazeNode clone() throws CloneNotSupportedException {
        super.clone();
        return new MazeNode(this);
    }

    public void reset() {
        nodeIsWall = false;
        nodeIsEntrance = false;
        nodeIsExit = false;
        visited = false;
        active = false;
        setColor(Color.WHITE);
    }

    public int getID() {
        return ID;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double newCost) {
        cost = newCost;
    }

    public double getNodeCost() {
        return nodeCost;
    }

    public void setNodeCost(double newCost) {
        nodeCost = newCost;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColor(Color color) {
        this.color = color;
        this.setFill(color);
        this.setStroke(Color.LIGHTGRAY);

//        int r = (int) (color.getRed() * 255);
//        int g = (int) (color.getGreen() * 255);
//        int b = (int) (color.getBlue() * 255);
//        String hex = String.format("#%02x%02x%02x", r, g, b);
//        this.setStyle("-fx-font: 12 arial; -fx-base: " + hex + ";");
    }

    public Color getColor() {
        return color;
    }

    public void setVisited(boolean value) {
        visited = value;

//        int r = (int) (cost);
//        int g = (int) (color.getGreen() * 255);
//        int b = (int) (color.getBlue() * 255);
//        String hex = String.format("#%02x%02x%02x", cost, cost, cost);
//        this.setStyle("-fx-background-color: " + hex + ";");
        if (value) {
            setColor(Color.DARKCYAN);
        } else {
            setColor(Color.WHITE);
        }

//        try {
//            Thread.sleep(10);
//        } catch (Throwable e) {
//
//        }
    }

    public boolean hasBeenVisited() {
        return visited;
    }

    public void setWall() {
        nodeIsWall = !nodeIsWall;
        nodeIsEntrance = false;
        nodeIsExit = false;
        if (nodeIsWall) {
            setColor(Color.BLACK);
        } else {
            setColor(Color.WHITE);
        }
    }

    public boolean isWall() {
        return nodeIsWall;
    }

    public void setEntrance(boolean isEntrance) {
        nodeIsWall = false;
        nodeIsEntrance = isEntrance;
        nodeIsExit = false;
        if (isEntrance) {
            setColor(Color.LIGHTBLUE);
        }
    }

    public boolean isEntrance() {
        return nodeIsEntrance;
    }

    public void setExit(boolean isExit) {
        nodeIsWall = false;
        nodeIsExit = isExit;
        nodeIsEntrance = false;
        if (isExit) {
            setColor(Color.GOLD);
        }
    }

    public boolean isExit() {
        return nodeIsExit;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            setColor(Color.GREENYELLOW);
        } else {
            setColor(Color.WHITE);
        }
    }

    public void copyState(MazeNode other) {
        nodeIsWall = other.nodeIsWall;
        nodeIsEntrance = other.nodeIsEntrance;
        nodeIsExit = other.nodeIsExit;
        visited = other.visited;
        active = other.active;
        setColor(other.color);
        cost = other.cost;
        nodeCost = other.nodeCost;
        rowIndex = other.rowIndex;
        columnIndex = other.columnIndex;
        ID = other.ID;
    }
}
