package main.dieRoller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import main.Manager;

public class Roll implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private List<Dice> dice;
	private List<Integer> modifiers;
	private List<Boolean> addPerDice;
	private List<Integer> numOfDice;
	private RollStats stats;
	
	/**
	 * Creates a Roll object
	 * @param Name - Name of roll
	 * @param die - Type of Die
	 * @param mod - Modifier
	 * @param perDie - Add modifier for each die?
	 * @param numDie - number of die to roll
	 */
	public Roll(String name, Dice die, int mod, boolean perDie, int numDie){
		init();
		
		setName(name);
		dice.add(die);
		modifiers.add(mod);
		addPerDice.add(perDie);
		numOfDice.add(numDie);
	}
	
	public Roll(String name, List<Dice> dice, List<Integer> mods, List<Boolean> perDice, List<Integer> numDice){
		init();
		
		setName(name);
		setDice(dice);
		setModifiers(mods);
		setAddPerDice(perDice);
		setNumOfDice(numDice);
	}
	
	public Roll(Roll roll){
		init();
		
		setName(roll.getName());
		for(Dice d : roll.getDice())
			dice.add(d);
		for(int i : roll.getModifiers())
			modifiers.add(i);
		for(boolean b : roll.isAddPerDice())
			addPerDice.add(b);
		for(int i : roll.getNumOfDice())
			numOfDice.add(i);
	}
	
	private void init(){
		name = "";
		dice = new ArrayList<Dice>();
		modifiers = new ArrayList<Integer>();
		addPerDice = new ArrayList<Boolean>();
		numOfDice = new ArrayList<Integer>();
	}

	public void roll(){
		// Setup output list of RawRolls
		ArrayList<RawRoll> output = new ArrayList<>();
		
		// Iterate of each die
		for(int i = 0; i < dice.size(); i++){
			if(dice.get(i) != null){
				
				// Setup list of raw roll values
				ArrayList<Integer> rolls = new ArrayList<>();
				
				boolean critFail = false;
				boolean critSuccess = false;
				
				// Roll die
				for(int j = 0; j < numOfDice.get(i); j++){
					int rollVal = dice.get(i).roll();
					rolls.add(rollVal);
					
					if(dice.get(i).equals(Dice.D20) && Manager.settings.isCritFail(rollVal) || Manager.settings.isCritSuccess(rollVal)){
						if(Manager.settings.isCritFail(rollVal))
							critFail = true;
						
						if(Manager.settings.isCritSuccess(rollVal))
							critSuccess = true;
					}
				}
				
				// Add RawRoll data to output
				RawRoll raw = new RawRoll(rolls, modifiers.get(i), addPerDice.get(i));
				raw.setD20CritFail(critFail);
				raw.setD20CritSuccess(critSuccess);
				output.add(raw);
			}
		}
		
		// Create statistics for output
		setStats(output);
	}
	
	public void addNewRoll(){
		dice.add(null);
		modifiers.add(0);
		addPerDice.add(false);
		numOfDice.add(1);
	}
	
	public void addRoll(Roll roll){
		setDie(roll.getDie());
		setModifier(roll.getModifier());
		setAddPerDie(roll.isAddPerDie());
		setNumOfDie(roll.getNumOfDie());
		
		addNewRoll();
	}
	
	public void removeRoll(){
		dice.remove(dice.size() - 2);
		modifiers.remove(modifiers.size() - 2);
		addPerDice.remove(addPerDice.size() - 2);
		numOfDice.remove(numOfDice.size() - 2);
	}

	private void setStats(List<RawRoll> rolls) {
		this.stats = new RollStats(rolls);
	}
	
	public void setStats(RollStats s){
		this.stats = s;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public String getDieRaw(){
		String output = "";
		
		for(int i = 0; i < dice.size(); i++){
			if(dice.get(i) != null){
				if(!addPerDice.get(i))
					output += " + " + numOfDice.get(i) + dice.get(i) + (modifiers.get(i) >= 0 ? "+" : "") + modifiers.get(i);
				else
					output += " + " + numOfDice.get(i) + "x(" + dice.get(i) + (modifiers.get(i) >= 0 ? "+" : "") + modifiers.get(i) + ")";
			}
		}
		
		if(dice.get(0) != null)
			return output.substring(3);
		
		return null;
	}
	
	public Roll getCurrentRoll(){
		return new Roll(getName(), getDie(), getModifier(), isAddPerDie(), getNumOfDie());
	}
	
	public boolean equals(Roll r){
		if(dice.size() != r.getDice().size())
			return false;
		
		boolean diceEqual = true;
		boolean modifiersEqual = true;
		boolean numOfDiceEqual = true;
		boolean addPerDiceEqual = true;
		
		for(int i = 0; i < dice.size(); i++){
			if(dice.get(i) != r.getDice().get(i))
				diceEqual = false;
			if(modifiers.get(i) != r.getModifiers().get(i))
				modifiersEqual = false;
			if(numOfDice.get(i) != r.getNumOfDice().get(i))
				numOfDiceEqual = false;
			if(addPerDice.get(i) != r.isAddPerDice().get(i))
				addPerDiceEqual = false;
		}
		
		return diceEqual && modifiersEqual && numOfDiceEqual && addPerDiceEqual;
	}
	
	public String getName() {
		return name;
	}

	public Roll setName(String name) {
		this.name = name;
		
		return this;
	}

	public Dice getDie() {
		return dice.get(0);
	}
	
	public List<Dice> getDice() {
		return dice;
	}

	public Roll setDie(Dice die) {
		this.dice.set(dice.size() - 1, die);
		
		return this;
	}
	
	public Roll setDice(List<Dice> die) {
		this.dice = die;
		
		return this;
	}

	public int getModifier() {
		return modifiers.get(0);
	}

	public List<Integer> getModifiers() {
		return modifiers;
	}

	public Roll setModifier(int modifier) {
		this.modifiers.set(modifiers.size() - 1, modifier);
		
		return this;
	}

	public Roll setModifiers(List<Integer> modifiers) {
		this.modifiers = modifiers;
		
		return this;
	}

	public boolean isAddPerDie() {
		return addPerDice.get(0);
	}

	public List<Boolean> isAddPerDice() {
		return addPerDice;
	}

	public Roll setAddPerDie(boolean addPerDie) {
		this.addPerDice.set(addPerDice.size() - 1, addPerDie);
		
		return this;
	}

	public Roll setAddPerDice(List<Boolean> addPerDice) {
		this.addPerDice = addPerDice;
		
		return this;
	}

	public int getNumOfDie() {
		return numOfDice.get(0);
	}

	public List<Integer> getNumOfDice() {
		return numOfDice;
	}

	public Roll setNumOfDie(int numOfDie) {
		this.numOfDice.set(numOfDice.size() - 1, numOfDie);
		
		return this;
	}

	public Roll setNumOfDice(List<Integer> numOfDice) {
		this.numOfDice = numOfDice;
		
		return this;
	}

	public RollStats getStats() {
		return stats;
	}
	
	public boolean isMultiRoll(){
		return getDice().size() > 1 && getDice().get(getDice().size() - 1) != null;
	}

}
