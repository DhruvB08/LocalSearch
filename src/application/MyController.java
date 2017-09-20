package application;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	
	@FXML TextArea PuzzleBox;
	@FXML MenuButton PuzzleSize;
	@FXML TextField numClimbs;
	@FXML TextField numIterations;
	@FXML TextField pValue;
	@FXML TextField numIterationsA;
	@FXML TextField initTemp;
	@FXML TextField decayRate;
	@FXML TextField ComputeDisplay;
	@FXML TextField ValueDisplay;
	
	private int puzzleSize;
	private int[][] puzzle;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	
	public void setPuzzleSize(int size) {
		puzzleSize = size;
		PuzzleSize.setText(Integer.toString(size));
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
	
	//onaction events for menuitems to select size
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
	
	//Clicking create puzzle button
	public void createPuzzle(ActionEvent event) {
		long startTime = Calendar.getInstance().getTimeInMillis();
		
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
		showComputeTime(startTime);
		showValue(puzzleMatrix);
	}
	
	//private method to create a new puzzle given the dimensions
	private int[][] createNewPuzzle(int size) {
		int n = getPuzzleSize();
		int[][] puzzle = new int[n][n];
		
		Random random = new Random();
		int minNum = 1, maxNum;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				maxNum = Math.max(n - i, n - j) - 1;
				if (i == n - 1 && j == n - 1) {
					puzzle[i][j] = 0;
				} else {
					puzzle[i][j] = random.nextInt(maxNum - minNum + 1) + minNum;
				}
			}
		}
		
		return puzzle;
	}
	
	//private method for printing the puzzle onto GUI
	private void showPuzzle(int[][] puzzleMatrix) {
		PuzzleBox.setText("");
		for (int i = 0; i < puzzleMatrix.length; i++) {
			for (int j = 0; j < puzzleMatrix[0].length; j++) {
				PuzzleBox.appendText(Integer.toString(puzzleMatrix[i][j]) + "\t");
			}
			PuzzleBox.appendText("\n");
		}
	}
	
	//Clicking the show puzzle button
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
	
	//Clicking the show solution button
	public void showSolution(ActionEvent event) {
		
	}
	
	//Clicking the Hill Climb button
	/* Hill Climb button
	 * possible input: number of climbs
	 * possible input: number of climbs, iterations per climb
	 * possible input: number of climbs, p value
	 */
	public void doHillClimb(ActionEvent event) {
		long startTime = Calendar.getInstance().getTimeInMillis();
		boolean randomRestarts = false;
		boolean randomWalk = false;
		
		int numberOfClimbs = 0;
		try {
			numberOfClimbs = Integer.parseInt(numClimbs.getText());
		} catch (NumberFormatException ex) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Number of Hill climbs is not an integer");
			alert.setContentText("You must input an integer for number of climbs.");
			alert.showAndWait();
			return;
		}

		int numberOfIterations = 0;
		if (!numIterations.getText().equals("")) {
			try {
				numberOfIterations = Integer.parseInt(numIterations.getText());
			} catch (NumberFormatException ex) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Number of iterations is not valid");
				alert.setContentText("You must input an integer for number of iterations, or leave the field blank.");
				alert.showAndWait();
				return;
			}
			
			randomRestarts = true;
		}
		
		double pValueInserted = 0;
		if (!pValue.getText().equals("")) {
			try {
				pValueInserted = Double.parseDouble(pValue.getText());
			} catch (NumberFormatException ex) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Number of Hill climbs is not a proper value");
				alert.setContentText("You must input a proper value between 0 and 1, or leave the field blank.");
				alert.showAndWait();
				return;
			}
			
			if (pValueInserted < 0 || pValueInserted > 1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid Bounds");
				alert.setContentText("The p-value is a probability, and must be between 0 and 1");
				alert.showAndWait();
				return;
			}
			
			randomWalk = true;
		}
		
		int[][] resultPuzzle = null;
		if (randomRestarts && randomWalk) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Too many arguements");
			alert.setContentText("You must either input a p-value for random walk, or number of iterations for random restarts, but not both.");
			alert.showAndWait();
			return;
		} else if (randomRestarts) {
			resultPuzzle = withRandomRestarts(numberOfClimbs, numberOfIterations);
		} else if (randomWalk) {
			resultPuzzle = withRandomWalk(numberOfClimbs, pValueInserted);
		} else {
			resultPuzzle = pureHillClimb(numberOfClimbs);
		}
		
		showComputeTime(startTime);
		showValue(resultPuzzle);
		setPuzzle(resultPuzzle);
		showPuzzle(resultPuzzle);
	}
	
	//Pure Hill Climbing, given the number of climbs to do
	private int[][] pureHillClimb(int numClimbs) {
		int[][] currPuzzle = getPuzzle();
		int[][] newPuzzle = currPuzzle;
		
		Random random = new Random();
		int n = getPuzzleSize();
		for (int i = 0; i < numClimbs; i++) {
			int maxNum = n - 1, minNum = 0;
			int x = random.nextInt(maxNum - minNum + 1) + minNum;
			int y = random.nextInt(maxNum - minNum + 1) + minNum;
			while (x == maxNum && y == maxNum) {
				x = random.nextInt(maxNum - minNum + 1) + minNum;
				y = random.nextInt(maxNum - minNum + 1) + minNum;
			}
			
			maxNum = Math.max(n - x, n - y) - 1;
			minNum = 1;
			int newVal = random.nextInt(maxNum - minNum + 1) + minNum;
			
			newPuzzle[x][y] = newVal;
			if (valueFunction(newPuzzle) >= valueFunction(currPuzzle)) {
				currPuzzle = newPuzzle;
			}
		}
		
		return currPuzzle;
	}
	
	//Random Walk, given the number of climbs and probability
	private int[][] withRandomWalk(int numClimbs, double prob) {
		int[][] currPuzzle = getPuzzle();
		int[][] newPuzzle = currPuzzle;
		
		Random random = new Random();
		int n = getPuzzleSize();
		for (int i = 0; i < numClimbs; i++) {
			int maxNum = n - 1, minNum = 0;
			int x = random.nextInt(maxNum - minNum + 1) + minNum;
			int y = random.nextInt(maxNum - minNum + 1) + minNum;
			while (x == maxNum && y == maxNum) {
				x = random.nextInt(maxNum - minNum + 1) + minNum;
				y = random.nextInt(maxNum - minNum + 1) + minNum;
			}
			
			maxNum = Math.max(n - x, n - y) - 1;
			minNum = 1;
			int newVal = random.nextInt(maxNum - minNum + 1) + minNum;
			
			newPuzzle[x][y] = newVal;
			if (valueFunction(newPuzzle) >= valueFunction(currPuzzle)) {
				currPuzzle = newPuzzle;
			} else if (random.nextDouble() <= prob) {
				currPuzzle = newPuzzle;
			}
		}
		
		return currPuzzle;
	}
	
	//Random Restarts, given the number of climbs and iterations per climb
	private int[][] withRandomRestarts(int numClimbs, int itsPerClimb) {
		int[][] currPuzzle = getPuzzle();
		int[][] newPuzzle = currPuzzle;
		int[][] bestPuzzle = currPuzzle;
		
		Random random = new Random();
		int n = getPuzzleSize();
		for (int j = 0; j < numClimbs; j++) {
			currPuzzle = createNewPuzzle(n);
			newPuzzle = currPuzzle;
			for (int i = 0; i < itsPerClimb; i++) {
				int maxNum = n - 1, minNum = 0;
				int x = random.nextInt(maxNum - minNum + 1) + minNum;
				int y = random.nextInt(maxNum - minNum + 1) + minNum;
				while (x == maxNum && y == maxNum) {
					x = random.nextInt(maxNum - minNum + 1) + minNum;
					y = random.nextInt(maxNum - minNum + 1) + minNum;
				}
				
				maxNum = Math.max(n - x, n - y) - 1;
				minNum = 1;
				int newVal = random.nextInt(maxNum - minNum + 1) + minNum;
				
				newPuzzle[x][y] = newVal;
				if (valueFunction(newPuzzle) >= valueFunction(currPuzzle)) {
					currPuzzle = newPuzzle;
				}
			}
			
			if(valueFunction(currPuzzle) >= valueFunction(bestPuzzle)) {
				bestPuzzle = currPuzzle;
			}
		}
		
		return bestPuzzle;
	}
	
	//Clicking the Simulated Annealing button
	public void doSimAnnealing(ActionEvent event){
		int iterationsA=0;
		try{
			iterationsA = Integer.parseInt(numIterationsA.getText());
		}catch(NumberFormatException ex){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Number of Simulated Annealing Iterations is Not an integer");
			alert.setContentText("You must input an integer for number of Iterations.");
			alert.showAndWait();
		}
		System.out.println(iterationsA);
	
		
		
		
		int initialTemperature=0;
		try{
			initialTemperature = Integer.parseInt(initTemp.getText());
		}catch(NumberFormatException ex){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("initial Temperature is Not an integer");
			alert.setContentText("You must input an integer for initial temperature.");
			alert.showAndWait();
		}
		System.out.println(initialTemperature);
		
		
		
		
		int decay=0;
		try{
			decay = Integer.parseInt(decayRate.getText());
		}catch(NumberFormatException ex){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Decay Rate is Not an integer");
			alert.setContentText("You must input a proper number for decay Rate value.");
			alert.showAndWait();
		}
		System.out.println(decay);
		
	}
	
	private void showComputeTime(long startTime) {
		long currTime = Calendar.getInstance().getTimeInMillis();
		long elapsed = currTime - startTime;
		
		ComputeDisplay.setText(Long.toString(elapsed));
	}
	
	//placeholder for solution algorithm
	private int valueFunction(int[][] puzzle) {
		return 0;
	}
	
	private void showValue(int[][] puzzle) {
		ValueDisplay.setText(Integer.toString(valueFunction(puzzle)));
	}
}
