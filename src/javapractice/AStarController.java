package javapractice;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

/**
 * @author Администратор
 */
public class AStarController implements Initializable {

    Maze maze = null;
    Maze mazeSnapshot = null;
    ArrayList<MazeNode> path = null;
    ArrayList<MazeNode> activeSet = null;
    ArrayList<ArrayList<MazeNode>> openSetsSet = null;
    ArrayList<ArrayList<MazeNode>> visitedSetsSet = null;
    MazeNode startNode = null;
    MazeNode endNode = null;
    int mazeSize = 10;
    int cellSize = 900 / mazeSize;

    @FXML
    private Label lblStepNumber;
    @FXML
    private GridPane gridPane;
    @FXML
    private ComboBox cmbSizes;
    @FXML
    private Slider sliderMazeStep;
    @FXML
    private SplitPane splitPane;
    @FXML
    private AnchorPane leftAnchorPane;
    @FXML
    private Label label;
    @FXML
    private Button btnChangeGridSize;
    @FXML
    private Button btnRandomMaze;
    @FXML
    private Button btnCleanMaze;
    @FXML
    private Button btnBeginSearch;
    @FXML
    private AnchorPane rightAnchorPane;

    @FXML
    private void changeGridSize(ActionEvent event) {
        mazeSize = Integer.parseInt(cmbSizes.getValue().toString());
        cellSize = 900 / mazeSize;
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        for (int i = 0; i < mazeSize; i++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(cellSize));
            gridPane.getRowConstraints().add(new RowConstraints(cellSize));
        }

        maze = new Maze(mazeSize, cellSize);
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                installNodeListener(maze.get(i, j));
                gridPane.add(maze.get(i, j), i, j);
            }
        }
        System.out.println("You clicked (changeGridSize)");
    }

    @FXML
    private void createRandomMaze(ActionEvent event) {
        clearMaze();
        Random rand = new Random();
        int seed = Integer.parseInt(cmbSizes.getValue().toString());
        int[] seedPos = new int[seed];
        int numUpdates = seed * 2;

        for (int i = 0; i < seedPos.length; i++) {
            seedPos[i] = Math.abs(rand.nextInt() % (mazeSize * mazeSize));
            maze.get(seedPos[i] % mazeSize, seedPos[i] / mazeSize).setWall();
        }

        for (int i = 0; i < numUpdates; i++) {
            for (int j = 0; j < seedPos.length; j++) {
                switch (Math.abs(rand.nextInt()) % 4) {
                    case 0:
                        seedPos[j] -= mazeSize;
                        break;
                    case 1:
                        seedPos[j]++;
                        break;
                    case 2:
                        seedPos[j] += mazeSize;
                        break;
                    case 3:
                        seedPos[j]--;
                        break;
                }

                if (!(seedPos[j] < 0 || seedPos[j] >= (mazeSize * mazeSize))) {
                    maze.get(seedPos[j] % mazeSize, seedPos[j] / mazeSize).setWall();
                }
            }
        }
    }

    @FXML
    private void clearMaze() {
        endNode = null;
        startNode = null;
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                maze.get(i, j).reset();
            }
        }
    }

    @FXML
    private void beginSearch(ActionEvent event) {
        try {
            mazeSnapshot = (Maze) maze.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("CloneNotSupportedException");
        }

        openSetsSet = new ArrayList<>();
        openSetsSet.clear();
        visitedSetsSet = new ArrayList<>();
        visitedSetsSet.clear();
        activeSet = new ArrayList<>();
        activeSet.clear();
        path = AStarSolver.solve(maze, startNode, endNode, activeSet, visitedSetsSet, openSetsSet);

        sliderMazeStep.setMax(activeSet.size() - 1);
        sliderMazeStep.setValue(0);
        switchMazeStep(0);
    }

    private void resetSearch(ActionEvent event) {
        sliderMazeStep.setValue(0);
        switchMazeStep(0);
    }

    private void switchMazeStep(int step) {
        MazeNode node;
        try {
            int mSize = maze.getMazeSize();
            for (int i = 0; i < mSize; i++) {
                for (int j = 0; j < mSize; j++) {
                    maze.get(j, i).copyState(mazeSnapshot.get(j, i));
                }
            }

            if (openSetsSet != null) {
                openSetsSet.get(step).forEach((openSetNode) -> {
                    int columnIndex = openSetNode.getColumnIndex();
                    int rowIndex = openSetNode.getRowIndex();

                    maze.get(rowIndex, columnIndex).setColor(Color.CADETBLUE);
                });
            }

            if (visitedSetsSet != null) {
                visitedSetsSet.get(step).forEach((visitedNode) -> {
                    int columnIndex = visitedNode.getColumnIndex();
                    int rowIndex = visitedNode.getRowIndex();

                    maze.get(rowIndex, columnIndex).setColor(Color.DARKBLUE);
                });
            }

            if (sliderMazeStep.getMax() != step) {
                node = activeSet.get(step);
                int columnIndex = node.getColumnIndex();
                int rowIndex = node.getRowIndex();
                maze.get(rowIndex, columnIndex).setColor(Color.CHARTREUSE);
            } else {
                if (path != null) {
                    path.forEach((pathNode) -> {
                        int columnIndex = pathNode.getColumnIndex();
                        int rowIndex = pathNode.getRowIndex();

                        maze.get(rowIndex, columnIndex).setColor(Color.CHARTREUSE);
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void installNodeListener(MazeNode node) {
        node.setOnMousePressed((MouseEvent mouseEvent) -> {
            if (mouseEvent.isShiftDown()) {
                if (endNode != null) {
                    endNode.reset();
                }
                endNode = node;
                node.setExit(true);
            } else {
                if (mouseEvent.isControlDown()) {
                    if (startNode != null) {
                        startNode.reset();
                    }
                    startNode = node;
                    node.setEntrance(true);
                } else {
                    node.setWall();
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbSizes.getItems().addAll("10", "15", "30", "45", "60", "90");
        cmbSizes.setValue(cmbSizes.getItems().get(0));
        changeGridSize(null);

        sliderMazeStep.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            sliderMazeStep.setValue(new_val.intValue());
            switchMazeStep(new_val.intValue());
            lblStepNumber.setText("" + new_val.intValue());
        });
    }
}
