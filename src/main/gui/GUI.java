package main.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Manager;
import main.gui.customNodes.ToggleSwitch;
import main.gui.dieRoller.DieRollerPane;
import main.gui.statGenerator.StatGenPane;
import main.gui.utils.LogWindow;
import main.gui.utils.SettingsPane;
import main.utils.Resources;

public class GUI {
	
	private Manager man;
	
	private final Stage window;
	
	private StackPane stack;
	
	private DieRollerPane dieRollerPane;
	private StatGenPane statGenPane;
	
	private boolean  rollerOnTop = true;
	
	private ProgressIndicator pi = new ProgressIndicator();
	
	public GUI(Stage window, Manager man){
		setMan(man);
		
		this.window = window;
		window.setTitle(Resources.TITLE + " - " + Resources.VERSION);
		window.setResizable(false);
		
		window.getIcons().add(new Image(this.getClass().getResourceAsStream("/res/icon.png")));
		
		BorderPane layout = new BorderPane();
		layout.setPadding(new Insets(10));
		
		VBox paneSwitchLayout = new VBox(10);
		paneSwitchLayout.setAlignment(Pos.CENTER);
		
		HBox topLayout = new HBox(10);
		
		Button showSettings = new Button("Settings");
		showSettings.setOnAction(e -> {
			SettingsPane sp = new SettingsPane(this);
			sp.showSettings();
		});
		
		ToggleSwitch paneSwitch = new ToggleSwitch("Die Roller", "Stat Gen", "#b2b2b2", "#b2b2b2", false);
		paneSwitch.setTooltip(new Tooltip("Switch between Die Roller and Stat Generator"));
		HBox.setHgrow(paneSwitch, Priority.ALWAYS);
		paneSwitch.switchOnProperty().addListener((observable, oldVal, newVal) -> {
			rollerOnTop = !newVal;
			switchTop();
			window.sizeToScene();
		});
		
		new LogWindow();
		Button showLog = new Button("Log");
		showLog.setOnAction(e -> {
			LogWindow.showWindow();
		});
		
		topLayout.getChildren().addAll(showSettings, paneSwitch, showLog);
		
		paneSwitchLayout.getChildren().addAll(topLayout, new Separator());
		
		stack = new StackPane();
		
		dieRollerPane = new DieRollerPane(this);
		statGenPane = new StatGenPane(this);
        
        addToStack(dieRollerPane);
        
		pi.setProgress(-1);
		pi.setMaxSize(50, 50);
		
		layout.setTop(paneSwitchLayout);
		layout.setCenter(stack);
        
		Scene scene = new Scene(layout);
		scene.getStylesheets().add("res/styles/ListView.css");
		window.setScene(scene);
		
		window.setOnCloseRequest(e -> {
			if(!dieRollerPane.requestSaveDialog()){
				e.consume();
			}
			LogWindow.close();
		});
	}
	
	public void addToStack(Node n){
		stack.getChildren().add(n);
	}
	
	public void removeFromStack(Node n){
		stack.getChildren().remove(n);
	}
	
	public void switchTop(){
		if(rollerOnTop)
			stack.getChildren().set(0, dieRollerPane);
		else
			stack.getChildren().set(0, statGenPane);
	}
	
	public void showIndicator(){
		addToStack(pi);
	}
	
	public void hideIndicator(){
		removeFromStack(pi);
	}

	public Manager getMan() {
		return man;
	}

	public void setMan(Manager man) {
		this.man = man;
	}
	
	public DieRollerPane getDieRollerPane(){
		return dieRollerPane;
	}
	
	public void showStats(){
		if(rollerOnTop){
			dieRollerPane.showStats();
		}else{
			statGenPane.showStats();
		}
	}
	
	public Stage getWindow(){
		return window;
	}
	
	public void show(){
		window.show();
	}

}
