package me.santio.paintshot;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class Teams {
    public static Teams instance;

    public enum Team { RED, BLUE;}
    private Location redspawn, bluespawn;
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

    public boolean hasPlayer(Player p) {
        return players.keySet().contains(p.getName());
    }
}
