package cl.bgmp.customgapples.gapple;

import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

public class Gapple {
  private String id;
  private String name;
  private List<String> lore;
  private List<PotionEffect> effects;
  private boolean enchanted;
  private boolean lightning;

  public Gapple(
      String id,
      String name,
      List<String> lore,
      List<PotionEffect> effects,
      boolean enchanted,
      boolean lightning) {
    this.id = id;
    this.name = ChatColor.translateAlternateColorCodes('&', name);
    this.lore =
        lore.stream()
            .map(l -> ChatColor.translateAlternateColorCodes('&', l))
            .collect(Collectors.toList());
    this.effects = effects;
    this.enchanted = enchanted;
    this.lightning = lightning;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<String> getLore() {
    return lore;
  }

  public List<PotionEffect> getEffects() {
    return effects;
  }

  public boolean hasLightning() {
    return lightning;
  }

  public ItemStack getItemStack() {
    return this.getItemStack(1);
  }

  public ItemStack getItemStack(int amount) {
    ItemStack item =
        this.enchanted
            ? new ItemStack(Material.GOLDEN_APPLE, amount, (short) 1)
            : new ItemStack(Material.GOLDEN_APPLE, amount);

    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(this.name);
    meta.setLore(this.lore);
    item.setItemMeta(meta);

    return item;
  }
}
