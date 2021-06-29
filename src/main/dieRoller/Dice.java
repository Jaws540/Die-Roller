package main.dieRoller;

import java.util.Random;

public enum Dice {
	NULL(0), D2(2), D4(4), D6(6), D8(8), D10(10), D12(12), D20(20), D100(100);
	
	private int val;
	
	private Dice(int val){
		this.val = val;
	}
	
	public int getVal(){
		return val;
	}
	
	public int roll(){
		Random die = new Random();
		return die.nextInt(getVal()) + 1;
	}
	
	@Override
	public String toString(){
		return "d" + val;
	}
}
