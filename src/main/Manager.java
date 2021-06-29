package main;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Platform;
import javafx.concurrent.Task;
import main.dieRoller.Roll;
import main.dieRoller.RollStats;
import main.gui.GUI;
import main.statGenerator.StatGenerator;
import main.utils.Log;
import main.utils.RollsList;
import main.utils.Settings;
import main.utils.Utils;

public class Manager {
	
	private Roll roll = null;
	
	private RollsList savedRolls = new RollsList();
	
	private StatGenerator statGen;
	
	private GUI gui;
	
	public static Settings settings;
	
	public Manager(){
		setSettings(new Settings());
	}
	
	/**
	 * 
	 * @param die
	 * @param numRolls
	 * @param modifier
	 * @param addPerDie
	 */
	public void rollDie(){
		roll.roll();
		Log.logRoll("Roll: " + roll.getDieRaw() + "\t\tOut: " + roll.getStats().getDataString());
		delay();
	}
	
	public void setRoll(Roll r){
		this.roll = r;
	}
	
	public void saveRoll(Roll roll){
		savedRolls.add(new Roll(roll));
	}
	
	public void printSavedRolls() {
		for(Roll r : savedRolls) {
			System.out.println(r.getName() + " - " + r.getDieRaw());
		}
	}
	
	public void rollSavedDie(Roll die){
		roll = die;
		roll.roll();
		Log.logRoll("Roll: " + roll.getDieRaw() + "\t\tOut: " + roll.getStats().getDataString());
		delay();
	}
	
	public void genreateStats(){
		statGen = new StatGenerator();
		statGen.generate();
		delay();
	}

	public RollStats getRollStats() {
		if(roll != null)
			return roll.getStats();
		else
			return null;
	}
	
	public void save(File file){
		Utils.save(file, savedRolls, settings);
	}
	
	public boolean load(File file){
		ArrayList<Serializable> save = Utils.load(file);
		List<Roll> newSavedRolls = (RollsList) save.get(0);
		Settings newSettings = (Settings) save.get(1);
		
		boolean rolls = false, settings = false;
		
		if(newSavedRolls != null){
			savedRolls.clear();
			setSavedRolls(newSavedRolls);
			rolls = true;
		}
		if(newSettings != null) {
			System.out.println("Loaded Settings");
			setSettings(newSettings);
			settings = true;
		}
		
		return rolls && settings;
	}
	
	public void delay(){
		// Background Task
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            	// Do Stuff Here
            	Random rand = new Random();
                try {
                    Thread.sleep(rand.nextInt(250) + 250);
                } catch (InterruptedException ie) {
                }
                
                // Once Stuff is done
                // Update the GUI on the JavaFX Application Thread
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        gui.showStats();
                    }
                });
				return null;
            }
         };

         // Run the task
         Thread th = new Thread(task);
         th.setDaemon(true);
         th.start();
	}
	
	public Roll getRoll(){
		return roll;
	}

	public RollsList getSavedRolls() {
		return savedRolls;
	}

	public void setSavedRolls(List<Roll> savedRolls) {
		RollsList rollsList = new RollsList();
		for(Roll r : savedRolls){
			rollsList.add(r);
		}
		this.savedRolls = rollsList;
	}

	public StatGenerator getStatGen() {
		return statGen;
	}

	public void setStatGen(StatGenerator statGen) {
		this.statGen = statGen;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		Manager.settings = settings;
	}
	
	public void setGUI(GUI gui) {
		this.gui = gui;
	}

}
