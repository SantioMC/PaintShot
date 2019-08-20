package me.santio.paintshot;

import me.santio.paintshot.Kits.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class PaintShot extends JavaPlugin implements Listener {


    int timer;
    public static PaintShot instance;

    public HashMap<String, KitManager> kits = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        timer = 180; // Allows the plugin to be reloaded

        // Setup Configuration
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        // Register Events
        Bukkit.getServer().getPluginManager().registerEvents(new EventClass(), this);

        // Register Commands
        this.getCommand("join").setExecutor(new JoinCommandExecutor());
        this.getCommand("kit").setExecutor(new JoinCommandExecutor());
        this.getCommand("kits").setExecutor(new JoinCommandExecutor());
        this.getCommand("play").setExecutor(new JoinCommandExecutor());
        this.getCommand("enter").setExecutor(new JoinCommandExecutor());

        this.getCommand("setspawn").setExecutor(new SetLobbyCommandExecutor());
        this.getCommand("setlobby").setExecutor(new SetLobbyCommandExecutor());

        // Register Kits
        ExtraWoolKit ExtraWool = new ExtraWoolKit();
        ExtraWool.createKit();
        SpeedKit Speed = new SpeedKit();
        Speed.createKit();
        GlassKit Glass = new GlassKit();
        Glass.createKit();
        KnifeKit Knife = new KnifeKit();
        Knife.createKit();

        // Timer Countdown

        new BukkitRunnable() {

            @Override
            public void run() {
                if (timer <= 0) {
                    timer = 180; // Holder
                }
                timer--;
            }
        }.runTaskTimerAsynchronously(this,0,20); // Run each 20 seconds

    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public void save(String key, Object value) {
        this.getConfig().set(key, value);
        saveConfig();
    }

    public Object get(String key) {
        return getConfig().get(key);
    }

    public boolean isSet(String key) {
        return getConfig().isSet(key);
    }

    public void saveDefaultValues(Player player) {
        String key = player.getUniqueId()+".";
        save(key+"wins",0);
        save(key+"loses",0);
        save(key+"ties",0);
        save(key+"painted",0);
        save(key+"blocksPlaced",0);
        save(key+"blocksBroken",0);
        save(key+"kills",0);
        save(key+"deaths",0);
    }

    public void openJoinGui(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 18, ChatColor.RED+""+ChatColor.BOLD+"Select your kit.");

        for (Map.Entry entry : kits.entrySet()) {
            KitManager gotKit = (KitManager) entry.getValue();
            ItemStack logo = gotKit.getLogo();
            ItemMeta logoMeta = logo.getItemMeta();
            logoMeta.setDisplayName(ChatColor.GOLD+gotKit.getName());
            ArrayList<String> lore = new ArrayList<>();
            if (gotKit.getRequiredWins() > 0) lore.add(ChatColor.LIGHT_PURPLE+"- "+ChatColor.AQUA+gotKit.getRequiredWins()+" wins required. (/stats)");
            lore.add(ChatColor.LIGHT_PURPLE+"- "+ChatColor.GRAY+gotKit.getDescription());
            logoMeta.setLore(lore);
            logo.setItemMeta(logoMeta);
            inventory.addItem(logo);
        }

        player.openInventory(inventory);
    }

    public ArrayList<Block> getBlocks(Block start, int radius){
        ArrayList<Block> blocks = new ArrayList<Block>();
        for(double x = start.getLocation().getX() - radius; x <= start.getLocation().getX() + radius; x++){
            for(double y = start.getLocation().getY() - radius; y <= start.getLocation().getY() + radius; y++){
                for(double z = start.getLocation().getZ() - radius; z <= start.getLocation().getZ() + radius; z++){
                    Location loc = new Location(start.getWorld(), x, y, z);
                    blocks.add(loc.getBlock());
                }
            }
        }
        return blocks;
    }

    public boolean getChance(int chance) {
        Random random = new Random();
        return random.nextInt(99) + 1 < chance;
    }



}