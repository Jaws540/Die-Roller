package main.gui.customNodes;

import javafx.beans.property.SimpleObjectProperty;
import main.dieRoller.Roll;

public class RollProperty extends SimpleObjectProperty<Roll> {
	
	public RollProperty(){
		super();
		set(new Roll(null, null, 0, false, 1));
	}

}
