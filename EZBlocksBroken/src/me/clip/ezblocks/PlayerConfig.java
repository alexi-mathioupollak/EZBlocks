package me.clip.ezblocks;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
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
		if (plugin.getConfig().getBoolean("database.enabled")
				&& EZBlocks.database != null) {
			// Save to MySQL

			// If the player is not in the database we have to do an INSERT
			// If he is in the database we have to do an UPDATE

			// So quickly check if the player is in the database
			try {
				String query = "SELECT blocksmined FROM `"
						+ EZBlocks.database.getTablePrefix()
						+ "playerblocks` WHERE uuid=?";
				PreparedStatement statement = EZBlocks.database.prepare(query);
				// Prepare statement will prevent sql injections.
				// However its rather save
				// with this data
				statement.setString(1, uuid);
				// Execute select and get the result table
				ResultSet result = statement.executeQuery();
				if (result.next()) {
					// UPDATE
					Bukkit.getLogger().info("UPDATING " + uuid);
					query = "UPDATE `" + EZBlocks.database.getTablePrefix()
							+ "playerblocks` SET blocksmined=?";
					PreparedStatement updateStatement = EZBlocks.database
							.prepare(query);
					// Prepare statement will prevent sql injections.
					// However its rather save
					// with this data
					updateStatement.setInt(0, broken);
					updateStatement.execute();
					updateStatement.close();
				} else {
					// INSERT
					Bukkit.getLogger().info("INSERTING " + uuid);
					query = "INSERT INTO `"
							+ EZBlocks.database.getTablePrefix()
							+ "playerblocks` (uuid,blocksmined) VALUES (?,?)";
					PreparedStatement insertStatement = EZBlocks.database
							.prepare(query);
					// Prepare statement will prevent sql injections.
					// However its rather save
					// with this data
					insertStatement.setString(1, uuid); // First question mark =
														// uuid
					insertStatement.setInt(2, broken); // Seccond question mark
														// = blocks

					insertStatement.execute();
					insertStatement.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			this.dataConfig.set(uuid + ".blocks_broken", broken);
			save();
		}
	}

	public int getBlocksBroken(String uuid) {
		if (plugin.getConfig().getBoolean("database.enabled")
				&& EZBlocks.database != null) {
			// Get data from mysql
			try {
				String query = "SELECT blocksmined FROM `"
						+ EZBlocks.database.getTablePrefix()
						+ "playerblocks` WHERE uuid=?";
				PreparedStatement statement = EZBlocks.database.prepare(query);
				// Prepare statement will prevent sql injections.
				// However its rather save
				// with this data
				statement.setString(1, uuid);
				// Execute select and get the result table
				ResultSet result = statement.executeQuery();
				if (result.next()) {
					return result.getInt(0);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			return this.dataConfig.getInt(uuid + ".blocks_broken");
		}
		return 0;
	}

	public boolean hasData(String uuid) {
		if (plugin.getConfig().getBoolean("database.enabled")
				&& EZBlocks.database != null) {
			return true;
		}
		return this.dataConfig.contains(uuid + ".blocks_broken")
				&& this.dataConfig.isInt(uuid + ".blocks_broken");
	}

}
