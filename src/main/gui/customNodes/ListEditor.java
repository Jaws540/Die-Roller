package main.gui.customNodes;

import java.util.List;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Pair;
import main.dieRoller.Roll;
import main.gui.dieRoller.RollEditor;

public class ListEditor extends Dialog<Pair<List<Roll>, Boolean>> {
	
	private boolean edited = false;
	
	public ListEditor(List<Roll> list){
		this.setTitle("Edit Saved Rolls");
		ButtonType doneButtonType = new ButtonType("Save", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().addAll(doneButtonType, ButtonType.CANCEL);
		
		BorderPane layout = new BorderPane();

		VBox center = new VBox(10);
		Label listLabel = new Label("Saved Rolls:");
		ListView<Roll> lv = new ListView<>();
		Callback<ListView<Roll>, ListCell<Roll>> cellFactory =
				new Callback<ListView<Roll>, ListCell<Roll>>() {
		    @Override
		    public ListCell<Roll> call(ListView<Roll> l) {
		        return new ListCell<Roll>() {
		            @Override
		            protected void updateItem(Roll item, boolean empty) {
		                super.updateItem(item, empty);
		                if (item == null || empty) {
		                    setGraphic(null);
		                } else {
		                	HBox cellLayout = new HBox();
		                	
		                	Label nameLabel = new Label(item.getName());
		                	nameLabel.setTextFill(Color.BLACK);
		                	Label rawDieLabel = new Label(item.getDieRaw());
		                	rawDieLabel.setTextFill(Color.BLACK);
		                	
		                	cellLayout.getChildren().addAll(nameLabel, new Spacer(), rawDieLabel);
		                	
		                	setGraphic(cellLayout);
		                }
		            }
		        } ;
		    }
		};
		lv.setCellFactory(cellFactory);
		for(Roll r : list){
			lv.getItems().add(r);
		}
		lv.setMinWidth(350);
		lv.setMaxHeight(200);
		lv.setPlaceholder(new Label("No Saved Rolls"));
		center.getChildren().addAll(listLabel, lv);
		layout.setCenter(center);
			
		VBox buttonLayout = new VBox(10);
		buttonLayout.setPadding(new Insets(10));
		buttonLayout.setAlignment(Pos.CENTER);
		Button shiftUp = new Button("Move Up");
		shiftUp.setOnAction(e -> {
			int index = lv.getSelectionModel().getSelectedIndex();
			Roll item = lv.getItems().get(index - 1);
			list.set(index - 1, lv.getSelectionModel().getSelectedItem());
			list.set(index, item);
			updateList(lv, list);
			lv.getSelectionModel().select(index - 1);
		});
		Button shiftDown = new Button("Move Down");
		shiftDown.setOnAction(e -> {
			int index = lv.getSelectionModel().getSelectedIndex();
			Roll item = lv.getItems().get(index + 1);
			list.set(index + 1, lv.getSelectionModel().getSelectedItem());
			list.set(index, item);
			updateList(lv, list);
			lv.getSelectionModel().select(index + 1);
		});
		Button editRoll = new Button("Edit Roll");
		editRoll.setOnAction(e -> {
			Roll oldRoll = lv.getSelectionModel().getSelectedItem();
			
			Dialog<Roll> rollSettingsEditor = new Dialog<>();
			rollSettingsEditor.setTitle("Edit Roll Settings");

			rollSettingsEditor.getDialogPane().getButtonTypes().addAll(doneButtonType, ButtonType.CANCEL);
			
			VBox editorLayout = new VBox(10);
			editorLayout.setAlignment(Pos.CENTER);
			
			HBox nameLayout = new HBox(10);
			nameLayout.setAlignment(Pos.CENTER);
			
			Label nameLabel = new Label("Name: ");
			
			TextField nameField = new TextField();
			nameField.setMaxWidth(Double.MAX_VALUE);
			HBox.setHgrow(nameField, Priority.ALWAYS);
			nameField.setText(lv.getSelectionModel().getSelectedItem().getName());
			
			nameLayout.getChildren().addAll(nameLabel, nameField);
			
			RollEditor rollEditor = new RollEditor(lv.getSelectionModel().getSelectedItem(), true);
			rollEditor.toggleAdvanced(true);
			
			editorLayout.getChildren().addAll(nameLayout, rollEditor);
			
			rollSettingsEditor.getDialogPane().setContent(editorLayout);
			
			rollSettingsEditor.setResultConverter(dialogButton -> {
			    if (dialogButton == doneButtonType) {
			    	Roll result;
			    	if(rollEditor.advancedRollProperty().get().getDieRaw().isEmpty())
			    		result = rollEditor.getRollProperty().get();
			    	else
			    		result = rollEditor.advancedRollProperty().get();
			    	
			    	result.setName(nameField.getText());
			    	edited = !oldRoll.equals(result);
			        return result;
			    }
			    return null;
			});
			
			Optional<Roll> dialogResult = rollSettingsEditor.showAndWait();
			if(dialogResult.isPresent()){
				list.set(lv.getSelectionModel().getSelectedIndex(), dialogResult.get());
				updateList(lv, list);
			}
		});
        Button removeSavedRoll = new Button("Remove");
        removeSavedRoll.setTooltip(new Tooltip("Removes selected roll from the Saved Rolls list"));
        removeSavedRoll.setOnAction(e -> {
        	boolean success = list.remove(lv.getSelectionModel().getSelectedItem());
			updateList(lv, list);
			if(success)
				edited = true;
        });
		buttonLayout.getChildren().addAll(shiftUp, shiftDown, editRoll, removeSavedRoll);
		for(Node n : buttonLayout.getChildren()){
			Button btn = (Button) n;
			btn.setMaxWidth(Double.MAX_VALUE);
			HBox.setHgrow(n, Priority.ALWAYS);
			btn.setDisable(true);
		}
		layout.setRight(buttonLayout);
		
		lv.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> {
			if(lv.getSelectionModel().getSelectedItem() != null){
				for(Node n : buttonLayout.getChildren()){
					Button btn = (Button) n;
					btn.setDisable(false);
				}
			}
			
			if(lv.getSelectionModel().getSelectedIndex() == 0){
				shiftUp.setDisable(true);
			}
			
			if(lv.getSelectionModel().getSelectedIndex() == list.size() - 1){
				shiftDown.setDisable(true);
			}
		});

		this.getDialogPane().setContent(layout);
		
		this.setResultConverter(dialogButton -> {
		    if (dialogButton == doneButtonType) {
		        return new Pair<List<Roll>, Boolean>(list, edited);
		    }
		    return null;
		});
	}

	private void updateList(ListView<Roll> lv, List<Roll> list) {
		lv.getItems().clear();
		for(Roll r : list){
			lv.getItems().add(r);
		}
	}

}
