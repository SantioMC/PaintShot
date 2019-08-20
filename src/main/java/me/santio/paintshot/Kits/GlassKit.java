package me.santio.paintshot.Kits;

import me.santio.paintshot.PaintShot;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GlassKit {
    PaintShot plugin = PaintShot.instance;

    public void createKit() {
        ItemStack logo = new ItemStack(Material.WHITE_STAINED_GLASS, 1);
        KitManager kitManager = new KitManager(0, "Glass", "Gives you 2 stacks of glass.",new ArrayList<>(), 10, logo, null);
        ItemStack pistol = new ItemStack(Material.WOODEN_HOE,1);
        ItemMeta pistolMeta = pistol.getItemMeta();
        pistolMeta.setDisplayName(ChatColor.GOLD+"Pistol");
        pistol.setItemMeta(pistolMeta);
        ArrayList<ItemStack> items = new ArrayList<>();
        items.add(pistol);
        items.add(new ItemStack(Material.GLASS,64));
        items.add(new ItemStack(Material.GLASS,64));
        kitManager.setItems(items);
        plugin.kits.put("Glass",kitManager);
    }


}
