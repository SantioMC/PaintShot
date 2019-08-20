package me.santio.paintshot.Arenas;

import me.santio.paintshot.PaintShot;
import org.bukkit.Location;

public class ArenaManager {

    private PaintShot plugin = PaintShot.instance;

    private String arenaName;
    private boolean restricted;
    private Location blueSpawn;
    private Location redSpawn;
    private Location center;


    public ArenaManager(String name, Boolean restricted, Location blueSpawn, Location redSpawn, Location center) {
        this.arenaName = name;
        this.restricted = restricted;
        this.blueSpawn = blueSpawn;
        this.redSpawn = redSpawn;
        this.center = center;
        plugin.save("arena."+arenaName+".restricted", restricted);
        plugin.save("arena."+arenaName+".blueSpawn", blueSpawn);
        plugin.save("arena."+arenaName+".redSpawn", redSpawn);
        plugin.save("arena."+arenaName+".center", center);
    }

    public void resetMap() {
        plugin.resetMap(center);
    }

    public String getArenaName() {
        return arenaName;
    }

    public void setArenaName(String arenaName) {
        this.arenaName = arenaName;
    }

    public boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    public Location getBlueSpawn() {
        return blueSpawn;
    }

    public void setBlueSpawn(Location blueSpawn) {
        this.blueSpawn = blueSpawn;
    }

    public Location getRedSpawn() {
        return redSpawn;
    }

    public void setRedSpawn(Location redSpawn) {
        this.redSpawn = redSpawn;
    }

    public Location getCenter() {
        return center;
    }

    public void setCenter(Location center) {
        this.center = center;
    }
}
