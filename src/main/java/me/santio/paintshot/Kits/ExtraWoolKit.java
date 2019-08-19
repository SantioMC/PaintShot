package me.santio.paintshot.Kits;

import me.santio.paintshot.PaintShot;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ExtraWoolKit {
    PaintShot plugin = PaintShot.instance;

    public void createKit() {
        ItemStack logo = new ItemStack(Material.WHITE_WOOL, 1);
        KitManager kitManager = new KitManager(0, "Extra Wool", "Gives you 50 wool, instead of 10.",new ArrayList<>(), 50, logo);
        ItemStack pistol = new ItemStack(Material.WOODEN_HOE,1);
        ItemMeta pistolMeta = pistol.getItemMeta();
        pistolMeta.setDisplayName(ChatColor.GOLD+"Pistol");
        pistol.setItemMeta(pistolMeta);
        ArrayList<ItemStack> items = new ArrayList<>();
        items.add(pistol);
        kitManager.setItems(items);
        plugin.kits.put("Extra Wool",kitManager);
    }


}
