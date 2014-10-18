package me.clip.ezblocks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class EZBlocks extends JavaPlugin {
	
	protected PlayerConfig playerconfig = new PlayerConfig(this);
	protected EZBlocksConfig config = new EZBlocksConfig(this);
	protected BreakHandler breakhandler = new BreakHandler(this);
	protected RewardHandler rewards = new RewardHandler(this);
	
	protected static List<String> enabledWorlds = new ArrayList<String>();
	protected static String brokenMsg;
	protected static int saveInterval;
	protected static BukkitTask savetask;
	protected static boolean usePickCounter;
	
	protected static boolean useEZRanks;
	
	protected EZBlocksCommands commands = new EZBlocksCommands(this);
	
	@Override
	public void onEnable() {
		config.loadConfigurationFile();
		loadOptions();
		playerconfig.reload();
		playerconfig.save();
		
		registerListeners();
		startSaveTask();
		getLogger().info(config.loadGlobalRewards()+" global rewards loaded!");
		
		if (hookEZRanksLite()) {
			getLogger().info("Successfully hooked into EZRanksLite!");
		}
		
		getCommand("blocks").setExecutor(commands);
		
	}
	
	private void loadOptions() {
		brokenMsg = getConfig().getString("blocks_broken_message");
		enabledWorlds = getConfig().getStringList("enabled_worlds");
		saveInterval = getConfig().getInt("save_interval");
		usePickCounter = getConfig().getBoolean("use_pickaxe_counter");
	}
	
	protected void reload() {
		stopSaveTask();
		getServer().getScheduler().runTask(this, new IntervalTask(this));
		reloadConfig();
		saveConfig();
		loadOptions();
		startSaveTask();
		getLogger().info(config.loadGlobalRewards()+" global rewards loaded!");

	}
	
	private boolean hookEZRanksLite() {
		if (Bukkit.getServer().getPluginManager().getPlugin("EZRanksLite") != null 
				&& Bukkit.getServer().getPluginManager().getPlugin("EZRanksLite").isEnabled()) {
			useEZRanks = true;
			return true;
		}
		else {
			useEZRanks = false;
			return false;
		}
	}
	
	@Override
	public void onDisable() {
		stopSaveTask();
		if (BreakHandler.breaks != null) {
		for (String uuid : BreakHandler.breaks.keySet()) {
			playerconfig.savePlayer(uuid, BreakHandler.breaks.get(uuid));
		}
		}
		RewardHandler.rewards = null;
		
	}
	
	protected void registerListeners() {
		Bukkit.getServer().getPluginManager().registerEvents(breakhandler, this);
	
	}
	
	private void startSaveTask() {
		if (savetask == null) {
			savetask = getServer().getScheduler().runTaskTimerAsynchronously(this,
					new IntervalTask(this), 1L, ((20L*60L)*saveInterval));
		} else {
			savetask.cancel();
			savetask = null;
			savetask = getServer().getScheduler().runTaskTimerAsynchronously(this,
					new IntervalTask(this), 1L, ((20L*60L)*saveInterval));
		}
	
	}

	private void stopSaveTask() {
	if (savetask != null) {
		savetask.cancel();
		savetask = null;
	}
}
	
	

}
