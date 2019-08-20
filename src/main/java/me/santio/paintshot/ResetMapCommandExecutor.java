package me.santio.paintshot;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetMapCommandExecutor implements CommandExecutor {

    PaintShot plugin = PaintShot.instance;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED+"This command can only be executed in-game!");
            return true;
        }

        Player player = (Player) sender;

        if (!(player.hasPermission("paintshot.reset"))) {
            player.sendMessage(ChatColor.RED+"You are not permitted to execute this command!");
            return true;
        }
        plugin.resetMap(player);
        player.sendMessage(ChatColor.GREEN+"Map reset!");
        return true;
    }
}
