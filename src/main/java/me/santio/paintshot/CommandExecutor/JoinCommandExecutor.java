package me.santio.paintshot.CommandExecutor;

import me.santio.paintshot.PaintShot;
import me.santio.paintshot.Teams;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommandExecutor implements CommandExecutor {

    PaintShot plugin = PaintShot.instance;
    Teams team = plugin.team;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED+"This command can only be executed from in game!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length >= 1) {
            player.sendMessage(ChatColor.RED+"/"+command);
            return true;
        }

        if(!(team.getTeam(player) == Teams.Team.SPECTATOR)) {
            player.sendMessage("You cannot run this when in a game!");
            return true;
        }

        plugin.openJoinGui(player);

        return true;
    }
}
