/*
 * This project is fully written by Alexander Bychkov (@sign5).
 */
package javapractice;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author sign5
 */
public class Maze implements Cloneable {

    private ArrayList<ArrayList<MazeNode>> maze;
    private int mazeSize;
    private int cellSize;

    Maze(int mazeSize, int cellSize) {
        maze = new ArrayList<>();
        this.mazeSize = mazeSize;
        this.cellSize = cellSize;
        for (int i = 0; i < mazeSize; i++) {
            ArrayList<MazeNode> row = new ArrayList(mazeSize);
            for (int j = 0; j < mazeSize; j++) {
                row.add(new MazeNode(i, j, i * mazeSize + j, cellSize, cellSize));
            }
            maze.add(row);
        }
    }

    Maze(Maze otherMaze) {
        maze = new ArrayList<>();
        this.mazeSize = otherMaze.mazeSize;
        this.cellSize = otherMaze.cellSize;
        for (int i = 0; i < this.mazeSize; i++) {
            ArrayList<MazeNode> row = new ArrayList(this.mazeSize);
            for (int j = 0; j < this.mazeSize; j++) {
                try {
                    row.add(otherMaze.get(i, j).clone());
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(Maze.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.maze.add(row);
        }
    }

    public MazeNode get(int i, int j) {
        if (i < mazeSize && j < mazeSize) {
            return maze.get(i).get(j);
        } else {
            return null;
        }
    }

    public int getMazeSize() {
        return mazeSize;
    }

    public int getCellSize() {
        return cellSize;
    }

    @Override
    public Maze clone() throws CloneNotSupportedException {
        super.clone();
        return new Maze(this);
    }
}
