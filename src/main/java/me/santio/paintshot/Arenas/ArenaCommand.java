package me.santio.paintshot.Arenas;

import me.santio.paintshot.PaintShot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommand implements CommandExecutor {

    PaintShot plugin = PaintShot.instance;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED+"This command can only be executed from in-game!");
            return true;
        }

        Player player = (Player) sender;

        if (!(player.hasPermission("paintshot.manage"))) {
            player.sendMessage(ChatColor.RED+ "You are not permitted to execute this command!");
            return true;
        }
        if (args.length < 1) {
            player.sendMessage(ChatColor.GRAY+"--------------");
            player.sendMessage(ChatColor.BLUE+"/arena create <name> "+ChatColor.GRAY+"Creates an arena");
            player.sendMessage(ChatColor.BLUE+"/arena setred <name> "+ChatColor.GRAY+"Sets the Red spawn point");
            player.sendMessage(ChatColor.BLUE+"/arena setblue <name> "+ChatColor.GRAY+"Sets the Blue spawn point");
            player.sendMessage(ChatColor.BLUE+"/arena center <name> "+ChatColor.GRAY+"Sets the center");
            player.sendMessage(ChatColor.BLUE+"/arena restrict <name> "+ChatColor.GRAY+"Toggles restricted access");
            player.sendMessage(ChatColor.BLUE+"/arena delete <name> "+ChatColor.GRAY+"Delete an arena");
            player.sendMessage(ChatColor.BLUE+"/arena list "+ChatColor.GRAY+"List all arenas");
            player.sendMessage(ChatColor.GRAY+"--------------");
            return true;
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED+"/arena create <name>");
                return true;
            }
            if (plugin.isSet("arena."+args[1])) {
                player.sendMessage(ChatColor.RED+"That arena already exists!");
                return true;
            }

            Location spawn = new Location(Bukkit.getWorld("default"),0,81,0);
            ArenaManager arena = new ArenaManager(args[1], true, spawn, spawn, spawn);
            player.sendMessage(ChatColor.GREEN+"Arena created! Edit it by using /arena");
            player.sendMessage(ChatColor.RED+"This arena is restricted, use /arena restrict "+args[1]+" to un-restrict it!");
        } else if (args[0].equalsIgnoreCase("setred")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED+"/arena " +args[0]+ " <name>");
                return true;
            }
            if (!(plugin.isSet("arena."+args[1]))) {
                player.sendMessage(ChatColor.RED+"That arena does not exist!");
                return true;
            }
            Location loc = player.getLocation();
            plugin.save("arena."+args[1]+".redSpawn",loc);
            player.sendMessage(ChatColor.GREEN+"Set the red spawn location for "+args[1]+"!");
        } else if (args[0].equalsIgnoreCase("setblue")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED+"/arena " +args[0]+ " <name>");
                return true;
            }
            if (!(plugin.isSet("arena."+args[1]))) {
                player.sendMessage(ChatColor.RED+"That arena does not exist!");
                return true;
            }
            Location loc = player.getLocation();
            plugin.save("arena."+args[1]+".blueSpawn",loc);
            player.sendMessage(ChatColor.GREEN+"Set the blue spawn location for "+args[1]+"!");
        } else if (args[0].equalsIgnoreCase("center")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED+"/arena " +args[0]+ " <name>");
                return true;
            }
            if (!(plugin.isSet("arena."+args[1]))) {
                player.sendMessage(ChatColor.RED+"That arena does not exist!");
                return true;
            }
            Location loc = player.getLocation();
            plugin.save("arena."+args[1]+".center",loc);
            player.sendMessage(ChatColor.GREEN+"Set the center location for "+args[1]+"!");
        } else if (args[0].equalsIgnoreCase("restrict")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED+"/arena " +args[0]+ " <name>");
                return true;
            }
            if (!(plugin.isSet("arena."+args[1]))) {
                player.sendMessage(ChatColor.RED+"That arena does not exist!");
                return true;
            }

            Boolean restricted = (Boolean) plugin.get("arena."+args[1]+".restricted");

            Boolean restrict = !restricted;
            plugin.save("arena."+args[1]+".restricted",restrict);

            player.sendMessage(ChatColor.GREEN+"Set the restricted state for "+args[1]+" to " + (restrict ? "ON" : "OFF"));
        }
        return true;
    }
}
