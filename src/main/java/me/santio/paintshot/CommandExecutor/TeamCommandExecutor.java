package me.santio.paintshot.CommandExecutor;

import me.santio.paintshot.PaintShot;
import me.santio.paintshot.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommandExecutor implements CommandExecutor {
    PaintShot plugin = PaintShot.instance;

    Teams team = new Teams();


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

                player.sendMessage(ChatColor.RED + "You specified a player but not a team!");
                player.sendMessage(ChatColor.RED + "/team " + target.getName() + "(team)");
                return true;
            } else if(strings.length == 2) {
                if(strings[1].equalsIgnoreCase("red") || strings[1].equalsIgnoreCase("blue")) {
                    switch (strings[1]) {
                        case "red":
                            team.setTeam(player, Teams.Team.RED);
                            player.sendMessage("red");
                            break;
                        case "blue":
                            team.setTeam(player, Teams.Team.BLUE);
                            player.sendMessage("blue");
                            break;
                    }
                }
            }
        }

        return false;
    }
}
