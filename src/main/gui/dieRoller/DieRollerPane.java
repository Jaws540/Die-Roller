package main.gui.dieRoller;

import java.io.File;
import java.util.List;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import javafx.util.Pair;
import main.dieRoller.Roll;
import main.dieRoller.RollStats;
import main.gui.GUI;
import main.gui.customNodes.ListEditor;
import main.gui.customNodes.Spacer;
import main.gui.customNodes.ToggleSwitch;
import main.gui.utils.AlertSystem;

public class DieRollerPane extends VBox {
	
	private GUI gui;
	
	private VBox statsPane;
	private Label total;
	private Label average;
	private Label highest;
	private Label lowest;
	private TextArea rolls;
	
	private ComboBox<Roll> savedRolls;
	
	private boolean loadedFile = false;
	private boolean edited = false;
	private boolean savedRoll = false;
	
	private RollEditor rollEditor;
	private Button confirmRoll;
	private Button rollSavedRoll;
	
	public DieRollerPane(GUI gui){
		super(10);
		this.setPadding(new Insets(10, 0, 0, 0));
		
		this.gui = gui;
		
		rollEditor = new RollEditor(false);
        
        confirmRoll = new Button("Roll");
        confirmRoll.setTooltip(new Tooltip("Rolls a die with current settings"));
        confirmRoll.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(confirmRoll, Priority.ALWAYS);
        confirmRoll.setOnAction(e -> {
        	if(rollEditor.confirmDieSelection() && rollEditor.confirmModifierSelection()){
        		gui.getMan().setRoll(rollEditor.getRollProperty().get().getCurrentRoll());
        		roll();
        	}else{
        		if(!rollEditor.confirmModifierSelection())
        			AlertSystem.showAlert(AlertType.ERROR, "Roll Editor Error", "NumberFormatException", "Error with Modifier value");
        		
        		if(!rollEditor.confirmDieSelection())
        			AlertSystem.showAlert(AlertType.ERROR, "Roll Editor Error", null, "Please Select A Die Type");
        	}
        });
        
        rollEditor.getRollProperty().addListener((observable, oldVal, newVal) -> {
        	if(rollEditor.getDie() != null){
        		confirmRoll.setText("Roll " + rollEditor.getRollProperty().get().getCurrentRoll().getDieRaw());
        	}else
        		confirmRoll.setText("Roll");
        });
        rollEditor.advancedRollActionProperty().set(e -> {
        	gui.getMan().setRoll(rollEditor.advancedRollProperty().get());
        	roll();
        });
        rollEditor.advancedRollSaveActionProperty().set(e ->{
        	System.out.println("Saving...");
        	saveRoll(rollEditor.advancedRollProperty().get());
        	rollEditor.saveRoutine();
        });
        
        
        // Saved rolls and additional controls
        
        savedRolls = new ComboBox<>();
        
        // ADDITIONAL CONTROLS
        HBox additionalControlsLayout = new HBox(10);
        
        Label advancedControlsLabel = new Label("Advanced Controls: ");
        
        ToggleSwitch advancedControls = new ToggleSwitch();
        advancedControls.setTooltip(new Tooltip("ON - Show extra controls\nOFF - Hide extra controls"));
        advancedControls.switchOnProperty().addListener((observable, oldVal, newVal) -> {
        	rollEditor.toggleAdvanced(newVal);
        	gui.getWindow().sizeToScene();
        });
        
        Button saveRoll = new Button("Save Roll");
        saveRoll.setTooltip(new Tooltip("Saves current roll to the Saved Rolls list"));
        saveRoll.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(saveRoll, Priority.ALWAYS);
        saveRoll.setOnAction(e -> {
        	if(rollEditor.confirmDieSelection() && rollEditor.confirmModifierSelection()) 
        		saveRoll(rollEditor.getRollProperty().get());
        	else{
        		if(!rollEditor.confirmModifierSelection())
        			AlertSystem.showAlert(AlertType.ERROR, "Roll Editor Error", "NumberFormatException", "Error with Modifier value");
        		
        		if(!rollEditor.confirmDieSelection())
        			AlertSystem.showAlert(AlertType.ERROR, "Roll Editor Error", null, "Please Select A Die Type");
        	}
        });
        
        additionalControlsLayout.getChildren().addAll(advancedControlsLabel, advancedControls, saveRoll);
        // END OF ADDITIONAL CONTROLS
        
        // SAVED ROLLS
        VBox savedRollsLayout = new VBox(10);
        savedRollsLayout.setAlignment(Pos.CENTER);
        
        HBox savedRollsSelectorLayout = new HBox(10);
        savedRollsSelectorLayout.setAlignment(Pos.CENTER);
        
        Label savedRollsLabel = new Label("Saved Rolls: ");
        
        savedRolls.setPromptText("Saved Rolls");
        savedRolls.setTooltip(new Tooltip("Select a Saved Roll"));
        savedRolls.setMaxWidth(162);
        savedRolls.setVisibleRowCount(15);
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
		                	setPadding(new Insets(1, 5, 1, 5));
		                }
		            }
		        } ;
		    }
		};
		savedRolls.setCellFactory(cellFactory);
        HBox.setHgrow(savedRolls, Priority.ALWAYS);
        
        savedRollsSelectorLayout.getChildren().addAll(savedRollsLabel, savedRolls);
        
        HBox savedRollsButtonLayout = new HBox(10);
        savedRollsButtonLayout.setAlignment(Pos.CENTER);
        
        Button save = new Button("Save");
        HBox.setHgrow(save, Priority.ALWAYS);
        save.setMaxWidth(Double.MAX_VALUE);
        save.setTooltip(new Tooltip("Saves rolls to a file"));
        save.setOnAction(e -> {
        	save();
        });
        
        Button load = new Button("Load");
        HBox.setHgrow(load, Priority.ALWAYS);
        load.setMaxWidth(Double.MAX_VALUE); 
        load.setTooltip(new Tooltip("Loads rolls from a file"));
        load.setOnAction(e -> {
        	FileChooser fc = new FileChooser();
        	fc.setTitle("Load Saved Rolls");
        	fc.getExtensionFilters().add(new ExtensionFilter("*.dlst", "*.dlst"));
        	File f = fc.showOpenDialog(gui.getWindow());
        	boolean success = false;
        	if(f != null)
        		success = gui.getMan().load(f);
        	if(success){
        		loadedFile = true;
    			setSavedRolls();
        	}
        });
        
        Button clear = new Button("Clear Saves");
        HBox.setHgrow(clear, Priority.ALWAYS);
        clear.setMaxWidth(Double.MAX_VALUE);
        clear.setTooltip(new Tooltip("Clears the Saved Rolls List"));
        clear.setOnAction(e -> {
        	if(requestSaveDialog()){
	        	gui.getMan().getSavedRolls().clear();
	        	savedRolls.getItems().clear();
	        	loadedFile = false;
	        	edited = false;
	        	savedRoll = false;
        	}
        });
        
        Button edit = new Button("Edit");
        HBox.setHgrow(edit, Priority.ALWAYS);
        edit.setMaxWidth(Double.MAX_VALUE);
        edit.setTooltip(new Tooltip("Edit List Order and Item Names"));
        edit.setOnAction(e -> {
        	ListEditor le = new ListEditor(gui.getMan().getSavedRolls());
        	Optional<Pair<List<Roll>, Boolean>> result = le.showAndWait();
        	if(result.isPresent()){
        		List<Roll> newSavedRolls = result.get().getKey();
        		edited = result.get().getValue();
        		gui.getMan().setSavedRolls(newSavedRolls);
        	}
			setSavedRolls();
        });
        
        savedRollsButtonLayout.getChildren().addAll(save, load, clear, edit);
        
        rollSavedRoll = new Button("Roll");
        rollSavedRoll.setTooltip(new Tooltip("Roll selected Saved Roll"));
        rollSavedRoll.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(rollSavedRoll, Priority.ALWAYS);
        rollSavedRoll.setOnAction(e -> {
        	if(savedRolls.getValue() != null){
        		gui.getMan().rollSavedDie(savedRolls.getValue());
        		confirmRoll.setDisable(true);
        		rollSavedRoll.setDisable(true);
        		rollEditor.disableRollButton();
        		gui.showIndicator();
        	}else{
        		savedRolls.requestFocus();
        	}
        });

		savedRolls.valueProperty().addListener((observable, oldVal, newVal) -> {
			if(newVal != null){
				savedRolls.setTooltip(new Tooltip(newVal.getDieRaw()));
				rollSavedRoll.setText("Roll " + newVal.getDieRaw());
			}else{
				savedRolls.setTooltip(new Tooltip("Select a Saved Roll"));
				rollSavedRoll.setText("Roll");
			}
		});
        
        savedRollsLayout.getChildren().addAll(savedRollsSelectorLayout, rollSavedRoll, savedRollsButtonLayout);
        // END OF SAVED ROLLS

        initStatsPane();
        this.getChildren().addAll(rollEditor, confirmRoll, additionalControlsLayout, new Separator(), savedRollsLayout, new Separator(), statsPane);
	}
	
	public boolean requestSaveDialog(){
		if((loadedFile && edited) || savedRoll){
			Alert confirmClose = new Alert(AlertType.WARNING);
			ButtonType save = new ButtonType("Save", ButtonData.YES);
			confirmClose.getButtonTypes().clear();
			confirmClose.getButtonTypes().addAll(save, ButtonType.CLOSE, ButtonType.CANCEL);
			confirmClose.setTitle("Work Saver");
			confirmClose.setHeaderText("You Have Unsaved Work");
			confirmClose.setContentText("Would you like to save your saved rolls to a file?");
			
			Optional<ButtonType> result = confirmClose.showAndWait();
			
			if(result.isPresent()){
				if(result.get().equals(save)){
					save();
				}else if(result.get().equals(ButtonType.CLOSE)){
					return true;
				}else if(result.get().equals(ButtonType.CANCEL)){
					return false;
				}
			}
		}
		
		return true;
	}
	
	public void save(){
		FileChooser fc = new FileChooser();
		fc.setTitle("Save Rolls To File");
		fc.getExtensionFilters().add(new ExtensionFilter("*.dlst", "*.dlst"));
		File f = fc.showSaveDialog(gui.getWindow());
		if(f != null)
			gui.getMan().save(f);
		edited = false;
		savedRoll = false;
	}
	
	public void saveRoll(Roll roll){
		TextInputDialog saveNameInput = new TextInputDialog();
		saveNameInput.setTitle("Save Roll");
		saveNameInput.setHeaderText("Saving roll: " + roll.getDieRaw());
		saveNameInput.setContentText("Enter Name of Roll: ");
		Optional<String> name = saveNameInput.showAndWait();
		
		if(name.isPresent()){
			roll.setName(name.get());
			gui.getMan().saveRoll(roll);
			
			setSavedRolls();
			
			savedRoll = true;
			
			rollEditor.reset();
		}
	}
	
	public void setSavedRolls(){
		savedRolls.getItems().clear();
		for(Roll r : gui.getMan().getSavedRolls()){
			savedRolls.getItems().add(r);
		}
	}
	
	public void roll(){
		gui.getMan().rollDie();
		confirmRoll.setDisable(true);
		rollSavedRoll.setDisable(true);
		rollEditor.disableRollButton();
		gui.showIndicator();
	}
	
	public RollStats getRollStats(){
		return gui.getMan().getRollStats();
	}
	
	public void initStatsPane(){
		statsPane = new VBox(10);
		statsPane.setAlignment(Pos.CENTER);
		
		HBox totalLayout = new HBox(10);
		Font font = Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 20);
		Label totalLabel = new Label("Total: ");
		totalLabel.setTooltip(new Tooltip("Actual result"));
		totalLabel.setFont(font);
		total = new Label();
		total.setFont(font);
		total.setMinWidth(40);
		total.setAlignment(Pos.CENTER);
		totalLayout.getChildren().addAll(totalLabel, total);
		totalLayout.setAlignment(Pos.CENTER);
		
		HBox statsLayout = new HBox(10);
		Label averageLabel = new Label("Average: ");
		averageLabel.setTooltip(new Tooltip("Average without modifier"));
		average = new Label();
		Label highestLabel = new Label("Highest: ");
		highestLabel.setTooltip(new Tooltip("Highest without modifier"));
		highest = new Label();
		Label lowestLabel = new Label("Lowest: ");
		lowestLabel.setTooltip(new Tooltip("Lowest without modifier"));
		lowest = new Label();
		statsLayout.getChildren().addAll(averageLabel, average, highestLabel, highest, lowestLabel, lowest);
		statsLayout.setAlignment(Pos.CENTER);
		
		Label rollsLabel = new Label("Roll Values");
		rolls = new TextArea();
		rolls.setEditable(false);
		rolls.setWrapText(false);
		rolls.setMaxWidth(300);
		rolls.setMaxHeight(100);
		rolls.setFont(Font.font("Lucida Console"));
		
		statsPane.getChildren().addAll(totalLayout, statsLayout, rollsLabel, rolls);
	}
	
	public void showStats(){
		total.setBackground(null);
		total.setTooltip(null);
		
		if(getRollStats().isD20CritFail() || getRollStats().isD20CritSuccess()){
			if(getRollStats().isD20CritFail() && !getRollStats().isD20CritSuccess()){
				total.setBackground(new Background(new BackgroundFill(new Color(1.0, 30.0 / 255, 30.0 / 255, 1.0), null, null)));
				total.setTooltip(new Tooltip("Crit Fail"));
			}else if(getRollStats().isD20CritSuccess() && !getRollStats().isD20CritFail()){
				total.setBackground(new Background(new BackgroundFill(new Color(30.0 / 255, 1.0, 30.0 / 255, 1.0), null, null)));
				total.setTooltip(new Tooltip("Crit Success"));
			}else if(getRollStats().isD20CritFail() && getRollStats().isD20CritSuccess()){
				total.setBackground(new Background(new BackgroundFill(new Color(1.0, 1.0, 30.0 / 255, 1.0), null, null)));
				total.setTooltip(new Tooltip("Crit Fail AND Crit Success"));
			}
		}
		total.setText("" + gui.getMan().getRollStats().getTotal());
		
		average.setText("" + gui.getMan().getRollStats().getAverage());
		highest.setText("" + gui.getMan().getRollStats().getHighest());
		lowest.setText("" + gui.getMan().getRollStats().getLowest());
		
		rolls.setText("Roll:\t" + gui.getMan().getRoll().getDieRaw() + "\n\nOut:\t" + getRollStats().getDataString());
		
		gui.hideIndicator();
		confirmRoll.setDisable(false);
		rollSavedRoll.setDisable(false);
		rollEditor.enableRollButton();
	}

}
