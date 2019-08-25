package me.santio.paintshot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArenaTabCompleter  implements TabCompleter {

    PaintShot plugin = PaintShot.instance;
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
        } else if (strings.length == 2) {
            for (Object arenaN : plugin.arenas.keySet().toArray()) {
                String arena = (String) arenaN;
                arenas.add(arena);

            }
            Collections.sort(arenas);
            return arenas;
        } else if(strings.length >= 2) {
            return plugin.Nothing;
        }
        return null;
    }
}
