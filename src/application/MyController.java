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
	@FXML TextField songName;
	@FXML TextField artist;
	@FXML TextField year;
	@FXML TextField album;
	@Override
	
	public void initialize(URL location, ResourceBundle resources) {

	}
	
	public void testingStuff(){
		
		System.out.println(songName.getText());
		System.out.println(artist.getText());
		System.out.println(year.getText());
		System.out.println(album.getText());
		
		
		
	}
	
	
}
