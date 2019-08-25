package me.santio.paintshot.CommandExecutor;

import me.santio.paintshot.Arenas.ArenaManager;
import me.santio.paintshot.PaintShot;
import me.santio.paintshot.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommandExecutor implements CommandExecutor {
    PaintShot plugin = PaintShot.instance;

    Teams team = plugin.team;


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (!(player.hasPermission("paintshot.setteam"))) {
            player.sendMessage(ChatColor.RED+"You are not permitted to execute this command!");
            return true;
        } else {

            if(strings.length < 1) {
                player.sendMessage(ChatColor.RED + "Specify a player and a team!");
                player.sendMessage(ChatColor.RED + "/team (player) (team)");
                return true;
            } else if(strings.length == 1) {
                Player target = Bukkit.getServer().getPlayer(strings[0]);
                if (target != null) {
                    player.sendMessage(ChatColor.RED + "You specified a player but not a team!");
                    player.sendMessage(ChatColor.RED + "/team " + target.getName() + " (team)");
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }
            } else if (strings.length == 2) {
                Player target = Bukkit.getServer().getPlayer(strings[0]);
                if(target != null) {
                    String arena = plugin.currentArena;
                    ArenaManager gotArena = plugin.arenas.get(arena);
                    if (strings[1].equalsIgnoreCase("red") || strings[1].equalsIgnoreCase("r")) {
                        team.setTeam(target, Teams.Team.RED);
                        target.teleport(gotArena.getRedSpawn());
                        player.sendMessage(ChatColor.GREEN + "Player " + target.getName() + ChatColor.GREEN + " is now in" + ChatColor.RED + " Red!");
                        return true;
                    } else if (strings[1].equalsIgnoreCase("blue") || strings[1].equalsIgnoreCase("b")) {
                        team.setTeam(target, Teams.Team.BLUE);
                        target.teleport(gotArena.getBlueSpawn());
                        player.sendMessage(ChatColor.GREEN + "Player " + target.getName() + ChatColor.GREEN + " is now in" + ChatColor.BLUE + " Blue!");
                        return true;
                    } else if (strings[1].equalsIgnoreCase("spectator") || strings[1].equalsIgnoreCase("spec") || strings[1].equalsIgnoreCase("s")) {
                        team.setTeam(target, Teams.Team.SPECTATOR);
                        target.teleport((Location) plugin.get("lobbySpawn"));
                        player.sendMessage(ChatColor.GREEN + "Player " + target.getName() + ChatColor.GREEN + " is now a" + ChatColor.GRAY + " Spectator!");
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + "No Team Found!");
                        return true;
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }
            }
        }

        return false;
    }
}