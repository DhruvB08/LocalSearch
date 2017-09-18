package application;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class MyController implements Initializable{
	
	@FXML
	TextArea PuzzleBox;
	@FXML
	MenuButton PuzzleSize;
	
	private int puzzleSize;
	private int[][] puzzle;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	
	public void setPuzzleSize(int size) {
		puzzleSize = size;
	}
	
	public int getPuzzleSize() {
		return puzzleSize;
	}
	
	public void setPuzzle(int[][] puzz) {
		puzzle = puzz;
	}
	
	public int[][] getPuzzle() {
		return puzzle;
	}
	
	@FXML
	public void setSize5(ActionEvent event) {
		setPuzzleSize(5);
	}
	public void setSize7(ActionEvent event) {
		setPuzzleSize(7);
	}
	public void setSize9(ActionEvent event) {
		setPuzzleSize(9);
	}
	public void setSize11(ActionEvent event) {
		setPuzzleSize(11);
	}
	
	public void createPuzzle(ActionEvent event) {
		if (getPuzzleSize() == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Size Selected");
			alert.setContentText("You must specify a puzzle size.");
			
			alert.showAndWait();
			return;
		}
		
		int[][] puzzleMatrix = createNewPuzzle(puzzleSize);
		setPuzzle(puzzleMatrix);
		showPuzzle(puzzleMatrix);
	}
	
	private int[][] createNewPuzzle(int size){
		int n = getPuzzleSize();
		int[][] puzzle = new int[n][n];
		
		Random random = new Random();
		int minNum = 1, maxNum;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				maxNum = Math.max(n - i, n - j) - 1;
				puzzle[i][j] = random.nextInt(maxNum - minNum + 1) + minNum;
			}
		}
		
		return puzzle;
	}
	
	private void showPuzzle(int[][] puzzleMatrix) {
		for (int i = 0; i < puzzleMatrix.length; i++) {
			for (int j = 0; j < puzzleMatrix[0].length; j++) {
				PuzzleBox.appendText(Integer.toString(puzzleMatrix[i][j]));
			}
			PuzzleBox.appendText("\n");
		}
	}
	
	public void showPuzzle(ActionEvent event) {
		if (getPuzzle() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Puzzle Exists");
			alert.setContentText("You must generate a puzzle first then show it.");
			
			alert.showAndWait();
			return;
		}
		
		showPuzzle(getPuzzle());
	}
}
