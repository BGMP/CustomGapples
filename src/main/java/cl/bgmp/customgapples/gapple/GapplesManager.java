package cl.bgmp.customgapples.gapple;

import cl.bgmp.customgapples.CustomGapples;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;

public class GapplesManager implements Listener {
  private CustomGapples customGapples;
  private Set<Gapple> gapples = new HashSet<>();
  private Cache<UUID, Boolean> invincibilityCache =
      CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.SECONDS).build();

  @Inject
  public GapplesManager(CustomGapples customGapples) {
    this.customGapples = customGapples;

    PluginManager pm = this.customGapples.getServer().getPluginManager();
    pm.registerEvents(this, this.customGapples);
  }

  public ImmutableSet<Gapple> getGapples() {
    return ImmutableSet.copyOf(gapples);
  }

  public void setGapples(Set<Gapple> gapples) {
    this.gapples = gapples;
  }

  public void reload() {
    this.setGapples(this.customGapples.getCustomGapplesConfig().getGapples());
  }

  @EventHandler
  public void onPlayerConsumeEvent(PlayerItemConsumeEvent event) {
    ItemStack item = event.getItem();
    Material material = item.getType();
    if (material != Material.GOLDEN_APPLE) return;

    ItemMeta meta = item.getItemMeta();
    if (!meta.hasDisplayName()) return;

    String name = meta.getDisplayName();
    Optional<Gapple> match = gapples.stream().filter(g -> g.getName().equals(name)).findFirst();
    if (!match.isPresent()) return;

    Player player = event.getPlayer();
    Gapple gapple = match.get();
    List<PotionEffect> effects = gapple.getEffects();
    player.addPotionEffects(effects);
    if (!gapple.hasLightning()) return;

    this.invincibilityCache.put(player.getUniqueId(), true);
    player.getWorld().strikeLightning(player.getLocation());
  }

  @EventHandler
  public void onPlayerDamage(EntityDamageByEntityEvent event) {
    Bukkit.broadcastMessage("Cancelling damage...");
    Entity entity = event.getEntity();
    if (!(entity instanceof Player)) return;

    Entity damager = event.getDamager();
    if (!(damager instanceof LightningStrike)) return;

    Player player = (Player) entity;
    if (!this.invincibilityCache.asMap().containsKey(player.getUniqueId())) return;

    event.setCancelled(true);
  }
}
