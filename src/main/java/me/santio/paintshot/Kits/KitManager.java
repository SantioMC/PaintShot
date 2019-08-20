package me.santio.paintshot.Kits;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class KitManager {
    private Integer requiredWins;
    private String name;
    private String description;
    private Integer WoolCount;
    private ArrayList<ItemStack> items;
    private ItemStack logo;
    private PotionEffectType potion;

    public KitManager(Integer requiredWins, String name, String description, ArrayList<ItemStack> items, Integer WoolCount, ItemStack logo, PotionEffectType potion) {
        this.requiredWins = requiredWins;
        this.name = name;
        this.description = description;
        this.items = items;
        this.WoolCount = WoolCount;
        this.logo = logo;
        this.potion = potion;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRequiredWins() {
        return requiredWins;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRequiredWins(int requiredWins) {
        this.requiredWins = requiredWins;
    }

    public ArrayList<ItemStack> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemStack> items) {
        this.items = items;
    }

    public Integer getWoolCount() {
        return WoolCount;
    }

    public void setWoolCount(Integer woolCount) {
        WoolCount = woolCount;
    }

    public ItemStack getLogo() {
        return logo;
    }

    public void setLogo(ItemStack logo) {
        this.logo = logo;
    }

    public PotionEffectType getPotion() {
        return potion;
    }

    public void setPotion(PotionEffectType potion) {
        this.potion = potion;
    }
}
