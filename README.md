CustomGapples [![build](https://github.com/BGMP/CustomGapples/actions/workflows/build.yml/badge.svg)](https://github.com/BGMP/CustomGapples/actions/workflows/build.yml) [![deploy](https://github.com/BGMP/CustomGapples/actions/workflows/deploy.yml/badge.svg)](https://github.com/BGMP/CustomGapples/actions/workflows/deploy.yml) [![licence](https://gitlicense.com/badge/BGMP/CustomGapples)](https://github.com/BGMP/CustomGapples/blob/master/LICENCE.md)
===
Spigot 1.8 plugin for creating custom golden apples.

## Commands
CustomGapples' commands & usage:
  - `/gapple` Node plugin command. It also displays a help menu.
  - `/gapple get <id> <amount>` Gets you a custom gapple.
  - `/gapple give <nick> <id> <amount>` Gives another player a custom gapple.
  - `/gapple reload` Reloads the plugin's configuration, re-loading all the custom gapples specified in config.
  
## Permissions
CustomGapples' permissions:
  - `customgapples.command.gapple.get` Allows a player to use /gapple get.
  - `customgapples.command.gapple.give` Allows a player to use /gapple give.
  - `customgapples.command.gapple.list` Allows a player to list all the available custom gapples.
  - `customgapples.command.reload` Reloads all custom gapples from configuration.

## Configuration
The plugin generates a [template configuration file](https://github.com/BGMP/CustomGapples/blob/master/src/main/resources/config.yml)
within its data folder. For the potion effects, you must use Bukkit's `PotionEffectType`. Here is a reference table:

| Potion Name     | PotionEffectType  |
---               |                 ---
| Absorption      | ABSORPTION        |
| Blindness       | BLINDNESS         |
| Fire Resistance | FIRE_RESISTANCE   |
| Haste           | FAST_DIGGING      |
| Health Boost    | HEALTH_BOOST      |
| Hunger          | HUNGER            |
| Instant Damage  | HARM              |
| Invisibility    | INVISIBILITY      |
| Instant Health  | HEAL              |
| Jump Boost      | JUMP              |
| Mining Fatigue  | SLOW_DIGGING      |
| Nausea          | CONFUSION         |
| Night Vision    | NIGHT_VISION      |
| Poison          | POISON            |
| Regeneration    | REGENERATION      |
| Resistance      | DAMAGE_RESISTANCE |
| Saturation      | SATURATION        |
| Slowness        | SLOW              |
| Speed           | SPEED             |
| Strength        | INCREASE_DAMAGE   |
| Water Breathing | WATER_BREATHING   |
| Weakness        | WEAKNESS          |
| Wither          | WITHER            |
 
## Installation
As in any other Maven project, simply clone this repository and run the corresponding packaging command:

  > mvn clean package

The compiled binary can be found in your `target/` directory.

## Download
Compiled binaries are deployed to my Maven repository: [https://maven.bgmp.cl/cl/bgmp/CustomGapples/](https://maven.bgmp.cl/cl/bgmp/CustomGapples/).
