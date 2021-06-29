package main.dieRoller;

import java.io.Serializable;
import java.util.List;

public class RollStats implements Serializable, Statistics {
	private static final long serialVersionUID = 1L;
	
	private int total = 0;
	private int average = 0;
	private int highest = Integer.MIN_VALUE;
	private int lowest = Integer.MAX_VALUE;
	private List<RawRoll> rolls;
	
	private boolean D20CritFail = false;
	private boolean D20CritSuccess = false;
	
	public RollStats(List<RawRoll> rolls){
		setRolls(rolls);
		
		int numRolls = 0;
		
		for(RawRoll roll : rolls){
			total += roll.getTotal();
			for(int i : roll.getRolls()){
				numRolls++;
				if(i > highest)
					highest = i;
				if(i < lowest)
					lowest = i;
			}
			
			if(roll.isD20CritFail())
				setD20CritFail(true);
			
			if(roll.isD20CritSuccess())
				setD20CritSuccess(true);
		}
		
		average = Math.round((float) total / (float) numRolls);
	}
	
	public String getDataString(){
		String output = "";
		
		for(RawRoll raw : rolls){
			if(!raw.isAddPerDie()){
				output += " + " + raw.getRolls().toString().replaceAll(",", " +") + (raw.getModifier() >= 0 ? "+" : "") + raw.getModifier();
			}else{
				output += " + " + raw.getRolls().toString().replaceAll(",", (raw.getModifier() >= 0 ? "+" : "") + raw.getModifier() + " +");
				output = output.substring(0, output.length() - 1) + (raw.getModifier() >= 0 ? "+" : "") + raw.getModifier() + "]";
			}
		}
		
		return output.substring(3) + " = " + getTotal();
	}

	public int getTotal() {
		return total;
	}

	public int getAverage() {
		return average;
	}

	public void setAverage(int average) {
		this.average = average;
	}

	public int getHighest() {
		return highest;
	}

	public void setHighest(int highest) {
		this.highest = highest;
	}

	public int getLowest() {
		return lowest;
	}

	public void setLowest(int lowest) {
		this.lowest = lowest;
	}

	public List<RawRoll> getRolls() {
		return rolls;
	}
	
	public RawRoll getLatestRoll(){
		return rolls.get(rolls.size() - 1);
	}

	public void setRolls(List<RawRoll> rolls) {
		this.rolls = rolls;
	}

	public boolean isD20CritFail() {
		return D20CritFail;
	}

	public void setD20CritFail(boolean d20CritFail) {
		D20CritFail = d20CritFail;
	}

	public boolean isD20CritSuccess() {
		return D20CritSuccess;
	}

	public void setD20CritSuccess(boolean d20CritSuccess) {
		D20CritSuccess = d20CritSuccess;
	}

}
