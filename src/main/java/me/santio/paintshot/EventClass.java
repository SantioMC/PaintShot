package me.santio.paintshot;

import me.santio.paintshot.Arenas.ArenaManager;
import me.santio.paintshot.Kits.KitManager;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class EventClass implements Listener {

    PaintShot plugin = PaintShot.instance;

    Teams team = new Teams();

    ArrayList<String> reload = new ArrayList<>();

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
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player player = event.getPlayer();
            if (player.getItemInHand().getType() == Material.WOODEN_HOE) {
                if (!(reload.contains(player.getName()))) {
                    player.playSound(player.getLocation(),Sound.ENTITY_SNOWBALL_THROW,1,1);
                    Snowball snowball = player.getWorld().spawn(player.getEyeLocation(), Snowball.class);
                    snowball.setVelocity(player.getLocation().getDirection().multiply(1.5));
                    snowball.setShooter(player);
                    reload.add(player.getName());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            reload.remove(player.getName());
                        }
                    }.runTaskLater(plugin, 20); // 1 second reload
                }
            }
        } else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();
            if (block.getType().equals(Material.WHITE_STAINED_GLASS) || block.getType().equals(Material.LIGHT_BLUE_STAINED_GLASS) || block.getType().equals(Material.PINK_STAINED_GLASS)) {
                block.setType(Material.AIR);
                event.getPlayer().getInventory().addItem(new ItemStack(Material.WHITE_STAINED_GLASS,1));
            }
        }
    }

    @EventHandler
    public void onLand(ProjectileHitEvent event) {
        if (event.getHitEntity() != null) {
            if (event.getHitEntity() instanceof Player) {
                ((Player) event.getHitEntity()).damage(4);
                event.getEntity().remove();
            }
        }

        if (event.getHitBlock() != null) {
            ArrayList<Block> blocks = plugin.getBlocks(event.getHitBlock().getLocation(),1);
            for (Block block : blocks) {
                if (block.getType() == Material.WHITE_TERRACOTTA) {
                    if (plugin.getChance(75)) block.setType(Material.LIGHT_BLUE_TERRACOTTA);
                } else if (block.getType() == Material.WHITE_STAINED_GLASS_PANE) {
                    if (plugin.getChance(75)) block.setType(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
                } else if (block.getType() == Material.WHITE_STAINED_GLASS) {
                    if (plugin.getChance(75)) block.setType(Material.LIGHT_BLUE_STAINED_GLASS);
                } else if (block.getType() == Material.WHITE_WOOL) {
                    if (plugin.getChance(75)) block.setType(Material.LIGHT_BLUE_WOOL);
                }
            }
        }
    }


    @EventHandler void onAllDamage(EntityDamageEvent event) {
        LivingEntity victim = (LivingEntity) event.getEntity();
        if (victim.getKiller() != null) {
            LivingEntity attacker = victim.getKiller();
            AttributeInstance attribute = attacker.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
            attribute.setBaseValue(21);
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
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
    public void onDeath(PlayerDeathEvent event) {
        new BukkitRunnable() {

            @Override
            public void run() {
                event.getEntity().spigot().respawn();
            }
        }.runTaskLater(plugin, 2);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.RED+""+ChatColor.BOLD+"Select your kit.")) {
            int slot = event.getSlot();
            String gotItem = ChatColor.stripColor(event.getWhoClicked().getInventory().getItem(slot).getItemMeta().getDisplayName());
            KitManager gotKit = plugin.kits.get(gotItem);
            if (!team.hasPlayer(player)) {
                team.addPlayer(player);
                Teams.Team pteam = team.getTeam(player);
                if (pteam == Teams.Team.BLUE) {
                    ItemStack item = new ItemStack(Material.LIGHT_BLUE_TERRACOTTA, 1);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&9&lBlue Team"));
                    item.setItemMeta(meta);
                    player.getInventory().setHelmet(item);
                    player.sendMessage(ChatColor.BLUE +"You have joined the blue team!");
                    String arena = plugin.currentArena;
                    ArenaManager gotArena = plugin.arenas.get(arena);
                    player.teleport(gotArena.getBlueSpawn());
                } else {
                    ItemStack item = new ItemStack(Material.RED_TERRACOTTA, 1);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lRed Team"));
                    item.setItemMeta(meta);
                    player.getInventory().setHelmet(item);
                    player.sendMessage(ChatColor.RED+"You have joined the red team!");
                    String arena = plugin.currentArena;
                    ArenaManager gotArena = plugin.arenas.get(arena);
                    player.teleport(gotArena.getRedSpawn());
                }
            }
            plugin.giveKit(player, gotKit);
            return;
        }
    }

    @EventHandler
    public void manipulate(PlayerArmorStandManipulateEvent e) {
        if(!e.getRightClicked().isVisible()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {

                plugin.updateScoreboard(e.getPlayer());

            }
        }.runTaskTimer(plugin, 1, 20); //20 incase is the amount of ticks. 20 = 1 second
    }
}
