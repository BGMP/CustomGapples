package cl.bgmp.customgapples;

import cl.bgmp.customgapples.gapple.Gapple;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CustomGapplesConfig implements Config {
  private FileConfiguration config;
  private File dataFolder;

  private Set<Gapple> gapples = new HashSet<>();

  public CustomGapplesConfig(FileConfiguration config, File dataFolder) {
    this.config = config;
    this.dataFolder = dataFolder;

    ConfigurationSection applesSection = this.config.getConfigurationSection("gapples");
    for (String id : applesSection.getKeys(false)) {
      String name = applesSection.getString(id + ".name");
      List<String> lore = applesSection.getStringList(id + ".lore");
      List<PotionEffect> effects = new ArrayList<>();
      boolean enchanted = applesSection.getBoolean(id + ".enchanted");
      boolean lightning = applesSection.getBoolean(id + ".lightning");

      for (String effectString : applesSection.getStringList(id + ".effects")) {
        String[] split = effectString.split(":");

        PotionEffectType effectType = PotionEffectType.getByName(split[0].toUpperCase());
        if (effectType == null) {
          Bukkit.getLogger()
              .severe(
                  "Invalid potion effect type detected for custom apple '"
                      + name
                      + "': "
                      + split[0]);
          continue;
        }

        int amplifier = Integer.parseInt(split[1]);
        int duration = Integer.parseInt(split[2]);

        effects.add(new PotionEffect(effectType, duration * 20, amplifier));
      }

      this.gapples.add(new Gapple(id, name, lore, effects, enchanted, lightning));
    }
  }

  @Override
  public Set<Gapple> getGapples() {
    return gapples;
  }
}
