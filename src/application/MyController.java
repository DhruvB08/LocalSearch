package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import javafx.scene.control.TextInputDialog;
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
		int n = size;
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
		int n = puzzle.length;
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
	
	public void doBFS(int row, int column,int[][] solutionArray,int count, int[][] puzzle){
		int n = puzzle.length;
		int moves = puzzle[row][column];
		boolean d = false,r=false,l=false,u = false;
		//Down
		if(row+moves<n){
			if(solutionArray[row+moves][column]==0){
				solutionArray[row+moves][column]=count;
				d=true;
				
			}
			if(!d&&solutionArray[row+moves][column]>count){
				solutionArray[row+moves][column]=count;
				d=true;
			}
		}
		//Right
		if(column+moves<n){
			if(solutionArray[row][column+moves]==0){
				solutionArray[row][column+moves]=count;
				r=true;
			}
			if(!r&&solutionArray[row][column+moves]>count){
				solutionArray[row][column+moves]=count;
				r=true;
			}
		}
		//Left
		if(column-moves>=0){
			if(solutionArray[row][column-moves]==0){
				solutionArray[row][column-moves]=count;
				l=true;
			}
			if(!l&&solutionArray[row][column-moves]>count){
				solutionArray[row][column-moves]=count;
				l=true;
			}
			
		}
		//Up
		if(row-moves>=0){
			if(solutionArray[row-moves][column]==0){
				solutionArray[row-moves][column]=count;
				u=true;
			}
			if(!u&&solutionArray[row-moves][column]>count){
				solutionArray[row-moves][column]=count;
				u=true;
			}
		}
		count++;
		if (d) {
			doBFS(row + moves, column, solutionArray, count, puzzle);
		}
		if(r){
			doBFS(row,column+moves,solutionArray,count, puzzle);
		}
		if(l){
			doBFS(row,column-moves,solutionArray,count, puzzle);
		}
		if(u){
			doBFS(row-moves,column,solutionArray,count, puzzle);
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
			alert.setContentText("You must generate a starting puzzle first before improving it");
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
			
			int currVal = currPuzzle[x][y];
			int currPuzzleValue = valueFunction(currPuzzle);
			currPuzzle[x][y] = newVal;
			int newPuzzleValue = valueFunction(currPuzzle);
			if (newPuzzleValue < currPuzzleValue) {
				currPuzzle[x][y] = currVal;
			}
		}
		
		return currPuzzle;
	}
	
	//Random Walk, given the number of climbs and probability
	private int[][] withRandomWalk(int numClimbs, double prob) {
		int[][] currPuzzle = getPuzzle();
		
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
			
			int prevVal = currPuzzle[x][y];
			int prevPuzzleVal = valueFunction(currPuzzle);
			currPuzzle[x][y] = newVal;
			int newPuzzleVal = valueFunction(currPuzzle);
			
			if (newPuzzleVal >= prevPuzzleVal) {
				continue;
			} else if (random.nextDouble() <= prob) {
				continue;
			} else {
				currPuzzle[x][y] = prevVal;
			}
		}
		
		return currPuzzle;
	}
	
	//Random Restarts, given the number of climbs and iterations per climb
	private int[][] withRandomRestarts(int numClimbs, int itsPerClimb) {
		int[][] currPuzzle = getPuzzle();
		int[][] bestPuzzle = currPuzzle;
		
		Random random = new Random();
		int n = currPuzzle.length;
		for (int j = 0; j < numClimbs; j++) {
			currPuzzle = createNewPuzzle(n);
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
		
				int prevVal = currPuzzle[x][y];
				int prevPuzzleValue = valueFunction(currPuzzle);
				currPuzzle[x][y] = newVal;
				int newPuzzleValue = valueFunction(currPuzzle);
			
				if (newPuzzleValue >= prevPuzzleValue) {
					continue;
				} else {
					currPuzzle[x][y] = prevVal;
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
			
			int currVal = currPuzzle[x][y];
			int currPuzzleValue = valueFunction(currPuzzle);
			currPuzzle[x][y] = newVal;
			int newPuzzleValue = valueFunction(currPuzzle);
			
			if (newPuzzleValue >= currPuzzleValue) {
				continue;
			} else {
				double numerator = newPuzzleValue - currPuzzleValue;
				double power = numerator / startTemp;
				double prob = Math.pow(Math.E, power);
				if (random.nextDouble() <= prob) {
					continue;
				} else {
					currPuzzle[x][y] = currVal;
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
		int n = puzzle.length;
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
		int n = puzzle.length;
		int[][] solArray = new int[n][n];
		doBFS(0, 0, solArray, 1, puzzle);
		
		return solArray[index1][index2];
	}
	
	private void showValue(int[][] puzzle) {
		ValueDisplay.setText(Integer.toString(valueFunction(puzzle)));
	}
	
	@FXML TextField TargetedChangeIter;
	
	/* The only way to reach the goal is by being in one of the squares above
	 * the goal cell, or one of the squares to the left of the goal square.
	 * Randomly picking one of those squares, and changing that value, targets
	 * the squares most likely needed to reach the goal cell. It is also a 
	 * repeatable process, so the number of iterations corresponds to how many
	 * times the program should attempt changing one of those squares to improve
	 * the puzzle's difficulty.
	 */
	public void ownApproach(ActionEvent event) {
		if (getPuzzle() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No puzzle found");
			alert.setContentText("You must generate a puzzle first, then improve it");
			alert.showAndWait();
			return;
		}
		
		int numIterations = 0;
		try {
			numIterations = Integer.parseInt(TargetedChangeIter.getText());
		} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Input");
			alert.setContentText("The number of iterations must be a proper integer value.");
			alert.showAndWait();
			return;
		}
		
		long startTime = Calendar.getInstance().getTimeInMillis();
		
		int[][] puzzle = getPuzzle();
		int n = puzzle.length;
		Random random = new Random();
		for (int i = 0; i < numIterations; i++) {
			boolean aboveGoalSqr = random.nextDouble() <= 0.5 ? true : false;
			
			int min = 0;
			int max = n - 2;
			int prevSquare;
			int index = random.nextInt(max - min + 1) + min;
			if (!aboveGoalSqr) {
				prevSquare = puzzle[n - 1][index];
			} else {
				prevSquare = puzzle[index][n - 1];
			}
			
			min = 1;
			max = n - index - 1;
			int newSquare = random.nextInt(max - min + 1) + min;
			
			int prevPuzzleValue = valueFunction(puzzle);
			if (!aboveGoalSqr) {
				puzzle[n - 1][index] = newSquare;
			} else {
				puzzle[index][n - 1] = newSquare;
			}
			int newPuzzleValue = valueFunction(puzzle);
			
			if (newPuzzleValue < prevPuzzleValue) {
				if (!aboveGoalSqr) {
					puzzle[n - 1][index] = prevSquare;
				} else {
					puzzle[index][n - 1] = prevSquare;
				}
			}
		}
		
		showValue(puzzle);
		setPuzzle(puzzle);
		showComputeTime(startTime);
		showPuzzle(puzzle);
	}
	
	//reading from file here
	public void puzzleFromFile(ActionEvent event) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Filename");
		dialog.setContentText("Please enter the name of the file:");
		dialog.showAndWait();
		String filename = dialog.getResult();
		
		try {
			long startTime = Calendar.getInstance().getTimeInMillis();
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			int n = Integer.parseInt(bufferedReader.readLine());
			int[][] puzzle = new int[n][n];
			
			for (int i = 0; i < n; i++) {
				String line = bufferedReader.readLine();
				String[] splitLine = line.split("\\s+");
				
				for (int j = 0; j < n; j++) {
					int value = Integer.parseInt(splitLine[j]);
					puzzle[i][j] = value;
				}
			}
			
			setPuzzle(puzzle);
			showPuzzle(puzzle);
			showValue(puzzle);
			showComputeTime(startTime);
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid file");
			alert.setContentText("File not found or invalid format, ignoring file");
			alert.showAndWait();
			return;
		}
	}
	
	@FXML MenuButton PlotChoice;
	public void generatePlotValues(ActionEvent event) {
		String plotChoice = PlotChoice.getText();
		List<Integer> numIters = new ArrayList<Integer>();
		List<Integer> values = new ArrayList<Integer>();
		
		if (getPuzzle() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No starting puzzle");
			alert.setContentText("You must generate a starting puzzle first before improving it.");
			alert.showAndWait();
			return;
		}
		
		int n = 0;
		if (plotChoice.equals("Pure Hill Climbing")) {
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
			
			int[][] puzzle = getPuzzle();
			n = puzzle.length;
			for (int k = 0; k < 55; k++) {
				Random random = new Random();
				
				for (int i = 0; i < numberOfClimbs; i++) {
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
					
					int currVal = puzzle[x][y];
					int currPuzzleValue = valueFunction(puzzle);
					puzzle[x][y] = newVal;
					int newPuzzleValue = valueFunction(puzzle);
					if (newPuzzleValue < currPuzzleValue) {
						puzzle[x][y] = currVal;
					}
				}
				
				setPuzzle(puzzle);
				numIters.add(numberOfClimbs);
				values.add(valueFunction(puzzle));
				
				numberOfClimbs += 10;
				puzzle = createNewPuzzle(n);
			}
		} else if (plotChoice.equals("Random Restarts")) {
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
			try {
				numberOfIterations = Integer.parseInt(numIterations.getText());
			} catch (NumberFormatException ex) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Number of iterations is not valid");
				alert.setContentText("You must input an integer for number of iterations.");
				alert.showAndWait();
				return;
			}
			
			int[][] puzzle = getPuzzle();
			n = puzzle.length;
			for (int k = 0; k < 55; k++) {
				Random random = new Random();
				int[][] bestPuzzle = new int[n][n];
				
				for (int i = 0; i < numberOfClimbs; i++) {
					puzzle = createNewPuzzle(n);
					
					for (int j = 0; j < numberOfIterations; j++) {
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
						
						int currVal = puzzle[x][y];
						int currPuzzleValue = valueFunction(puzzle);
						puzzle[x][y] = newVal;
						int newPuzzleValue = valueFunction(puzzle);
						
						if (newPuzzleValue < currPuzzleValue) {
							puzzle[x][y] = currVal;
						}
					}
					
					if (valueFunction(puzzle) >= valueFunction(bestPuzzle)) {
						bestPuzzle = puzzle;
					}
				}
				
				setPuzzle(puzzle);
				numIters.add(numberOfClimbs);
				values.add(valueFunction(bestPuzzle));
				
				numberOfClimbs += 10;
				puzzle = createNewPuzzle(n);
			}
		} else if (plotChoice.equals("Random Walk")) {
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
			
			double p = 0;
			try {
				p = Double.parseDouble(pValue.getText());
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid value");
				alert.setContentText("You must input a number between 0 and 1 for the probability");
				alert.showAndWait();
				return;
			}
			
			if (p > 1 || p < 0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid value");
				alert.setContentText("The value for probability must be between 0 and 1");
				alert.showAndWait();
				return;
			}
			
			int[][] puzzle = getPuzzle();
			n = puzzle.length;
			for (int k = 0; k < 55; k++) {
				Random random = new Random();
				
				for (int i = 0; i < numberOfClimbs; i++) {
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
					
					int currVal = puzzle[x][y];
					int currPuzzleValue = valueFunction(puzzle);
					puzzle[x][y] = newVal;
					int newPuzzleValue = valueFunction(puzzle);
					
					if (newPuzzleValue < currPuzzleValue && random.nextDouble() > p) {
						puzzle[x][y] = currVal;
					}
				}
				
				setPuzzle(puzzle);
				numIters.add(numberOfClimbs);
				values.add(valueFunction(puzzle));
				
				numberOfClimbs += 10;
				puzzle = createNewPuzzle(n);
			}
		} else if (plotChoice.equals("Simulated Annealing")) {
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
			
			int[][] puzzle = getPuzzle();
			n = puzzle.length;
			double initTemp = startTemp;
			for (int k = 0; k < 55; k++) {
				Random random = new Random();
				
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
					
					int currVal = puzzle[x][y];
					int currPuzzleValue = valueFunction(puzzle);
					puzzle[x][y] = newVal;
					int newPuzzleValue = valueFunction(puzzle);
					
					if (newPuzzleValue >= currPuzzleValue) {
						continue;
					} else {
						double numerator = newPuzzleValue - currPuzzleValue;
						double power = numerator / startTemp;
						double prob = Math.pow(Math.E, power);
						if (random.nextDouble() <= prob) {
							continue;
						} else {
							puzzle[x][y] = currVal;
						}
					}
					
					startTemp = startTemp * decay;
				}
				
				setPuzzle(puzzle);
				numIters.add(numIterations);
				values.add(valueFunction(puzzle));
				
				numIterations += 10;
				puzzle = createNewPuzzle(n);
				startTemp = initTemp;
			}
		} else if (plotChoice.equals("Targeted Change")) {
			int numIterations = 0;
			try {
				numIterations = Integer.parseInt(TargetedChangeIter.getText());
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid Input");
				alert.setContentText("The number of iterations must be a proper integer value.");
				alert.showAndWait();
				return;
			}
			
			int[][] puzzle = getPuzzle();
			n = puzzle.length;
			for (int k = 0; k < 55; k++) {
				Random random = new Random();
				
				for (int i = 0; i < numIterations; i++) {
					boolean aboveGoalSqr = random.nextDouble() <= 0.5 ? true : false;
					
					int min = 0;
					int max = n - 2;
					int prevSquare;
					int index = random.nextInt(max - min + 1) + min;
					if (!aboveGoalSqr) {
						prevSquare = puzzle[n - 1][index];
					} else {
						prevSquare = puzzle[index][n - 1];
					}
					
					min = 1;
					max = n - index - 1;
					int newSquare = random.nextInt(max - min + 1) + min;
					
					int prevPuzzleValue = valueFunction(puzzle);
					if (!aboveGoalSqr) {
						puzzle[n - 1][index] = newSquare;
					} else {
						puzzle[index][n - 1] = newSquare;
					}
					int newPuzzleValue = valueFunction(puzzle);
					
					if (newPuzzleValue < prevPuzzleValue) {
						if (!aboveGoalSqr) {
							puzzle[n - 1][index] = prevSquare;
						} else {
							puzzle[index][n - 1] = prevSquare;
						}
					}
				}
				
				setPuzzle(puzzle);
				numIters.add(numIterations);
				values.add(valueFunction(puzzle));
				
				numIterations += 10;
				puzzle = createNewPuzzle(n);
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Missing choice");
			alert.setContentText("You must specify what you want to plot first.");
			alert.showAndWait();
			return;
		}
		
		System.out.println("");
		System.out.println(plotChoice);
		System.out.println("n = " + n);
		System.out.print("Iterations: " + "\t");
		for (Integer i : numIters) {
			System.out.print(i + "\t");
		}
		System.out.println("");
		System.out.print("Result Value:" + "\t");
		for (Integer i : values) {
			System.out.print(i + "\t");
		}
		System.out.println("");
	}
	
	public void setHillClimb(ActionEvent event) {
		PlotChoice.setText("Pure Hill Climbing");
	}
	public void setRandomRestarts(ActionEvent event) {
		PlotChoice.setText("Random Restarts");
	}
	public void setRandomWalk(ActionEvent event) {
		PlotChoice.setText("Random Walk");
	}
	public void setSimAnneal(ActionEvent event) {
		PlotChoice.setText("Simulated Annealing");
	}
	public void setTargetChange(ActionEvent event) {
		PlotChoice.setText("Targeted Change");
	}
}
