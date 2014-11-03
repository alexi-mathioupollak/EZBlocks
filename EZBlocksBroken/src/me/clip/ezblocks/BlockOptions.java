package me.clip.ezblocks;

import java.util.ArrayList;
import java.util.List;

public class BlockOptions {
	
	
	private List<String> enabledWorlds = new ArrayList<String>();
	
	private String brokenMsg;
	private boolean usePickCounter;
	private boolean pickaxeNeverBreaks;
	private boolean onlyBelowY;
	private int belowYCoord;
	
	public BlockOptions(){}


	public List<String> getEnabledWorlds() {
		return enabledWorlds;
	}


	public void setEnabledWorlds(List<String> enabledWorlds) {
		this.enabledWorlds = enabledWorlds;
	}


	public String getBrokenMsg() {
		return brokenMsg;
	}


	public void setBrokenMsg(String brokenMsg) {
		this.brokenMsg = brokenMsg;
	}


	public boolean usePickCounter() {
		return usePickCounter;
	}


	public void setUsePickCounter(boolean usePickCounter) {
		this.usePickCounter = usePickCounter;
	}


	public boolean pickaxeNeverBreaks() {
		return pickaxeNeverBreaks;
	}


	public void setPickaxeNeverBreaks(boolean pickaxeNeverBreaks) {
		this.pickaxeNeverBreaks = pickaxeNeverBreaks;
	}


	public boolean onlyBelowY() {
		return onlyBelowY;
	}


	public void setOnlyBelowY(boolean onlyBelowY) {
		this.onlyBelowY = onlyBelowY;
	}


	public int getBelowYCoord() {
		return belowYCoord;
	}


	public void setBelowYCoord(int belowYCoord) {
		this.belowYCoord = belowYCoord;
	}
	
}
