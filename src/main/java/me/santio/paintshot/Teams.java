package me.santio.paintshot;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Teams {
    public static Teams instance;

    public enum Team { RED, BLUE, SPECTATOR }

    private HashMap<String, Team> players = new HashMap<String, Team>();


    public void addPlayer(Player p) {
        players.put(p.getName(), getTeamPlayers());
    }

    public Team getTeam(Player p) {
        return players.get(p.getName());
    }

    public Team getTeamPlayers() {
        int red = 0, blue = 0;
        for(String p : players.keySet()) {
            if(players.get(p) == Team.RED) red++;
            else blue++;
        }
        if (red > blue) return Team.BLUE;
        else return Team.RED;
    }

    public boolean hasPlayer(Player p) { p.sendMessage(ChatColor.BLUE+"[DEBUG] Got team - "+players.toString() + " - "+players.keySet().contains(p.getName())); return players.keySet().contains(p.getName()); }

    public void removePlayer(Player p) {
        setTeam(p, Team.SPECTATOR);
    }

    public void setTeam(Player p, Team team) {
        p.sendMessage(ChatColor.BLUE+"[DEBUG] "+p.getName()+" - "+team.toString());
        players.put(p.getName(), team);
        p.sendMessage(ChatColor.BLUE+"[DEBUG] Added team - "+players.toString());
    }
}
