package main.gui.utils;

import java.util.List;
import java.util.Optional;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CritListEditor extends Dialog<List<Integer>> {
	
	public CritListEditor(List<Integer> list) {
		this.setTitle("Edit Crit Values");
		ButtonType doneButtonType = new ButtonType("Save", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().addAll(doneButtonType, ButtonType.CANCEL);
		
		HBox layout = new HBox(10);
		
		ListView<Integer> displayList = new ListView<Integer>();
		displayList.getItems().addAll(list);
		displayList.setMaxWidth(100);
		displayList.setMaxHeight(100);
		displayList.setMinWidth(100);
		displayList.setMinHeight(100);
		displayList.setPlaceholder(new Label("No Values"));
		
		VBox buttonLayout = new VBox(10);
		buttonLayout.setAlignment(Pos.CENTER);
		
		Button addInt = new Button("Add Number");
		addInt.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(addInt, Priority.ALWAYS);
		addInt.setOnAction(e -> {
			TextInputDialog numInput = new TextInputDialog();
			numInput.setTitle("Add Crit Value");
			numInput.setHeaderText(null);
			numInput.setContentText("Enter new crit value");
			Optional<String> result = numInput.showAndWait();
			if(result.isPresent()) {
				try {
					int num = Integer.parseInt(result.get());
					displayList.getItems().add(num);
				}catch(NumberFormatException err) {}
			}
		});
		
		Button removeInt = new Button("Remove");
		removeInt.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(removeInt, Priority.ALWAYS);
		removeInt.setOnAction(e -> {
			if(!displayList.getSelectionModel().isEmpty())
				displayList.getItems().remove(displayList.getSelectionModel().getSelectedIndex());
			else
				displayList.requestFocus();
		});
		
		buttonLayout.getChildren().addAll(addInt, removeInt);
		
		layout.getChildren().addAll(displayList, buttonLayout);
		
		this.getDialogPane().setContent(layout);
		
		this.setResultConverter(dialogButton -> {
		    if (dialogButton == doneButtonType) {
		        return displayList.getItems();
		    }
		    return null;
		});
	}

}
