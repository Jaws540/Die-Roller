package main.gui.customNodes;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;


public class ToggleSwitch extends HBox {
	
	private final Label label = new Label();
	private final Button button = new Button();
	
	private final String leftLabel;
	private final String rightLabel;
	private final String leftColor;
	private final String rightColor;
	
	private boolean pos = true;
	
	private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(false);
	public SimpleBooleanProperty switchOnProperty() { return switchedOn; }
	
	public ToggleSwitch() {
		this.leftLabel = "ON";
		this.rightLabel = "OFF";
		this.leftColor = "#38a2ff"; // #38a2ff, #006aff - Original
		this.rightColor = "#b2b2b2"; // #b2b2b2, lightgrey, grey - Original
		
		this.pos = true;
		
		init();
	}
	
	public ToggleSwitch(boolean startOff) {
		this.leftLabel = "ON";
		this.rightLabel = "OFF";
		this.leftColor = "#38a2ff"; // #38a2ff, #006aff - Original
		this.rightColor = "#b2b2b2"; // #b2b2b2, lightgrey, grey - Original
		
		this.pos = startOff;
		
		init();
	}
	
	public ToggleSwitch(String leftLabel, String rightLabel, boolean startOff){
		this.leftLabel = leftLabel;
		this.rightLabel = rightLabel;
		this.leftColor = "#006aff";
		this.rightColor = "grey";

		this.pos = startOff;
		
		init();
	}
	
	public ToggleSwitch(String leftLabel, String rightLabel, String leftColor, String rightColor, boolean startOff){
		this.leftLabel = leftLabel;
		this.rightLabel = rightLabel;
		this.leftColor = leftColor;
		this.rightColor = rightColor;
		
		this.pos = startOff;
		
		init();
	}
	
	private void init() {
		
		if(pos){
			label.setText(rightLabel);
			getChildren().addAll(button, label);	
		}else{
			label.setText(leftLabel);
			getChildren().addAll(label, button);	
		}
		
		button.setOnAction((e) -> {
			switchedOn.set(!switchedOn.get());
		});
		label.setOnMouseClicked((e) -> {
			switchedOn.set(!switchedOn.get());
		});
		setStyle();
		bindProperties();
		
		switchedOn.addListener((a,b,c) -> {
			if (c) {
				if(pos){
	        		label.setText(leftLabel);
	        		setStyle("-fx-background-color: " + leftColor + ";");
	        		button.toFront();
				}else{
					label.setText(rightLabel);
	    			setStyle("-fx-background-color: " + rightColor + ";");
	        		label.toFront();
				}
    		}else{
    			if(pos){
					label.setText(rightLabel);
	    			setStyle("-fx-background-color: " + rightColor + ";");
	        		label.toFront();
				}else{
	        		label.setText(leftLabel);
	        		setStyle("-fx-background-color: " + leftColor + ";");
	        		button.toFront();
				}
    		}
		});
	}
	
	private void setStyle() {
		//Default Width
		setWidth(80);
		label.setAlignment(Pos.CENTER);
		setStyle("-fx-background-color: " + rightColor + "; -fx-text-fill:black; -fx-background-radius: 4;");
		setAlignment(Pos.CENTER_LEFT);
	}
	
	private void bindProperties() {
		label.prefWidthProperty().bind(widthProperty().divide(2));
		label.prefHeightProperty().bind(heightProperty());
		button.prefWidthProperty().bind(widthProperty().divide(2));
		button.prefHeightProperty().bind(heightProperty());
	}
	
	public void setTooltip(Tooltip tooltip){
		label.setTooltip(tooltip);
		button.setTooltip(tooltip);
	}
}