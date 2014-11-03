package me.clip.ezblocks;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RewardHandler {
	
	EZBlocks plugin;
	
	protected static HashMap<Integer, Reward> rewards;
	
	public RewardHandler(EZBlocks i) {
		rewards = new HashMap<Integer, Reward>();
		plugin = i;
	}
	
	public void setReward(int breaks, Reward r) {
		if (rewards == null) {
			rewards = new HashMap<Integer, Reward>();
		}
		rewards.put(breaks, r);
	}
	
	public boolean shouldGiveReward(int breaks) {
		if (rewards == null) {
			return false;
		}
		if (rewards.containsKey(breaks) && rewards.get(breaks) != null) {
			return true;
		}
		return false;
	}
	
	public void giveReward(Player p) {
		
		int breaks = BreakHandler.breaks.get(p.getUniqueId().toString());
		
		Reward r = rewards.get(breaks);
		
		if (r == null) {
			return;
		}
		
		if (r.getCommands() == null || r.getCommands().isEmpty()) {
			return;
		}
		
		for (String cmd : r.getCommands()) {
			
			String f = cmd.replace("%player%", p.getName()).replace("%blocksbroken%", breaks+"");
			
			if (cmd.startsWith("ezmsg")) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', f.replace("ezmsg ", "")));
			}
			else if (cmd.startsWith("ezbroadcast")) {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', f.replace("ezbroadcast ", "")));
			}
			else {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), f);
			}
			
		}
				
	}

}
