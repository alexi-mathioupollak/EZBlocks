package me.clip.ezblocks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import me.clip.ezrankslite.EZRanksLite;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BreakHandler implements Listener {

	EZBlocks plugin;

	protected static HashMap<String, Integer> breaks = new HashMap<String, Integer>();

	public BreakHandler(EZBlocks i) {
		plugin = i;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent e) {

		if (EZBlocks.useEZRanks) {
			String uuid = e.getPlayer().getUniqueId().toString();

			if (breaks.containsKey(uuid)) {

				EZRanksLite.getAPI().setPlayerPlaceholder(
						e.getPlayer().getName(), "%blocksbroken%",
						breaks.get(uuid) + "");

			} else {
				EZRanksLite.getAPI().setPlayerPlaceholder(
						e.getPlayer().getName(), "%blocksbroken%", "0");
			}
		}
	}
	
	//leaving priority at default to hopefully not interfere with auto inventory plugins
	@EventHandler(ignoreCancelled = true)
	public void onBreak(BlockBreakEvent e) {

		if (EZBlocks.enabledWorlds.contains(e.getPlayer().getWorld().getName())
				|| EZBlocks.enabledWorlds.contains("all")) {

			ItemStack i = e.getPlayer().getItemInHand();

			if (i.getType().equals(Material.DIAMOND_PICKAXE)
					|| i.getType().equals(Material.GOLD_PICKAXE)
					|| i.getType().equals(Material.IRON_PICKAXE)
					|| i.getType().equals(Material.STONE_PICKAXE)) {

				String uuid = e.getPlayer().getUniqueId().toString();

				int b;

				if (!breaks.containsKey(uuid)) {
					if (plugin.playerconfig.hasData(uuid)) {
						b = plugin.playerconfig.getBlocksBroken(uuid) + 1;
					} else {
						b = 1;
					}

				} else {
					b = breaks.get(uuid) + 1;
				}

				breaks.put(uuid, b);

				if (plugin.rewards.shouldGiveReward(b)) {
					plugin.rewards.giveReward(e.getPlayer());
				}

				if (EZBlocks.useEZRanks) {
					EZRanksLite.getAPI().setPlayerPlaceholder(
							e.getPlayer().getName(), "%blocksbroken%", b + "");
				}

				if(EZBlocks.usePickCounter) {
					
					ItemMeta im = i.getItemMeta();

					if (i.hasItemMeta() && im.hasLore()) {

						List<String> lore = im.getLore();

						boolean contains = false;
						String replace = "";

						for (int l = 0; l < lore.size(); l++) {
							String line = lore.get(l);
							if (line.contains("§7Blocks broken: §e")) {
								contains = true;
								replace = line;
								lore.remove(line);
								break;
							}
						}

						if (contains && replace != null) {
							String num = replace.replace("§7Blocks broken: §e", "");

							if (!isInt(num)) {
								lore.add("§7Blocks broken: §e1");
								im.setLore(lore);
								i.setItemMeta(im);
								return;
							}

							int updated = Integer.parseInt(num.trim()) + 1;
							lore.add("§7Blocks broken: §e" + updated);
							im.setLore(lore);
							i.setItemMeta(im);
							return;

						} else {
							lore.add("§7Blocks broken: §e1");
							im.setLore(lore);
							i.setItemMeta(im);
							return;
						}

					} else {
						List<String> lore = Arrays
								.asList(new String[] { "§7Blocks broken: §e1" });
						im.setLore(lore);
						i.setItemMeta(im);
						return;
					}	
					
					
				}
				
				

			}
		}
	}

	private boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
