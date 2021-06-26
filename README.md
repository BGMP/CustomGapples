CustomGapples
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
  
## Instalation
As in any other Maven project, simply clone this repository and run the corresponding packaging command:

  > mvn clean package

The compiled binary can be found in your `target/` directory.
