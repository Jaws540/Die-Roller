package main.dieRoller;

import java.io.Serializable;
import java.util.List;

public class RawRoll implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<Integer> rolls;
	private int modifier;
	private boolean addPerDie;
	private int total;
	
	private boolean D20CritFail = false;
	private boolean D20CritSuccess = false;
	
	public RawRoll(List<Integer> rolls, int mod, boolean addPerDie){
		setRolls(rolls);
		setModifier(mod);
		setAddPerDie(addPerDie);
		
		setTotal(0);
		for(int i : rolls){
			setTotal(getTotal() + i);
		}
		
		if(!addPerDie)
			setTotal(getTotal() + modifier);
		else
			setTotal(getTotal() + (modifier * rolls.size()));
	}
	
	public List<Integer> getRolls() {
		return rolls;
	}
	public void setRolls(List<Integer> rolls) {
		this.rolls = rolls;
	}
	public int getModifier() {
		return modifier;
	}
	public void setModifier(int modifier) {
		this.modifier = modifier;
	}
	public boolean isAddPerDie() {
		return addPerDie;
	}
	public void setAddPerDie(boolean addPerDie) {
		this.addPerDie = addPerDie;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
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
