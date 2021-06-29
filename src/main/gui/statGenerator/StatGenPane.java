package main.gui.statGenerator;

import main.gui.GUI;

import java.util.regex.Pattern;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StatGenPane extends VBox {
	
	private GUI gui;
	
	private VBox statsPane;
	private StatLabel[] results;
	private Label average;
	private Label highest;
	private Label lowest;
	private TextArea rolls;
	
	private Button generateButton;
	
	public StatGenPane(GUI gui){
		super(10);
		this.gui = gui;
		this.setPadding(new Insets(10, 0, 10, 0));
		
		this.setAlignment(Pos.CENTER);
		
		generateButton = new Button("Generate Stats");
		generateButton.setTooltip(new Tooltip("Rolls 4d6 (subtracting the lowest) 6 times"));
		HBox.setHgrow(generateButton, Priority.ALWAYS);
		generateButton.setMaxWidth(Double.MAX_VALUE);
		generateButton.setOnAction(e -> {
			generateButton.setDisable(true);
			gui.showIndicator();
			gui.getMan().genreateStats();
		});
		
		initStatsPane();
		
		this.getChildren().addAll(generateButton, statsPane);
	}
	
	public void initStatsPane(){
		statsPane = new VBox(10);
		statsPane.setAlignment(Pos.CENTER);
		
		VBox resultsLayout = new VBox(10);
		Label resultsLabel = new Label("Results: ");
		resultsLabel.setTooltip(new Tooltip("Roll results"));
		resultsLabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 20));
		GridPane resultsLabelLayout = new GridPane();
		resultsLabelLayout.setPadding(new Insets(10));
		results = new StatLabel[6];
		for(int i = 0; i < 6; i++){
			results[i] = new StatLabel();
			results[i].setMajorFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 20));
			results[i].setMinorFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 12));
			results[i].setMinWidth(45);
			results[i].setMinHeight(65);
			results[i].setAlignment(Pos.CENTER);
			resultsLabelLayout.add(results[i], i, 0);
			GridPane.setHgrow(results[i], Priority.ALWAYS);
			GridPane.setHalignment(results[i], HPos.CENTER);
		}
		resultsLayout.getChildren().addAll(resultsLabel, resultsLabelLayout);
		resultsLayout.setAlignment(Pos.CENTER);
		
		HBox statsLayout = new HBox(10);
		Label averageLabel = new Label("Average: ");
		averageLabel.setTooltip(new Tooltip("Average roll"));
		average = new Label();
		Label highestLabel = new Label("Highest: ");
		highestLabel.setTooltip(new Tooltip("Highest roll"));
		highest = new Label();
		Label lowestLabel = new Label("Lowest: ");
		lowestLabel.setTooltip(new Tooltip("Lowest lowest"));
		lowest = new Label();
		statsLayout.getChildren().addAll(averageLabel, average, highestLabel, highest, lowestLabel, lowest);
		statsLayout.setAlignment(Pos.CENTER);
		
		Label rollsLabel = new Label("Roll Values");
		rolls = new TextArea();
		rolls.setEditable(false);
		rolls.setWrapText(true);
		rolls.setMaxWidth(300);
		rolls.setMaxHeight(100);
		rolls.setFont(Font.font("Lucida Console"));
		
		statsPane.getChildren().addAll(resultsLayout, statsLayout, rollsLabel, rolls);
	}
	
	public void showStats(){
		for(int i = 0; i < 6; i++){
			int result = gui.getMan().getStatGen().getStatistics().getRolls().get(0).getRolls().get(i);
			results[i].setValue(result);
			
			double red = 0, green = 0, blue = 30.0 / 255.0;
			double step = 255.0 / 7.0;
			if(result >= 11){
				red = ((step * (18 - result)) / 255.0);
				green = 1.0;
			}else if(result <= 10){
				green = ((step * (result - 3)) / 255.0);
				red = 1.0;
			}
			
			results[i].setBackground(new Background(new BackgroundFill(new Color(red, green, blue, 1.0), null, null)));
		}
		average.setText("" + gui.getMan().getStatGen().getStatistics().getAverage());
		highest.setText("" + gui.getMan().getStatGen().getStatistics().getHighest());
		lowest.setText("" + gui.getMan().getStatGen().getStatistics().getLowest());
		rolls.setText("Roll: \t6x (4d6 - lowest)\n\nOut: \t" + getFormattedDataString(gui.getMan().getStatGen().getStatistics().getDataString()));
		
		gui.hideIndicator();
		generateButton.setDisable(false);
	}
	
	private String getFormattedDataString(String dataString){
		return dataString.substring(0, dataString.indexOf(']') + 1).replaceAll(Pattern.quote(" +"), ",");
	}

}
