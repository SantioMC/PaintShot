package me.santio.paintshot;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class EventClass implements Listener {

    PaintShot plugin = PaintShot.instance;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!plugin.isSet(player.getUniqueId()+".wins")) plugin.saveDefaultValues(player);
        if (plugin.isSet("lobbySpawn")) {
            player.teleport((Location) plugin.get("lobbySpawn"));
        }
        player.setGameMode(GameMode.ADVENTURE);
        player.setFlying(false);
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
            player.closeInventory();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {

                ScoreboardManager manager = Bukkit.getScoreboardManager();
                Scoreboard sidebar = manager.getNewScoreboard();
                Objective obj = sidebar.registerNewObjective("Scoreboard", "dummy");


                obj.setDisplayName("  §b§lPaintShot §8[§6" + Bukkit.getOnlinePlayers().size() + "§8/§6" + Bukkit.getServer().getMaxPlayers() + "§8]");
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);

                obj.getScore("§aTime Left:").setScore(plugin.timer);
                obj.getScore(" ").setScore(-1);
                obj.getScore("§9/join").setScore(-2);
                obj.getScore("§bServer IP in here idek.").setScore(-3);
                e.getPlayer().setScoreboard(sidebar);
            }
        }.runTaskTimer(plugin, 1, 20); //20 incase is the amount of ticks. 20 = 1 second
    }
}
