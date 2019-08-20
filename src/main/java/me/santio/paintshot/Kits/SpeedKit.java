package me.santio.paintshot.Kits;

import me.santio.paintshot.PaintShot;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class SpeedKit {
    PaintShot plugin = PaintShot.instance;

    public void createKit() {
        ItemStack logo = new ItemStack(Material.LIGHT_BLUE_TERRACOTTA, 1);
        KitManager kitManager = new KitManager(0, "Speed", "Gives you permanent Speed I.",new ArrayList<>(), 10, logo, PotionEffectType.SPEED);
        ItemStack pistol = new ItemStack(Material.WOODEN_HOE,1);
        ItemMeta pistolMeta = pistol.getItemMeta();
        pistolMeta.setDisplayName(ChatColor.GOLD+"Pistol");
        pistol.setItemMeta(pistolMeta);
        ArrayList<ItemStack> items = new ArrayList<>();
        items.add(pistol);
        kitManager.setItems(items);
        plugin.kits.put("Speed",kitManager);
    }


}
