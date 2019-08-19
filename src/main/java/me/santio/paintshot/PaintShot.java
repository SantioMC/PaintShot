package me.santio.paintshot;

import me.santio.paintshot.Kits.ExtraWoolKit;
import me.santio.paintshot.Kits.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class PaintShot extends JavaPlugin implements Listener {
    
    int timer = 180;
    public static PaintShot instance;

    public HashMap<String, KitManager> kits = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        // Register Commands
        this.getCommand("join").setExecutor(new JoinCommandExecutor());
        this.getCommand("kit").setExecutor(new JoinCommandExecutor());
        this.getCommand("kits").setExecutor(new JoinCommandExecutor());
        this.getCommand("play").setExecutor(new JoinCommandExecutor());
        this.getCommand("enter").setExecutor(new JoinCommandExecutor());

        // Register Kits
        ExtraWoolKit ExtraWool = new ExtraWoolKit();
        ExtraWool.createKit();

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

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!isSet(player.getUniqueId()+".wins")) saveDefaultValues(player);
        event.setJoinMessage(ChatColor.DARK_GRAY+"["+ChatColor.GREEN+"+"+ChatColor.DARK_GRAY+"]"+ChatColor.AQUA+" "+player.getDisplayName());
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(ChatColor.DARK_GRAY+"["+ChatColor.RED+"-"+ChatColor.DARK_GRAY+"]"+ChatColor.AQUA+" "+player.getDisplayName());
    }

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
           Player player = (Player) event.getEntity();
           player.setFoodLevel(20);
           event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            if (!(attacker.getItemInHand().getType() == Material.WOODEN_SWORD)) {
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.RED+""+ChatColor.BOLD+"Select your kit.")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED+"This feature does not exist yet!");
        }
    }

    public void openJoinGui(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 18, ChatColor.RED+""+ChatColor.BOLD+"Select your kit.");

        for (Map.Entry entry : kits.entrySet()) {
            KitManager gotKit = (KitManager) entry.getValue();
            ItemStack logo = gotKit.getLogo();
            ItemMeta logoMeta = logo.getItemMeta();
            logoMeta.setDisplayName(ChatColor.GOLD+gotKit.getName());
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.LIGHT_PURPLE+"- "+ChatColor.GRAY+gotKit.getDescription());
            logoMeta.setLore(lore);
            logo.setItemMeta(logoMeta);
            inventory.addItem(logo);
        }

        player.openInventory(inventory);
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {

                ScoreboardManager manager = Bukkit.getScoreboardManager();
                Scoreboard sidebar = manager.getNewScoreboard();
                Objective obj = sidebar.registerNewObjective("Scoreboard", "dummy");

                obj.setDisplayName("  §b§lPaintShot §8[§e" + Bukkit.getOnlinePlayers().size() + "§8/§e" + Bukkit.getServer().getMaxPlayers() + "§8]");
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);

                timer--;

                obj.getScore("§aTime Left:").setScore(timer);
                obj.getScore(" ").setScore(-1);
                obj.getScore("§9/join").setScore(-2);
                obj.getScore("§bServer IP in here idek.").setScore(-3);
                e.getPlayer().setScoreboard(sidebar);
            }
        }.runTaskTimer(this, 1, 20); //20 incase is the amount of ticks. 20 = 1 second
    }

}
