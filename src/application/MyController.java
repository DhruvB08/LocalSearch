package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

public class MyController implements Initializable{
	
	@FXML TextField numClimbs;
	@FXML TextField numIterations;
	@FXML TextField pValue;
	@FXML TextField numIterationsA;
	@FXML TextField initTemp;
	@FXML TextField decayRate;
	@FXML TextField ComputeDisplay;
	@FXML TextField ValueDisplay;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	
	@FXML MenuButton PuzzleSize;
	private int puzzleSize;
	
	/**
	 * Set the size of the puzzle
	 * 
	 * @param size
	 */
	public void setPuzzleSize(int size) {
		puzzleSize = size;
		PuzzleSize.setText(Integer.toString(size));
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
		
	/**
	 * Get the size of the puzzle
	 * 
	 * @return size of puzzle (in input from user)
	 */
	public int getPuzzleSize() {
		return puzzleSize;
	}
	
	private int[][] puzzle;
	
	public void setPuzzle(int[][] puzz) {
		puzzle = puzz;
	}
	
	public int[][] getPuzzle() {
		return puzzle;
	}
	
	/**
	 * Clicking the 'New Puzzle' Button
	 * Creates a new puzzle using the input size by user
	 * Creates an alert if no input size from user
	 * 
	 * @param event
	 */
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
	
	/**
	 * Create a new puzzle using given input size
	 * 
	 * @param n		the dimensions of the puzzle (n by n)
	 * @return		a randomly generated new puzzle of size n by n
	 */
	private int[][] createNewPuzzle(int n) {
		int[][] puzzle = new int[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if ((i == n - 1) && (j == n - 1)) {
					puzzle[i][j] = 0;
				} else {
					puzzle[i][j] = randomLegalValue(n, i, j);
				}
			}
		}
		
		return puzzle;
	}
	
	/**
	 * Generates a random legal value for the given cell
	 * 
	 * @param size	the dimensions of the puzzle
	 * @param i		the first index of the chosen square
	 * @param j		the second index of the chosen square
	 * @return		a legal integer value for the chosen square
	 */
	public int randomLegalValue(int size, int i, int j) {
		int mxL = j;
		int mxR = size - j - 1;
		int mxU = i;
		int mxD = size - i - 1;
		
		int maxLorR = Math.max(mxL, mxR);
		int maxUorD = Math.max(mxU, mxD);
		int maxValue = Math.max(maxLorR, maxUorD);
		
		Random random = new Random();
		int newValue = random.nextInt(maxValue) + 1;
		
		return newValue;
	}
	
	@FXML TextArea PuzzleBox;
	
	/**
	 * Showing the puzzle on the TextArea in the GUI
	 * 
	 * @param puzzleMatrix	the puzzle
	 */
	private void showPuzzle(int[][] puzzleMatrix) {
		PuzzleBox.setText("");
		
		int n = puzzleMatrix.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if ((i == n - 1) && (j == n - 1)) {
					PuzzleBox.appendText("G");
				} else {
					String strVal = Integer.toString(puzzleMatrix[i][j]);
					PuzzleBox.appendText(strVal + "\t");
				}
			}
			
			PuzzleBox.appendText("\n");
		}
	}
	
	/**
	 * Clicking the 'Show Puzzle' button
	 * Displays the current puzzle onto the GUI
	 * 
	 * @param event
	 */
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
	
	/**
	 * Clicking the 'Show Solution' button
	 * Displays the solution matrix of the current puzzle onto the GUI
	 * 
	 * @param event
	 */
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
						String strVal = Integer.toString(val);
						PuzzleBox.appendText(strVal + "\t");
					}
				}
			}
			
			PuzzleBox.appendText("\n");
		}
	}
	
	/**
	 * Tomer do this
	 * 
	 * @param row
	 * @param column
	 * @param solutionArray
	 * @param count
	 * @param puzzle
	 */
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
	
	public final String pureHillClimb = "Pure Hill Climb";
	public final String randomRestarts = "Random Restarts";
	public final String randomWalk = "Random Walk";
	public final String simAnneal = "Simulated Annealing";
	
	/** 
	 * Clicking the 'Hill Climb' button
	 * 
	 * possible input: number of climbs
	 * possible input: number of climbs, iterations per climb
	 * possible input: number of climbs, p value
	 * Each of the above inputs correspond to Pure Hill Climb, Random Restarts,
	 * and Random Walk respectively
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
		
		ImprovementMethodInfo methodInfo = new ImprovementMethodInfo(pureHillClimb, numberOfClimbs);
		methodInfo.puzzle = getPuzzle();

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
			
			methodInfo.methodName = randomRestarts;
			methodInfo.itersPerClimb = numberOfIterations;
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
			
			if (methodInfo.methodName.equals(randomRestarts)) {
				methodInfo.wasChanged = true;
			}
			
			methodInfo.methodName = randomWalk;
			methodInfo.p = pValueInserted;
		}
		
		if (methodInfo.wasChanged) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Too many arguements");
			alert.setContentText("You must either input a p-value for random walk, or number of iterations for random restarts, but not both.");
			alert.showAndWait();
			return;
		} 
		
		int[][] resultPuzzle = getImprovedPuzzle(methodInfo);
		
		showComputeTime(startTime);
		showValue(resultPuzzle);
		setPuzzle(resultPuzzle);
		showPuzzle(resultPuzzle);
	}
	
	/**
	 * Getting the new, improved puzzle
	 * 
	 * @param method	{@link ImprovementMethodInfo} containing all relevant information
	 * @return			the new and improved puzzle
	 */
	private int[][] getImprovedPuzzle(ImprovementMethodInfo method) {
		int[][] puzzle = method.puzzle;
		int n = puzzle.length;
		
		if (method.methodName.equals(randomRestarts)) {
			ImprovementMethodInfo pureClimbs = new ImprovementMethodInfo(pureHillClimb, method.itersPerClimb);
			pureClimbs.puzzle = getPuzzle();
			int[][] bestPuzzle = puzzle;
			
			for (int i = 0; i < method.numClimbs; i++) {
				puzzle = getImprovedPuzzle(pureClimbs);
				int currPuzzleValue = valueFunction(puzzle);
				int bestPuzzleValue = valueFunction(bestPuzzle);
				
				if (currPuzzleValue >= bestPuzzleValue) {
					bestPuzzle = puzzle;
				}
				
				pureClimbs.puzzle = createNewPuzzle(n);
			}
			
			return bestPuzzle;
		}
		
		double currTemp = 0;
		if (method.methodName.equals(simAnneal)) {
			currTemp = method.initTemp;
		}
		
		for (int i = 0; i < method.numClimbs; i++) {
			int[] indexes = getRandomSquare(n);
			int x = indexes[0];
			int y = indexes[1];
			
			int prevValue = puzzle[x][y];
			int prevPuzzleValue = valueFunction(puzzle);
			
			int newValue = randomLegalValue(n, x, y);
			puzzle[x][y] = newValue;
			int newPuzzleValue = valueFunction(puzzle);
			
			if (newPuzzleValue >= prevPuzzleValue) {
				continue;
			} else {
				if (method.methodName.equals(randomWalk)) {
					Random random = new Random();
					double res = random.nextDouble();
					
					if (res <= method.p) {
						continue;
					} else {
						puzzle[x][y] = prevValue;
					}
				} 
				
				else if (method.methodName.equals(simAnneal)) {
					Random random = new Random();
					double numerator = newPuzzleValue - prevPuzzleValue;
					double power = numerator / currTemp;
					double prob = Math.pow(Math.E, power);
					
					if (random.nextDouble() <= prob) {
						continue;
					} else {
						puzzle[x][y] = prevValue;
					}
				}
				
				else if (method.methodName.equals(pureHillClimb)) {
					puzzle[x][y] = prevValue;
				}
			}
			
			if (method.methodName.equals(simAnneal)) {
				currTemp = currTemp * method.decay;
			}
		}
		
		return puzzle;
	}
	
	/**
	 * Get the indexes of a randomly picked square in an array
	 * 
	 * @param n		the size of the puzzle (n by n)
	 * @return		[x, y] where x and y are the indexes of the cell in a matrix
	 */
	private int[] getRandomSquare(int n) {
		Random random = new Random();
		
		int maxNum = n - 1, minNum = 0;
		int x = random.nextInt(maxNum - minNum + 1) + minNum;
		int y = random.nextInt(maxNum - minNum + 1) + minNum;
		
		while (x == maxNum && y == maxNum) {
			x = random.nextInt(maxNum - minNum + 1) + minNum;
			y = random.nextInt(maxNum - minNum + 1) + minNum;
		}
		
		int[] indexes = new int[2];
		indexes[0] = x;
		indexes[1] = y;
		
		return indexes;
	}
	
	/**
	 * Clicking the 'Simulated Annealing' button
	 * Needs a number of iterations, initial temperature, and decay rate as parameters
	 * 
	 * @param event
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
		
		ImprovementMethodInfo methodInfo = new ImprovementMethodInfo(simAnneal, numIterations);
		methodInfo.initTemp = startTemp;
		methodInfo.decay = decay;
		
		int[][] currPuzzle = getImprovedPuzzle(methodInfo);
		
		showComputeTime(startTime);
		showValue(currPuzzle);
		setPuzzle(currPuzzle);
		showPuzzle(currPuzzle);
	}
	
	/**
	 * Shows the time it took to compute puzzle and solution onto the GUI
	 * 
	 * @param startTime		time in milliseconds when computation started
	 */
	private void showComputeTime(long startTime) {
		long currTime = Calendar.getInstance().getTimeInMillis();
		long elapsed = currTime - startTime;
		
		ComputeDisplay.setText(Long.toString(elapsed));
	}
	
	/**
	 * Getting the value of the puzzle
	 * 
	 * @param puzzle		puzzle to get value of
	 * @return				0 if no solution, otherwise amount of squares necessary to reach goal from start
	 */
	private int valueFunction(int[][] puzzle) {
		int n = puzzle.length;
		int[][] solArray = new int[n][n];
		doBFS(0, 0, solArray, 1, puzzle);
		int sol = solArray[n-1][n-1];
		
		if (sol == 0) {
			int numFails = 0;
			
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (solArray[i][j] == 0) {
						numFails++;
					}
				}
			}
			
			return (-1 * numFails);
		}
		
		return sol;
	}
	
	/**
	 * Getting solution at a specific square
	 * 
	 * @param puzzle	the puzzle to work with
	 * @param index1	first index of target square
	 * @param index2	second index of target square
	 * @return			amount of squares necessary to reach target square from start square, or 0 if no solution
	 */
	private int valueFunction(int[][] puzzle, int index1, int index2) {
		int n = puzzle.length;
		int[][] solArray = new int[n][n];
		doBFS(0, 0, solArray, 1, puzzle);
		
		return solArray[index1][index2];
	}
	
	/**
	 * Show the value of the given puzzle onto the GUI
	 * 
	 * @param puzzle	puzzle to work with
	 */
	private void showValue(int[][] puzzle) {
		ValueDisplay.setText(Integer.toString(valueFunction(puzzle)));
	}
	
	@FXML TextField TargetedChangeIter;
	
	/**
	 * The only way to reach the goal is by being in one of the squares above
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
		int[][] puzzle = targetedChange(getPuzzle(), numIterations);
		
		showValue(puzzle);
		setPuzzle(puzzle);
		showComputeTime(startTime);
		showPuzzle(puzzle);
	}
	
	/**
	 * Implementing Targeted Change
	 * 
	 * @param puzzle	the puzzle to start with
	 * @param numIters	number of iterations
	 * @return			the new and improved puzzle
	 */
	private int[][] targetedChange(int[][] puzzle, int numIters) {
		int n = puzzle.length;
		Random random = new Random();
		
		for (int i = 0; i < numIters; i++) {
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
			int newSquare = 0;
			if (!aboveGoalSqr) {
				newSquare = randomLegalValue(n, n - 1, index);
			} else {
				newSquare = randomLegalValue(n, index, n - 1);
			}
			
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
		
		return puzzle;
	}
	
	/**
	 * Read an input puzzle from a file
	 * File must be in project
	 * 
	 * @param event
	 */
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
	
	/**
	 * Clicking the plot button
	 * Prints 55 x/y values onto system output for given choice
	 * 
	 * @param event
	 */
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
		
		int n = getPuzzle().length;
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
			
			ImprovementMethodInfo methodInfo = new ImprovementMethodInfo(pureHillClimb, numberOfClimbs);
			methodInfo.puzzle = getPuzzle();
	
			for (int k = 0; k < 55; k++) {
				int[][] puzzle = getImprovedPuzzle(methodInfo);
				
				numIters.add(numberOfClimbs);
				values.add(valueFunction(puzzle));
				
				methodInfo.numClimbs += 10;
				methodInfo.puzzle = createNewPuzzle(n);
			}
		} 
		
		else if (plotChoice.equals("Random Restarts")) {
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
			
			ImprovementMethodInfo methodInfo = new ImprovementMethodInfo(randomRestarts, numberOfClimbs);
			methodInfo.puzzle = getPuzzle();
			methodInfo.itersPerClimb = numberOfIterations;
			
			for (int k = 0; k < 55; k++) {
				int[][] puzzle = getImprovedPuzzle(methodInfo);
				
				numIters.add(methodInfo.numClimbs);
				values.add(valueFunction(puzzle));
				
				methodInfo.numClimbs += 10;
				methodInfo.puzzle = createNewPuzzle(n);
			}
		} 
		
		else if (plotChoice.equals("Random Walk")) {
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
			
			ImprovementMethodInfo methodInfo = new ImprovementMethodInfo(randomWalk, numberOfClimbs);
			methodInfo.puzzle = getPuzzle();
			methodInfo.p = p;
			
			for (int k = 0; k < 55; k++) {
				int[][] puzzle = getImprovedPuzzle(methodInfo);
				
				numIters.add(methodInfo.numClimbs);
				values.add(valueFunction(puzzle));
				
				methodInfo.numClimbs += 10;
				methodInfo.puzzle = createNewPuzzle(n);
			}
		} 
		
		else if (plotChoice.equals("Simulated Annealing")) {
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
			
			ImprovementMethodInfo methodInfo = new ImprovementMethodInfo(simAnneal, numIterations);
			methodInfo.puzzle = getPuzzle();
			methodInfo.initTemp = startTemp;
			methodInfo.decay = decay;
			
			for (int k = 0; k < 55; k++) {
				int[][] puzzle = getImprovedPuzzle(methodInfo);
				
				numIters.add(methodInfo.numClimbs);
				values.add(valueFunction(puzzle));
				
				methodInfo.numClimbs += 10;
				methodInfo.puzzle = createNewPuzzle(n);
			}
		} 
		
		else if (plotChoice.equals("Targeted Change")) {
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
			for (int k = 0; k < 55; k++) {
				puzzle = targetedChange(puzzle, numIterations);
				
				numIters.add(numIterations);
				values.add(valueFunction(puzzle));
				
				numIterations += 10;
				puzzle = createNewPuzzle(n);
			}
		} 
		
		else {
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

class ImprovementMethodInfo {
	String methodName;
	int numClimbs;
	int[][] puzzle;
	
	boolean wasChanged;
	int itersPerClimb;
	double p;
	
	double initTemp;
	double decay;
	
	public ImprovementMethodInfo(String name, int climbs) {
		methodName = name;
		numClimbs = climbs;
	}
}
