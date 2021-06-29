package main.statGenerator;

import java.util.ArrayList;
import java.util.regex.Pattern;

import main.dieRoller.Dice;
import main.dieRoller.RawRoll;
import main.dieRoller.RollStats;
import main.utils.Log;

public class StatGenerator {
	
	private RollStats statistics;
	
	public void generate(){
		ArrayList<Integer> stats = new ArrayList<>();
		
		for(int i = 0; i < 6; i++){
			int lowest = Integer.MAX_VALUE, total = 0;
			
			for(int j = 0; j < 4; j++){
				int roll = Dice.D6.roll();
				if(roll < lowest)
					lowest = roll;
				total += roll;
			}
			total -= lowest;
			
			stats.add(total);
		}
		ArrayList<RawRoll> list = new ArrayList<>();
		list.add(new RawRoll(stats, 0, false));
		
		setStatistics(new RollStats(list));
		
		Log.logRoll("Stat Generation: \t" + getFormattedDataString(getStatistics().getDataString()));
	}
	
	private String getFormattedDataString(String dataString){
		return dataString.substring(0, dataString.indexOf(']') + 1).replaceAll(Pattern.quote(" +"), ",");
	}

	public RollStats getStatistics() {
		return statistics;
	}

	public void setStatistics(RollStats statistics) {
		this.statistics = statistics;
	}
}
