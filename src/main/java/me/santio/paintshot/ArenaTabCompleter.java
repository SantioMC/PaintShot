package me.santio.paintshot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArenaTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        ArrayList<String> sub = new ArrayList<String>();
        ArrayList<String> arenas = new ArrayList<String>();

        if(strings.length == 1) {
            sub.add("create");
            sub.add("setred");
            sub.add("setblue");
            sub.add("center");
            sub.add("restrict");
            sub.add("delete");
            sub.add("list");
            sub.add("reload");

            Collections.sort(sub);
            return sub;
        }

        return null;
    }
}
