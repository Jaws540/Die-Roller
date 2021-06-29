package main.gui.dieRoller;

import java.util.regex.Pattern;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.dieRoller.Dice;
import main.dieRoller.Roll;
import main.gui.customNodes.RollProperty;
import main.gui.customNodes.ToggleSwitch;
import main.gui.utils.AlertSystem;

public class RollEditor extends VBox {
	
	private Roll roll;
	private Roll multiRoll;
	
	private ComboBox<Dice> dieSelector;
	private Spinner<Integer> modifierSpinner;
	private ToggleSwitch addPerDieSwitch;
	private Spinner<Integer> numberOfDie;
	
	private boolean editOnly;
	private boolean modifierNumberFormatException = false;
	
	private boolean advanced = false;
	private VBox advancedControls;
	private Label multiRollValueLabel;
	
	private RollProperty rollProperty;
	private RollProperty advancedRollProperty;
	
	private ObjectProperty<EventHandler<ActionEvent>> advancedRollActionProperty;
	private ObjectProperty<EventHandler<ActionEvent>> advancedRollSaveActionProperty;
	
	private Button rollMultiRoll;
	
	public RollEditor(boolean editOnly){
		super(10);
		setEditOnly(editOnly);
		init();
	}
	
	public RollEditor(Roll initRoll, boolean editOnly){
		super(10);
		setEditOnly(editOnly);
		
		if(initRoll.isMultiRoll()){
			advancedRollProperty = new RollProperty();
			advancedRollProperty.set(initRoll);
		}
		
		init();
		if(!initRoll.isMultiRoll()){
			dieSelector.setValue(initRoll.getDie());
			modifierSpinner.getValueFactory().setValue(initRoll.getModifier());
			addPerDieSwitch.switchOnProperty().set(initRoll.isAddPerDie());
			numberOfDie.getValueFactory().setValue(initRoll.getNumOfDie());
		}
	}
	
	private void init(){
		this.setAlignment(Pos.CENTER);
		
		rollProperty = new RollProperty();
		
		if(advancedRollProperty == null)
			advancedRollProperty = new RollProperty();
		
		roll = new Roll(null, null, 0, false, 1);
		multiRoll = new Roll(null, null, 0, false, 1);
		
		HBox dieSelectorLayout = new HBox(10);
		dieSelectorLayout.setAlignment(Pos.CENTER);
		
		Label dieSelectorLabel = new Label("Select A Die: ");
		
		dieSelector = new ComboBox<Dice>();
		dieSelector.setTooltip(new Tooltip("Select Die Type"));
		dieSelector.setPromptText("Select A Die");
		dieSelector.getItems().addAll(Dice.D2, Dice.D4, Dice.D6, Dice.D8, Dice.D10, Dice.D12, Dice.D20, Dice.D100);
		dieSelector.valueProperty().addListener((observable, oldVal, newVal) -> {
			roll.setDie(dieSelector.getValue());
			rollProperty.setValue(new Roll(roll));
		});
		
		dieSelectorLayout.getChildren().addAll(dieSelectorLabel, dieSelector);
		
		HBox additiveLayout = new HBox(10);
		additiveLayout.setAlignment(Pos.CENTER);
		
		Label additiveSpinnerLabel = new Label("Modifier: ");
		
		modifierSpinner = new Spinner<>();
		modifierSpinner.setEditable(true);
		modifierSpinner.setTooltip(new Tooltip("Modifier added to die roll"));
        SpinnerValueFactory<Integer> additiveValueFactory = 
        		new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        modifierSpinner.setValueFactory(additiveValueFactory);
        modifierSpinner.getEditor().textProperty().addListener((observable, oldVal, newVal) -> {
        	if(newVal.replaceAll("\\d*", "").matches("[\\D+][^-]")){
        		modifierSpinner.getEditor().setText(newVal.replaceAll("[\\D+][^-]", ""));
        	}
        	
        	try{
        		modifierSpinner.valueFactoryProperty().getValue().setValue(Integer.parseInt(modifierSpinner.getEditor().getText()));
        		modifierNumberFormatException = false;
        	}catch(NumberFormatException e){
        		System.err.println("NumberFormatException: " + modifierSpinner.getEditor().getText());
        		modifierNumberFormatException = true;
        	}
        });
        modifierSpinner.valueProperty().addListener((observable, oldVal, newVal) -> {
        	if(dieSelector.getValue() != null){
    			roll.setModifier(modifierSpinner.getValue());
    			rollProperty.setValue(new Roll(roll));
        	}
		});
        
        modifierSpinner.focusedProperty().addListener((observable, oldVal, newVal) -> {
        	if(!newVal && modifierSpinner.getEditor().getText().isEmpty()) {
        		modifierSpinner.getEditor().setText("0");
        	}
        	
        	if(!newVal) {
            	try{
            		modifierSpinner.valueFactoryProperty().getValue().setValue(Integer.parseInt(modifierSpinner.getEditor().getText()));
            		modifierNumberFormatException = false;
            	}catch(NumberFormatException e){
            		System.err.println("NumberFormatException: " + modifierSpinner.getEditor().getText());
            		modifierNumberFormatException = true;
            	}
        	}
        });
        
		additiveLayout.getChildren().addAll(additiveSpinnerLabel, modifierSpinner);
		
		HBox switchLayout = new HBox(10);
		switchLayout.setAlignment(Pos.CENTER);
		
		Label switchLabel = new Label("Add modifier per die: ");
        
        addPerDieSwitch = new ToggleSwitch(true);
        addPerDieSwitch.setMaxWidth(75);
        addPerDieSwitch.setTooltip(new Tooltip("ON - Adds modifier to each die\nOFF - Add modifier to total"));
        addPerDieSwitch.switchOnProperty().addListener((observable, oldVal, newVal) -> {
        	if(dieSelector.getValue() != null){
        		roll.setAddPerDie(addPerDieSwitch.switchOnProperty().get());
    			rollProperty.setValue(new Roll(roll));
        	}
		});
        
        switchLayout.getChildren().addAll(switchLabel, addPerDieSwitch);
		
		HBox numRollLayout = new HBox(10);
		numRollLayout.setAlignment(Pos.CENTER);
		
		Label spinnerLabel = new Label("Number of die: ");
		
		numberOfDie = new Spinner<>();
		numberOfDie.setTooltip(new Tooltip("Number of die to roll at once"));
		numberOfDie.setEditable(true);
        SpinnerValueFactory<Integer> valueFactory = 
        		new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1);
        numberOfDie.setValueFactory(valueFactory);
        numberOfDie.getEditor().textProperty().addListener((observable, oldVal, newVal) -> {
        	if(newVal.replaceAll("\\d*", "").matches("\\D+")){
        		numberOfDie.getEditor().setText(newVal.replaceAll("\\D+", ""));
        	}
        	
        	if(!numberOfDie.getEditor().getText().isEmpty())
        		numberOfDie.valueFactoryProperty().getValue().setValue(Integer.parseInt(numberOfDie.getEditor().getText()));
        });
        numberOfDie.focusedProperty().addListener((observable, oldVal, newVal) -> {
        	if(!newVal && numberOfDie.getEditor().getText().isEmpty()){
        		numberOfDie.getEditor().setText("1");
        	}
        });
        numberOfDie.valueProperty().addListener((observable, oldVal, newVal) -> {
        	if(dieSelector.getValue() != null){
        		roll.setNumOfDie(numberOfDie.getValue());
    			rollProperty.setValue(new Roll(roll));
        	}
		});
        
        numRollLayout.getChildren().addAll(spinnerLabel, numberOfDie);
        
        advancedControls = new VBox(10);
        
        HBox multiRollLayout = new HBox(10);
        
        Label multiRollLabel = new Label("MultiRoll: ");
        multiRollValueLabel = new Label();
        if(advancedRollProperty.get().isMultiRoll())
        	multiRollValueLabel.setText(advancedRollProperty.get().getDieRaw());
        multiRollValueLabel.setMaxWidth(Double.MAX_VALUE);
        multiRollLabel.setMinWidth(55);
        HBox.setHgrow(multiRollValueLabel, Priority.ALWAYS);
        
        Button saveMultiRoll = new Button("Save");
        saveMultiRoll.setMinWidth(55);
        advancedRollSaveActionProperty = saveMultiRoll.onActionProperty();
        saveMultiRoll.disableProperty().bind(multiRollValueLabel.textProperty().isEmpty());
        saveMultiRoll.setOnAction(e -> {
        	multiRollValueLabel.setText("");
        	disableRollButton();
        });
        
        if(!isEditOnly())
        	multiRollLayout.getChildren().addAll(multiRollLabel, multiRollValueLabel, saveMultiRoll);
        else
        	multiRollLayout.getChildren().addAll(multiRollLabel, multiRollValueLabel);
        
        HBox multiRollButtonLayout = new HBox(10);

        Button addMultiRoll = new Button("Add");
        addMultiRoll.setOnAction(e -> {
        	if(confirmDieSelection() && confirmModifierSelection()){
	        	multiRoll.addRoll(roll.getCurrentRoll());
	        	reset();
	        	advancedRollProperty.setValue(multiRoll);
	        	multiRollValueLabel.setText(advancedRollProperty.get().getDieRaw());
	        	String tooltip = multiRollValueLabel.getText().replaceAll(Pattern.quote("+ "), " +\n");
	        	Tooltip tt = new Tooltip(tooltip);
	        	tt.setFont(Font.font("Lucida Console", 15));
	        	multiRollValueLabel.setTooltip(tt);
	        	enableRollButton();
        	}else{
        		if(!confirmModifierSelection())
        			AlertSystem.showAlert(AlertType.ERROR, "Roll Editor Error", "NumberFormatException", "Error with Modifier value");
        		
        		if(!confirmDieSelection())
        			AlertSystem.showAlert(AlertType.ERROR, "Roll Editor Error", null, "Please Select A Die Type");
        	}
        });
        
        rollMultiRoll = new Button("Roll MultiRoll");
        rollMultiRoll.setMaxWidth(Double.MAX_VALUE);
        rollMultiRoll.disableProperty().bind(multiRollValueLabel.textProperty().isEmpty());
        HBox.setHgrow(rollMultiRoll, Priority.ALWAYS);
        advancedRollActionProperty = rollMultiRoll.onActionProperty();
        rollMultiRoll.setOnAction(e -> {
        	reset();
        });
        
        Button removeMultiRoll = new Button("Remove");
        removeMultiRoll.disableProperty().bind(multiRollValueLabel.textProperty().isEmpty());
        removeMultiRoll.setOnAction(e -> {
        	advancedRollProperty.get().removeRoll();
        	multiRollValueLabel.setText(advancedRollProperty.get().getDieRaw());
        	if(advancedRollProperty.get().getDice().get(0) != null){
        		String tooltip = multiRollValueLabel.getText().replaceAll(Pattern.quote("+ "), " +\n");
        		Tooltip tt = new Tooltip(tooltip);
        		tt.setFont(Font.font("Lucida Console", 15));
        		multiRollValueLabel.setTooltip(tt);
        	}
        });
        
        addMultiRoll.setMinWidth(55);
        removeMultiRoll.setMinWidth(55);
        if(!isEditOnly())
        	multiRollButtonLayout.getChildren().addAll(addMultiRoll, rollMultiRoll, removeMultiRoll);
        else
        	multiRollButtonLayout.getChildren().addAll(addMultiRoll, removeMultiRoll);
        
        advancedControls.getChildren().addAll(multiRollLayout, multiRollButtonLayout);
        
        this.getChildren().addAll(dieSelectorLayout, additiveLayout, switchLayout, numRollLayout);
	}
	
	public void saveRoutine(){
		advancedRollProperty.set(new Roll(null, null, 0, false, 1));
		multiRollValueLabel.setText("");
	}
	
	public void toggleAdvanced(boolean show){
		advanced = show;
		if(show)
			this.getChildren().add(advancedControls);
		else
			this.getChildren().remove(advancedControls);
	}
	
	public boolean isAdvanced(){
		return advanced;
	}
	
	public void reset(){
		dieSelector.setValue(null);
		modifierSpinner.getValueFactory().setValue(0);
		addPerDieSwitch.switchOnProperty().set(false);
		numberOfDie.getValueFactory().setValue(1);
		
		roll = new Roll(null, null, 0, false, 1);
		rollProperty.setValue(roll);
	}
	
	public boolean confirmDieSelection(){
		return (dieSelector.getValue() != null);
	}
	
	public boolean confirmModifierSelection() {
		return !modifierNumberFormatException;
	}
	
	public void disableRollButton(){
		rollMultiRoll.disableProperty().unbind();
		rollMultiRoll.setDisable(true);
	}
	
	public void enableRollButton(){
        rollMultiRoll.disableProperty().bind(multiRollValueLabel.textProperty().isEmpty());
	}
	
	public void requestDieInput(){
		dieSelector.requestFocus();
	}
	
	public Dice getDie(){
		return dieSelector.getValue();
	}
	
	public int getNumberOfDie(){
		return numberOfDie.getValue();
	}
	
	public boolean getAddPerDie(){
		return addPerDieSwitch.switchOnProperty().get();
	}
	
	public int getModifier(){
		return modifierSpinner.getValue();
	}

	public RollProperty getRollProperty() {
		return rollProperty;
	}

	public void setRollProperty(RollProperty rollProperty) {
		this.rollProperty = rollProperty;
	}
	
	public ObjectProperty<EventHandler<ActionEvent>> advancedRollActionProperty(){
		return advancedRollActionProperty;
	}
	
	public RollProperty advancedRollProperty(){
		return advancedRollProperty;
	}

	public boolean isEditOnly() {
		return editOnly;
	}

	public void setEditOnly(boolean editOnly) {
		this.editOnly = editOnly;
	}

	public ObjectProperty<EventHandler<ActionEvent>> advancedRollSaveActionProperty() {
		return advancedRollSaveActionProperty;
	}

}
