package application;
	
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/MainScene.fxml"));
			primaryStage.setTitle("My Application");
			primaryStage.setScene(new Scene(root));
			primaryStage.setResizable(false);
			primaryStage.show();
			
			/*
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			*/
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	static Scanner scanner = new Scanner(System.in);
	public static void main(String[] args) {
		/*
		System.out.println("Please enter the name of the file to use, or blank if no input file.");
		System.out.println("e.g.: text.txt");
		String filename = scanner.nextLine();
		
		try {
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			int n = Integer.parseInt(bufferedReader.readLine());
			int[][] puzzle = new int[n][n];
			
			for (int i = 0; i < n; i++) {
				String line = bufferedReader.readLine();
				String[] splitLine = line.split("\\s+");
				
				for (int j = 0; j < n; j++) {
					int strIndex = 2 * j;
					int value = Integer.parseInt(splitLine[j]);
					puzzle[i][j] = value;
				}
			}
			
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					System.out.print(puzzle[i][j] + "\t");
				}
				System.out.println("");
			}
		} catch (IOException e) {
			System.out.println("File not found or invalid format, ignoring file");
		}
		*/
		launch(args);
	}
}
