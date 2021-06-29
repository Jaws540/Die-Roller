package main.gui.statGenerator;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class StatLabel extends VBox {
	
	private Label modLabel;
	private Label valLabel;
	
	public StatLabel(){
		super(3);
		
		modLabel = new Label();
		valLabel = new Label();
		
		modLabel.setTooltip(new Tooltip("Level 1 Character Stat Mod"));
		valLabel.setTooltip(new Tooltip("Level 1 Character Stat"));
		
		this.getChildren().addAll(valLabel, modLabel);
	}
	
	public void setMinorFont(Font font){
		modLabel.setFont(font);
	}
	
	public void setMajorFont(Font font){
		valLabel.setFont(font);
	}
	
	public void setValue(int value){
		String mod = "";
		
		switch(value){
			case 1:
				mod = "-5";
				break;
			case 2:
			case 3:
				mod = "-4";
				break;
			case 4:
			case 5:
				mod = "-3";
				break;
			case 6:
			case 7:
				mod = "-2";
				break;
			case 8:
			case 9:
				mod = "-1";
				break;
			case 10:
			case 11:
				mod = "+0";
				break;
			case 12:
			case 13:
				mod = "+1";
				break;
			case 14:
			case 15:
				mod = "+2";
				break;
			case 16:
			case 17:
				mod = "+3";
				break;
			case 18:
			case 19:
				mod = "+4";
				break;
		}
		
		modLabel.setText(mod);
		
		valLabel.setText("" + value);
	}

}
