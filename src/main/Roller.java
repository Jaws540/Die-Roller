package main;

import main.gui.GUI;

import javafx.application.Application;
import javafx.stage.Stage;

public class Roller extends Application {
	
	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage arg0) {
		Manager man = new Manager();
		
		GUI gui = new GUI(arg0, man);
		
		man.setGUI(gui);
		
		gui.show();
	}

}
