/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javapractice;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Администратор
 */
public class AStarSolver {

    private static MazeNode end;
    private static double[][] F;
    private static double[][] C;

    public static Comparator<MazeNode> mazeNodeComaparator = new Comparator<MazeNode>() {

        @Override
        public int compare(MazeNode node1, MazeNode node2) {
            double precision = 1.0e-10;

            int row1 = node1.getRowIndex();
            int row2 = node2.getRowIndex();
            int col1 = node1.getColumnIndex();
            int col2 = node2.getColumnIndex();
            if ((heuristicFunction(node1, end) + C[col1][row1] - heuristicFunction(node2, end) - C[col2][row2]) > precision) {
                return 1;
            } else {
                if ((heuristicFunction(node2, end) + C[col2][row2] - heuristicFunction(node1, end) - C[col1][row1]) > precision) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    };
//    private static Comparator<MazeNode> mazeSort = new MazeSort<MazeNode>();

    public static ArrayList<MazeNode> solve(Maze maze, MazeNode startNode, MazeNode endNode,
            ArrayList<MazeNode> activeSet, ArrayList<ArrayList<MazeNode>> visitedSetsSet, ArrayList<ArrayList<MazeNode>> openSetsSet) {
        if (maze != null && startNode != null && endNode != null) {
            end = endNode;
            boolean goalReached = false;
            int size = maze.getMazeSize();
            F = new double[size][size];
            C = new double[size][size];
            double tmpCost;

            ArrayList<MazeNode> openSet = new ArrayList<>();
            ArrayList<MazeNode> closedSet = new ArrayList<>();
            ArrayList<MazeNode> neighbours;
            MazeNode[][] fromSet = new MazeNode[size][size];

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    C[i][j] = Double.MAX_VALUE;
                    fromSet[i][j] = null;
                }
            }

            MazeNode active = startNode;
            int curRow = active.getRowIndex();
            int curCol = active.getColumnIndex();
            openSet.add(active);
            activeSet.add(active);

            openSetsSet.add(new ArrayList<MazeNode>());
            visitedSetsSet.add(new ArrayList<MazeNode>());

            C[curCol][curRow] = 0;
            F[curCol][curRow] = C[curCol][curRow] + heuristicFunction(startNode, active);

            while (!openSet.isEmpty()) {
                boolean better_result = false;
                openSet.sort(mazeNodeComaparator);
                active = openSet.remove(0);
                closedSet.add(active);
                active.setVisited(true);
                activeSet.add(active);

                if (endNode == active) {
                    goalReached = true;
                    openSetsSet.add(copySet(openSet));
                    visitedSetsSet.add(copySet(closedSet));
                    break;
                }

                curRow = active.getRowIndex();
                curCol = active.getColumnIndex();

                neighbours = getNeighbours(maze, active);

                for (MazeNode neighbour : neighbours) {
                    if (closedSet.contains(neighbour)) {
                        continue;
                    }
                    int column = neighbour.getColumnIndex();
                    int row = neighbour.getRowIndex();
                    if (!openSet.contains(neighbour) && !neighbour.hasBeenVisited() && !neighbour.isWall()) {
                        openSet.add(neighbour);
                    }

                    tmpCost = C[curCol][curRow] + neighbour.getCost();

                    if (!openSet.contains(neighbour)) {
                        better_result = true;
                    } else {
                        better_result = tmpCost < C[column][row];
                    }

                    if (better_result) {
                        fromSet[column][row] = active;
                        C[column][row] = tmpCost;
                        F[column][row] = C[column][row] + heuristicFunction(neighbour, endNode);
                        if (!openSet.contains(neighbour)) {
                            openSet.add(neighbour);
                        }
                    }
                }
                openSetsSet.add(copySet(openSet));
                visitedSetsSet.add(copySet(closedSet));
            }

            int a = 0;
            if (goalReached) {
                return reconstructPath(fromSet, startNode, endNode);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static double heuristicFunction(MazeNode activeNode, MazeNode endNode) {
        double dx = Math.pow(endNode.getColumnIndex() - activeNode.getColumnIndex(), 2);
        double dy = Math.pow(endNode.getRowIndex() - activeNode.getRowIndex(), 2);
        return Math.sqrt(dx + dy);
//        double dx = Math.abs(endNode.getColumnIndex() - activeNode.getColumnIndex());
//        double dy = Math.abs(endNode.getRowIndex() - activeNode.getRowIndex());
//        return (dx + dy);

        // return Math.max(cell.x - end.x, cell.y - end.y);
        // * CHOOSE
        // 1) return Math.abs(cell.x-end.x)-Math.abs(cell.y-end.y); //good
        // return Math.max(cell.x-end.x,cell.y-end.y ); //also good (mby
        // greatest)
        // 3) return
        // return (int)
        // Math.sqrt(((cell.x-end.x)*(cell.x-end.x)+(cell.y-end.y)*(cell.y-end.y)));
        // meh
    }

    private static ArrayList<MazeNode> getNeighbours(Maze maze, MazeNode node) {
        int curColumn = node.getColumnIndex();
        int curRow = node.getRowIndex();
        int mazeSize = maze.getMazeSize();
        ArrayList<MazeNode> neighbours = new ArrayList<MazeNode>();
        if (curColumn < (mazeSize - 1)) {
            if (!maze.get(curRow, curColumn + 1).isWall() && !maze.get(curRow, curColumn + 1).hasBeenVisited()) {
                neighbours.add(maze.get(curRow, curColumn + 1));
            }
        }

        if (curRow < (mazeSize - 1)) {
            if (!maze.get(curRow + 1, curColumn).isWall() && !maze.get(curRow + 1, curColumn).hasBeenVisited()) {
                neighbours.add(maze.get(curRow + 1, curColumn));
            }
        }

        if (curColumn > 0) {
            if (!maze.get(curRow, curColumn - 1).isWall() && !maze.get(curRow, curColumn - 1).hasBeenVisited()) {
                neighbours.add(maze.get(curRow, curColumn - 1));
            }
        }

        if (curRow > 0) {
            if (!maze.get(curRow - 1, curColumn).isWall() && !maze.get(curRow - 1, curColumn).hasBeenVisited()) {
                neighbours.add(maze.get(curRow - 1, curColumn));
            }
        }
        return neighbours;
    }

    private static ArrayList<MazeNode> reconstructPath(MazeNode[][] from, MazeNode startNode, MazeNode endNode) {
        ArrayList<MazeNode> path = new ArrayList<>();

        MazeNode curNode = endNode;
        path.add(curNode);
        while (curNode != startNode) {
            curNode = from[curNode.getColumnIndex()][curNode.getRowIndex()];
            path.add(curNode);
        }
        Collections.reverse(path);
        return path;
    }

    private static ArrayList<MazeNode> copySet(ArrayList<MazeNode> set) {
        ArrayList<MazeNode> newSet = new ArrayList<MazeNode>();
        for (MazeNode node : set) {
            newSet.add(node);
        }
        return newSet;
    }
}
