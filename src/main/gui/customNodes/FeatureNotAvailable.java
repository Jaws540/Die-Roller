package main.gui.customNodes;

import javafx.scene.control.Alert;

public class FeatureNotAvailable extends Alert {

	public FeatureNotAvailable() {
		super(AlertType.ERROR);
		this.setTitle("Error");
		this.setHeaderText(null);
		this.setContentText("Feature Currently Unavailable");
	}

}
