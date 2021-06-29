package main.utils;

import java.io.Serializable;
import java.util.List;

public class Settings implements Serializable {
	private static final long serialVersionUID = 1L;

	private int[] critSuccess = {20};
	
	private int[] critFail = {1};
	
	public boolean isCritSuccess(int rollVal) {
		boolean found = false;
		
		for(int i : critSuccess) {
			if(i == rollVal) {
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	public boolean isCritFail(int rollVal) {
		boolean found = false;
		
		for(int i : critFail) {
			if(i == rollVal) {
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	public int[] getCritSuccess() {
		return critSuccess;
	}
	
	public int[] getCritFail() {
		return critFail;
	}
	
	public void editCritSuccess(List<Integer> list) {
		critSuccess = new int[list.size()];
		for(int i = 0; i < list.size(); i++) {
			critSuccess[i] = list.get(i);
		}
	}
	
	public void editCritFail(List<Integer> list) {
		critFail = new int[list.size()];
		for(int i = 0; i < list.size(); i++) {
			critFail[i] = list.get(i);
		}
	}

}
