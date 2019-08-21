package me.santio.paintshot;

import me.santio.paintshot.Arenas.ArenaCommand;
import me.santio.paintshot.Arenas.ArenaManager;
import me.santio.paintshot.Kits.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class PaintShot extends JavaPlugin implements Listener {


    int timer;
    public static PaintShot instance;

    public HashMap<String, KitManager> kits = new HashMap<>();
    public HashMap<String, ArenaManager> arenas = new HashMap<>();
    public String currentArena;

    @Override
    public void onEnable() {

        // Global variables
        instance = this;
        timer = 180; // Allows the plugin to be reloaded

        // Setup Configuration
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        updateArenas();

        // Set Arena and Reset

        currentArena = getRandomArena();
        arenas.get(currentArena).resetMap();

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

        this.getCommand("resetmap").setExecutor(new ResetMapCommandExecutor());
        this.getCommand("reset").setExecutor(new ResetMapCommandExecutor());

        this.getCommand("arena").setExecutor(new ArenaCommand());

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

        // Create Hologram

        Location location = new Location(Bukkit.getWorld("default"), 0, 81, 0);
        createHologram(location, ChatColor.BLUE+"/join");

    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public void updateArenas() {
        arenas.clear();
        ConfigurationSection sec = getConfig().getConfigurationSection("arena");

        for(String key : sec.getKeys(false)){
            Location blueSpawn = new Location(Bukkit.getWorld("default"), getConfig().getInt("arena."+key+".blueSpawn.x"), getConfig().getInt("arena."+key+".blueSpawn.y"), getConfig().getInt("arena."+key+".blueSpawn.z"));
            Location redSpawn = new Location(Bukkit.getWorld("default"), getConfig().getInt("arena."+key+".redSpawn.x"), getConfig().getInt("arena."+key+".redSpawn.y"), getConfig().getInt("arena."+key+".redSpawn.z"));
            Location center = new Location(Bukkit.getWorld("default"), getConfig().getInt("arena."+key+".center.x"), getConfig().getInt("arena."+key+".center.y"), getConfig().getInt("arena."+key+".center.z"));
            ArenaManager arena = new ArenaManager(key, getConfig().getBoolean("arena."+key+".restricted"), blueSpawn, redSpawn, center);
            arenas.put(key, arena);
        }
    }

    public void updateScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard sidebar = manager.getNewScoreboard();
        Objective obj = sidebar.registerNewObjective("Scoreboard", "dummy");


        obj.setDisplayName("  §b§lPaintShot §8[§6" + Bukkit.getOnlinePlayers().size() + "§8/§6" + Bukkit.getServer().getMaxPlayers() + "§8]");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        obj.getScore("§aTime Left:").setScore(timer);
        obj.getScore(" ").setScore(-1);
        obj.getScore("§9/join").setScore(-2);
        obj.getScore("§bServer IP in here idek.").setScore(-3);
        player.setScoreboard(sidebar);
    }

    public String getRandomArena() {
        Object[] gotArenas = arenas.keySet().toArray();
        String key = (String) gotArenas[new Random().nextInt(gotArenas.length)];
        if (arenas.get(key).isRestricted()) getRandomArena();
        return key;
    }

    public void giveKit(Player player, KitManager kit) {
        player.getInventory().clear();
        player.getActivePotionEffects().clear();
        for (ItemStack item : kit.getItems()) {
            player.getInventory().addItem(item);
        }
        if (kit.getPotion() != null) {
            player.addPotionEffect(new PotionEffect(kit.getPotion(), 1000000,0));
        }
        player.getInventory().addItem(new ItemStack(Material.WHITE_WOOL, kit.getWoolCount()));
    }

    public void createHologram(Location location, String message) {
        ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        as.setGravity(false);
        as.setCanPickupItems(false);
        as.setCustomName(message);
        as.setCustomNameVisible(true);
        as.setVisible(false);
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

    public ArrayList<Block> getBlocks(Location start, int radius){
        ArrayList<Block> blocks = new ArrayList<Block>();
        for(double x = start.getX() - radius; x <= start.getX() + radius; x++){
            for(double y = start.getY() - radius; y <= start.getY() + radius; y++){
                for(double z = start.getZ() - radius; z <= start.getZ() + radius; z++){
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

    public void resetMap(Location location) {
        ArrayList<Block> blocks = getBlocks(location,50);
        for (Block block : blocks) {
            if (block.getType() == Material.LIGHT_BLUE_TERRACOTTA) {
                block.setType(Material.WHITE_TERRACOTTA);
            } else if (block.getType() == Material.LIGHT_BLUE_STAINED_GLASS_PANE) {
                block.setType(Material.WHITE_STAINED_GLASS_PANE);
            } else if (block.getType() == Material.LIGHT_BLUE_STAINED_GLASS) {
                block.setType(Material.WHITE_STAINED_GLASS);
            } else if (block.getType() == Material.LIGHT_BLUE_WOOL) {
                block.setType(Material.WHITE_WOOL);
            }
        }
    }



}