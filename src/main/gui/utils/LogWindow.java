package main.gui.utils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LogWindow extends Application {
	
	private static ListView<String> logList;
	private static Stage window;
	
	public LogWindow() {
		try {
			start(new Stage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage window) throws Exception {
		LogWindow.window = window;
		
		window.setTitle("Roll Log");
		
		AnchorPane layout = new AnchorPane();
		
		logList = new ListView<>();
		logList.setMinWidth(300);
		logList.setPlaceholder(new Label("No Rolls Have Been Made Yet"));
		
		AnchorPane.setTopAnchor(logList, 0.0);
		AnchorPane.setLeftAnchor(logList, 0.0);
		AnchorPane.setRightAnchor(logList, 0.0);
		AnchorPane.setBottomAnchor(logList, 0.0);
		layout.getChildren().addAll(logList);
		
		Scene scene = new Scene(layout);
		
		window.setScene(scene);
	}
	
	public static void updateLog(String log) {
		logList.getItems().add(log);
		logList.scrollTo(logList.getItems().size() - 1);
	}
	
	public static void showWindow() {
		window.show();
		if(window.isIconified()) {
			window.setIconified(false);
		}
	}
	
	public static void close() {
		window.close();
	}

}
