package me.clip.ezblocks;

import java.util.HashMap;



public class IntervalTask implements Runnable {

	private EZBlocks plugin;
	
	public IntervalTask(EZBlocks instance) {
		plugin = instance;
	}
	
	@Override
	public void run() {
		
		if (BreakHandler.breaks == null || BreakHandler.breaks.isEmpty()) {
			return;
		}
		
		HashMap<String, Integer> save = BreakHandler.breaks;
		
		for (String uuid : save.keySet()) {
			plugin.playerconfig.savePlayer(uuid, save.get(uuid));
		}
		
		//set to debug only option in the future
		System.out.println("[EZBlocks] "+save.size()+" players saved!");
		save = null;
	}
}
