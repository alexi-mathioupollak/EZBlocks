package me.clip.ezblocks;



import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EZBlocksCommands implements CommandExecutor {
	
	EZBlocks plugin;
	
	public EZBlocksCommands(EZBlocks i) {
		plugin = i;
	}
	
	private void sms(CommandSender p, String msg) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
		
		if (!(s instanceof Player)) {
			sms(s, "&cConsole not supported yet!");
			return true;
		}
		
		Player p = (Player) s;
		
		String uuid = p.getUniqueId().toString();

		
		if (args.length == 0) {
			if (BreakHandler.breaks.containsKey(uuid)) {
				sms(s, EZBlocks.brokenMsg.replace("%player%", p.getName())
						.replace("%blocksbroken%", BreakHandler.breaks.get(uuid)+""));
				return true;
			}
			else {
				if (plugin.playerconfig.hasData(uuid)) {
					BreakHandler.breaks.put(uuid, plugin.playerconfig.getBlocksBroken(uuid));
				}
				else {
					BreakHandler.breaks.put(uuid, 0);
				}
				sms(s, EZBlocks.brokenMsg.replace("%player%", p.getName())
						.replace("%blocksbroken%", BreakHandler.breaks.get(uuid)+""));	
			}
			return true;
		}
		else if (args.length > 0) {
			if (args[0].equalsIgnoreCase("help")) {
				if (!p.hasPermission("ezblocks.admin")
						|| !p.hasPermission("ezblocks.check")) {
					sms(s, "&cEZBlocks &7Help");
					sms(s, "&f/blocks &7- &cView your blocks broken");
					return true;
				}
				sms(s, "&cEZBlocks &7Help");
				sms(s, "&f/blocks &7- &cView your blocks broken");
				sms(s, "&f/blocks check <player> &7- &cView others blocks broken");
				sms(s, "&f/blocks version &7- &cView plugin version");
				sms(s, "&f/blocks reload &7- &cReload the ezblocks config");
				return true;
			}
			else if (args[0].equalsIgnoreCase("reload")) {
				if (!p.hasPermission("ezblocks.admin")) {
					sms(s, "&cYou don't have permission to do that!");
					return true;
				}
				plugin.reload();
				sms(s, "&cConfiguration successfully reloaded!");
				return true;
			}
			else if (args[0].equalsIgnoreCase("version")) {
				if (!p.hasPermission("ezblocks.admin")) {
					sms(s, "&cYou don't have permission to do that!");
					return true;
				}
				sms(s, "&cEZBlocks version &f" + plugin.getDescription().getVersion());
				sms(s, "&7Created by: extended_clip");
				return true;
			}
			else if (args[0].equalsIgnoreCase("check")) {
				if (!p.hasPermission("ezblocks.check")) {
					sms(s, "&cYou don't have permission to do that!");
					return true;
				}
				if (args.length >= 2) {
					
					
					@SuppressWarnings("deprecation")
					Player target = Bukkit.getServer().getPlayer(args[1]);
					
					if (target == null) {
						sms(s, "&f"+args[1]+" &cis not online!");
						return true;
					}
					
					String uid = target.getUniqueId().toString();
					
					if (BreakHandler.breaks.containsKey(uid)) {
						sms(s, EZBlocks.brokenMsg.replace("%player%", target.getName())
								.replace("%blocksbroken%", BreakHandler.breaks.get(uid)+""));
						return true;
					}
					else {
						if (plugin.playerconfig.hasData(uid)) {
							BreakHandler.breaks.put(uuid, plugin.playerconfig.getBlocksBroken(uid));
						}
						else {
							BreakHandler.breaks.put(uid, 0);
						}
						sms(s, EZBlocks.brokenMsg.replace("%player%", target.getName())
								.replace("%blocksbroken%", BreakHandler.breaks.get(uid)+""));	
					}
					
					
					
				}
				else {
					sms(s, "&cIncorrect usage! &7/blocks check <player>");
				}
				
				
				
				return true;
			}
			else {
				sms(s, "&cIncorrect usage! &7/blocks help");
			}
			
		}
		
		return true;
	}
}