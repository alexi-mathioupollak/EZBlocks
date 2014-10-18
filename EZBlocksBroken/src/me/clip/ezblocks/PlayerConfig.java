package me.clip.ezblocks;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerConfig {

	private EZBlocks plugin;
	private FileConfiguration dataConfig = null;
	private File dataFile = null;

	public PlayerConfig(EZBlocks i) {
		this.plugin = i;
	}

	public void reload() {
		if (this.dataFile == null) {
			this.dataFile = new File(this.plugin.getDataFolder(), "stats.yml");
		}
		this.dataConfig = YamlConfiguration.loadConfiguration(this.dataFile);
	}

	public FileConfiguration load() {
		if (this.dataConfig == null) {
			reload();
		}
		return this.dataConfig;
	}

	public void save() {
		if ((this.dataConfig == null) || (this.dataFile == null))
			return;
		try {
			load().save(this.dataFile);
		} catch (IOException ex) {
			this.plugin.getLogger().log(Level.SEVERE,
					"Could not save to " + this.dataFile, ex);
		}
	}

	public Set<String> getAllEntries() {
		return this.dataConfig.getKeys(false);
	}

	public void savePlayer(String uuid, int broken) {
		this.dataConfig.set(uuid + ".blocks_broken", broken);
		save();
	}

	public int getBlocksBroken(String uuid) {
		return this.dataConfig.getInt(uuid + ".blocks_broken");
	}

	public boolean hasData(String uuid) {
		return this.dataConfig.contains(uuid + ".blocks_broken")
				&& this.dataConfig.isInt(uuid + ".blocks_broken");
	}

}
