package me.santio.paintshot.Kits;

import me.santio.paintshot.PaintShot;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class KnifeKit {
    PaintShot plugin = PaintShot.instance;

    public void createKit() {
        ItemStack logo = new ItemStack(Material.WOODEN_SWORD, 1);
        KitManager kitManager = new KitManager(3, "Knife", "Gives you a knife.",new ArrayList<>(), 10, logo, null);
        ItemStack pistol = new ItemStack(Material.WOODEN_HOE,1);
        ItemMeta pistolMeta = pistol.getItemMeta();
        pistolMeta.setDisplayName(ChatColor.GOLD+"Pistol");
        pistol.setItemMeta(pistolMeta);
        ArrayList<ItemStack> items = new ArrayList<>();
        items.add(pistol);
        ItemStack knife = new ItemStack(Material.WOODEN_SWORD,1);
        ItemMeta knifeMeta = knife.getItemMeta();
        knifeMeta.setDisplayName(ChatColor.GOLD+"Knife");
        knife.setItemMeta(knifeMeta);
        items.add(knife);
        kitManager.setItems(items);
        plugin.kits.put("Knife",kitManager);
    }


}
