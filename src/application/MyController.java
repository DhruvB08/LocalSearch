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
				if (i == puzzleMatrix.length - 1 && j == puzzleMatrix[0].length - 1) {
					PuzzleBox.appendText("G");
				} else {
					PuzzleBox.appendText(Integer.toString(puzzleMatrix[i][j]) + "\t");
				}
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
		if (getPuzzle() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Puzzle Exists");
			alert.setContentText("You must generate a puzzle first then you can show the solution");
			alert.showAndWait();
			return;
		}
		
		PuzzleBox.setText("");
		
		int[][] puzzle = getPuzzle();
		int n = getPuzzleSize();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == 0 && j == 0) {
					PuzzleBox.appendText("0" + "\t");
				} else {
					int val = valueFunction(puzzle, i, j);
					if (val == 0) {
						PuzzleBox.appendText("X" + "\t");
					} else {
						PuzzleBox.appendText(Integer.toString(val) + "\t");
					}
				}
			}
			PuzzleBox.appendText("\n");
		}
	}
	
	//Clicking the Hill Climb button
	/* Hill Climb button
	 * possible input: number of climbs
	 * possible input: number of climbs, iterations per climb
	 * possible input: number of climbs, p value
	 */
	public void doHillClimb(ActionEvent event) {
		if (getPuzzle() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No starting puzzle");
			alert.setContentText("You must generate a strting puzzle first before improving it");
			alert.showAndWait();
			return;
		}
		
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
		int n = currPuzzle.length;
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
		int n = currPuzzle.length;
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
		int n = currPuzzle.length;
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
	/* Simulated Annealing
	 * required inputs: iterations, starting temperature, decay rate
	 */
	public void doSimAnnealing(ActionEvent event){
		if (getPuzzle() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No starting puzzle");
			alert.setContentText("You must generate a strting puzzle first before improving it");
			alert.showAndWait();
			return;
		}
		
		long startTime = Calendar.getInstance().getTimeInMillis();
		
		int numIterations = 0;
		try {
			numIterations = Integer.parseInt(numIterationsA.getText());
		} catch (NumberFormatException ex) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Number of Iterations is not an integer");
			alert.setContentText("You must input an integer for number of Iterations.");
			alert.showAndWait();
			return;
		}
	
		double startTemp = 0;
		try {
			startTemp = Double.parseDouble(initTemp.getText());
		} catch (NumberFormatException ex) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("initial Temperature is not a number");
			alert.setContentText("You must input a number for initial temperature.");
			alert.showAndWait();
			return;
		}
			
		double decay = 0;
		try {
			decay = Double.parseDouble(decayRate.getText());
		} catch (NumberFormatException ex) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Decay Rate is not a number");
			alert.setContentText("You must input a number for the decay rate.");
			alert.showAndWait();
			return;
		}
		
		if (decay < 0 || decay > 1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Decay rate invalid");
			alert.setContentText("Decay rate must be between 0 and 1.");
			alert.showAndWait();
			return;
		}
		
		int[][] currPuzzle = getPuzzle();
		int[][] newPuzzle = currPuzzle;
		
		Random random = new Random();
		int n = currPuzzle.length;
		for (int i = 0; i < numIterations; i++) {
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
			} else {
				double numerator = valueFunction(newPuzzle) - valueFunction(currPuzzle);
				double power = numerator / startTemp;
				double prob = Math.pow(Math.E, power);
				if (random.nextDouble() <= prob) {
					currPuzzle = newPuzzle;
				}
			}
			
			startTemp = startTemp * decay;
		}
		
		showComputeTime(startTime);
		showValue(currPuzzle);
		setPuzzle(currPuzzle);
		showPuzzle(currPuzzle);
	}
	
	private void showComputeTime(long startTime) {
		long currTime = Calendar.getInstance().getTimeInMillis();
		long elapsed = currTime - startTime;
		
		ComputeDisplay.setText(Long.toString(elapsed));
	}
	
	//getting solution at goal cell
	private int valueFunction(int[][] puzzle) {
		int n = getPuzzleSize();
		int sol = valueFunction(puzzle, n - 1, n - 1);
		
		if (sol == 0) {
			int numFails = 0;
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (i == n - 1 && j == n - 1) {
						continue;
					} else if (valueFunction(puzzle, i, j) == 0) {
						numFails++;
					}
				}
			}
			
			return (-1 * numFails);
		}
		
		return sol;
	}
	
	//placeholder for solution algorithm
	//returns 0 when no solution
	private int valueFunction(int[][] puzzle, int index1, int index2) {
		return 0;
	}
	
	private void showValue(int[][] puzzle) {
		ValueDisplay.setText(Integer.toString(valueFunction(puzzle)));
	}
}
