package me.clip.ezblocks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

public class EZBlocksConfig {
	
	
	private EZBlocks plugin;
	
	
	public EZBlocksConfig(EZBlocks i) {
		plugin = i;

	}
	
	protected void loadConfigurationFile() {
		FileConfiguration c = plugin.getConfig();
		c.options().header("EZBlocks version "+plugin.getDescription().getVersion()+" Main configuration file.");
		c.addDefault("save_interval", 5);
		c.addDefault("use_pickaxe_counter", true);
		c.addDefault("pickaxe_never_breaks", true);
		c.addDefault("only_track_below_y.enabled", false);
		c.addDefault("only_track_below_y.coord", 50);
		c.addDefault("enabled_worlds", Arrays.asList(new String[] {
				"world", "world_nether", "all"
		}));
		c.addDefault("global_rewards.default.blocks_needed", 100);
		c.addDefault("global_rewards.default.reward_commands", Arrays.asList(new String[] {
				"eco give %player% 100", "ezmsg &bCongrats on your first 100 blocks!"
		}));
		c.options().copyDefaults(true);
		c.addDefault("blocks_broken_message", "&bYou have broken &e%blocksbroken%&b blocks!");
		plugin.saveConfig();
	}
	
	protected int loadGlobalRewards() {
		FileConfiguration c = plugin.getConfig();
		RewardHandler.rewards = new HashMap<Integer, Reward>();
		if (c.isConfigurationSection("global_rewards")) {
			
			Set<String> keys = c.getConfigurationSection("global_rewards").getKeys(false);
			
			if (keys == null || keys.isEmpty()) {
				return 0;
			}
			
			for (String id : keys) {
				Reward r = new Reward(id);
				r.setBlocksNeeded(c.getInt("global_rewards."+id+".blocks_needed"));
				r.setCommands(c.getStringList("global_rewards."+id+".reward_commands"));
				plugin.rewards.setReward(r.getBlocksNeeded(), r);
			}
			return RewardHandler.rewards.size();			
		}
		return 0;
		
	}

}
