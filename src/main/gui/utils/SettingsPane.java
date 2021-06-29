package main.gui.utils;

import java.util.List;
import java.util.Optional;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.gui.GUI;

public class SettingsPane {
	
	private Dialog<String> settings;
	
	private ListView<Integer> critSuccessList;
	private ListView<Integer> critFailList;
	
	private GUI gui;
	
	public SettingsPane(GUI gui) {
		this.gui = gui;
		
		settings = new Dialog<>();
		settings.getDialogPane().getButtonTypes().addAll(ButtonType.FINISH, ButtonType.CLOSE);
		settings.setTitle("Settings");
		
		VBox layout = new VBox(10);
		
		HBox critSuccessLayout = new HBox(10);
		
		Label critSuccessLabel = new Label("Crit Success Values: ");
		
		critSuccessList = new ListView<>();
		critSuccessList.setOrientation(Orientation.HORIZONTAL);
		critSuccessList.setMaxHeight(25);
		critSuccessList.setMaxWidth(175);
		critSuccessList.setMinHeight(25);
		critSuccessList.setMinWidth(175);
		critSuccessList.setPlaceholder(new Label("No Crit Success Values"));
		
		Button editSuccessList = new Button("Edit");
		editSuccessList.setOnAction(e -> {
			Optional<List<Integer>> result = new CritListEditor(critSuccessList.getItems()).showAndWait();
			if(result.isPresent()) {
				List<Integer> critVals = result.get();
				critSuccessList.getItems().clear();
				critSuccessList.getItems().addAll(critVals);
				gui.getMan().getSettings().editCritSuccess(critVals);
			}
		});
		
		critSuccessLayout.getChildren().addAll(critSuccessLabel, critSuccessList, editSuccessList);
		
		HBox critFailLayout = new HBox(10);
		
		Label critFailLabel = new Label("Crit Success Values: ");
		
		critFailList = new ListView<>();
		critFailList.setOrientation(Orientation.HORIZONTAL);
		critFailList.setMaxHeight(25);
		critFailList.setMaxWidth(175);
		critFailList.setMinHeight(25);
		critFailList.setMinWidth(175);
		critFailList.setPlaceholder(new Label("No Crit Fail Values"));
		
		Button editFailList = new Button("Edit");
		editFailList.setOnAction(e -> {
			Optional<List<Integer>> result = new CritListEditor(critFailList.getItems()).showAndWait();
			if(result.isPresent()) {
				List<Integer> critVals = result.get();
				critFailList.getItems().clear();
				critFailList.getItems().addAll(critVals);
				gui.getMan().getSettings().editCritFail(critVals);
			}
		});
		
		critFailLayout.getChildren().addAll(critFailLabel, critFailList, editFailList);
		
		layout.getChildren().addAll(critSuccessLayout, critFailLayout);
		
		settings.getDialogPane().setContent(layout);
	}
	
	public void showSettings() {
		for(int i : gui.getMan().getSettings().getCritSuccess()) {
			critSuccessList.getItems().add(i);
		}
		
		for(int i : gui.getMan().getSettings().getCritFail()) {
			critFailList.getItems().add(i);
		}
		settings.showAndWait();
	}

}
