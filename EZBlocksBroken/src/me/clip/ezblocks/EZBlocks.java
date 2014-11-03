package me.clip.ezblocks;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class EZBlocks extends JavaPlugin {
	
	protected PlayerConfig playerconfig = new PlayerConfig(this);
	protected EZBlocksConfig config = new EZBlocksConfig(this);
	protected BreakHandler breakhandler = new BreakHandler(this);
	protected RewardHandler rewards = new RewardHandler(this);
	protected EZBlocksCommands commands = new EZBlocksCommands(this);	

	protected static BlockOptions options;
	
	protected static int saveInterval;
	protected static BukkitTask savetask;
	
	protected static boolean useEZRanks;
	
	private static EZBlocks ezblocks;
	
	@Override
	public void onEnable() {
		
		ezblocks = this;
		
		config.loadConfigurationFile();
		loadOptions();
		playerconfig.reload();
		playerconfig.save();
		registerListeners();
		startSaveTask();
		getCommand("blocks").setExecutor(commands);
		getLogger().info(config.loadGlobalRewards()+" global rewards loaded!");
		
		if (hookEZRanksLite()) {
			getLogger().info("Successfully hooked into EZRanksLite!");
		}
		
	}
	
	private void loadOptions() {
		saveInterval = getConfig().getInt("save_interval");
		options = new BlockOptions();
		options.setBrokenMsg(getConfig().getString("blocks_broken_message"));
		options.setEnabledWorlds(getConfig().getStringList("enabled_worlds"));
		options.setUsePickCounter(getConfig().getBoolean("use_pickaxe_counter"));
		options.setPickaxeNeverBreaks(getConfig().getBoolean("pickaxe_never_breaks"));
		options.setOnlyBelowY(getConfig().getBoolean("only_track_below_y.enabled"));
		options.setBelowYCoord(getConfig().getInt("only_track_below_y.coord"));
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
		ezblocks = null;
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
	
	public int getBlocksBroken(Player p) {
		if (BreakHandler.breaks != null
				&& BreakHandler.breaks.containsKey(p.getUniqueId().toString())) {
			return BreakHandler.breaks.get(p.getUniqueId().toString());
		}
		else {
			return 0;
		}
			
	}

	public static EZBlocks getEZBlocks() {
		return ezblocks;
	}
	

}
