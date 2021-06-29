package main.utils;

import java.util.ArrayList;

import main.gui.utils.LogWindow;

public class Log {
	
	private static ArrayList<String> log = new ArrayList<String>();
	
	public static void logRoll(String rollData){
		log.add(rollData);
		System.out.println(rollData);
		LogWindow.updateLog(rollData);
	}
	
	public static ArrayList<String> getLog(){
		return log;
	}

}
