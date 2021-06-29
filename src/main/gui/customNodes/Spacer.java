package main.gui.customNodes;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class Spacer extends HBox {
	
	public Spacer(){
		this.setMaxWidth(Double.MAX_VALUE);
		this.setMinWidth(25);
		HBox.setHgrow(this, Priority.ALWAYS);
	}

}
