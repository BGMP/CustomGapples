package cl.bgmp.customgapples.command;

import cl.bgmp.customgapples.CustomGapples;
import cl.bgmp.customgapples.Permission;
import cl.bgmp.customgapples.gapple.Gapple;
import cl.bgmp.customgapples.util.Potions;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import cl.bgmp.minecraft.util.commands.annotations.NestedCommand;
import cl.bgmp.minecraft.util.commands.annotations.TabCompletion;
import cl.bgmp.minecraft.util.commands.exceptions.CommandNumberFormatException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class GappleCommand {
  private CustomGapples customGapples;

  public GappleCommand(CustomGapples customGapples) {
    this.customGapples = customGapples;
  }

  public static class GappleParentCommand {
    private static final String COMMANDS_MENU_TITLE = " CustomGapples Commands ";
    private static final String COMMANDS_HELP_MENU =
        ChatColor.BLUE.toString()
            + ChatColor.STRIKETHROUGH
            + "----------"
            + ChatColor.WHITE
            + COMMANDS_MENU_TITLE
            + ChatColor.BLUE
            + ChatColor.STRIKETHROUGH
            + "----------"
            + ChatColor.BLUE
            + ChatColor.STRIKETHROUGH
            + "\n"
            + ChatColor.GOLD
            + " » "
            + ChatColor.GREEN
            + "/gapple get <id> <amount>"
            + "\n"
            + ChatColor.GOLD
            + " » "
            + ChatColor.GREEN
            + "/gapple give <nick> <id> <amount>"
            + "\n"
            + ChatColor.GOLD
            + " » "
            + ChatColor.GREEN
            + "/gapple list"
            + "\n"
            + ChatColor.GOLD
            + " » "
            + ChatColor.GREEN
            + "/gapple reload";

    @Command(
        aliases = {"gapple"},
        desc = "CustomGapples' node command.")
    @NestedCommand(GappleCommand.class)
    public static void gapple(final CommandContext args, final CommandSender sender) {
      sender.sendMessage(COMMANDS_HELP_MENU);
    }
  }

  @Command(
      aliases = {"get"},
      desc = "Gives you custom golden apples.",
      usage = "<id> <amount>",
      min = 1,
      max = 2)
  @CommandPermissions(Permission.GAPPLE_GET_COMMAND)
  @CommandScopes({"player"})
  public void get(final CommandContext args, final CommandSender sender)
      throws CommandNumberFormatException {
    String id = args.getString(0);
    Optional<Gapple> match =
        this.customGapples.getGapplesManager().getGapples().stream()
            .filter(g -> g.getId().equals(id))
            .findFirst();
    if (!match.isPresent()) {
      sender.sendMessage(
          ChatColor.YELLOW + "⚠ " + ChatColor.RED + " No custom gapple matched query.");
      return;
    }

    Player player = (Player) sender;
    Gapple gapple = match.get();
    int amount = args.argsLength() == 1 ? 1 : args.getInteger(1);

    HashMap<Integer, ItemStack> leftover =
        player.getInventory().addItem(gapple.getItemStack(amount));
    if (leftover.isEmpty()) return;

    player.getWorld().dropItemNaturally(player.getLocation(), gapple.getItemStack(amount));
  }

  @Command(
      aliases = {"give"},
      desc = "Gives custom golden apples.",
      usage = "<nick> <id> <amount>",
      min = 2,
      max = 3)
  @CommandPermissions(Permission.GAPPLE_GIVE_COMMAND)
  @CommandScopes({"player", "console"})
  public void give(final CommandContext args, final CommandSender sender)
      throws CommandNumberFormatException {
    String nick = args.getString(0);
    Optional<? extends Player> target =
        Bukkit.getOnlinePlayers().stream().filter(p -> p.getName().equals(nick)).findFirst();
    if (!target.isPresent()) {
      sender.sendMessage(ChatColor.YELLOW + "⚠ " + ChatColor.RED + " No players matched query.");
      return;
    }

    String id = args.getString(1);
    Optional<Gapple> match =
        this.customGapples.getGapplesManager().getGapples().stream()
            .filter(g -> g.getId().equals(id))
            .findFirst();
    if (!match.isPresent()) {
      sender.sendMessage(
          ChatColor.YELLOW + "⚠ " + ChatColor.RED + " No custom gapple matched query.");
      return;
    }

    Player player = target.get();
    Gapple gapple = match.get();
    int amount = args.argsLength() == 2 ? 1 : args.getInteger(2);

    HashMap<Integer, ItemStack> leftover =
        player.getInventory().addItem(gapple.getItemStack(amount));
    if (leftover.isEmpty()) return;

    player.getWorld().dropItemNaturally(player.getLocation(), gapple.getItemStack(amount));
  }

  @Command(
      aliases = {"list"},
      desc = "Lists all loaded custom gapples.",
      max = 0)
  @CommandPermissions(Permission.GAPPLE_LIST_COMMAND)
  @CommandScopes({"player", "console"})
  public void list(final CommandContext args, final CommandSender sender) {
    String title =
        ChatColor.BLUE.toString()
            + ChatColor.STRIKETHROUGH
            + "----------"
            + ChatColor.WHITE
            + " Custom Gapples "
            + ChatColor.BLUE
            + ChatColor.STRIKETHROUGH
            + "----------"
            + ChatColor.BLUE
            + ChatColor.STRIKETHROUGH
            + "\n";

    StringBuilder builder = new StringBuilder(title);
    int i = 1;
    for (Gapple gapple : this.customGapples.getGapplesManager().getGapples()) {
      builder
          .append(ChatColor.WHITE.toString())
          .append(i)
          .append(". ")
          .append(gapple.getName())
          .append(" ")
          .append(ChatColor.WHITE)
          .append("(")
          .append(ChatColor.GOLD)
          .append(gapple.getId())
          .append(ChatColor.WHITE)
          .append(")")
          .append("\n");

      for (PotionEffect effect : gapple.getEffects()) {
        builder
            .append(ChatColor.WHITE)
            .append(" ﹄ ")
            .append(ChatColor.WHITE)
            .append(Potions.potionEffectTypeName(effect.getType()))
            .append(" ")
            .append(ChatColor.GREEN)
            .append(effect.getAmplifier())
            .append(ChatColor.WHITE)
            .append(", ")
            .append(ChatColor.GREEN)
            .append(effect.getDuration() / 20)
            .append("s")
            .append("\n");
      }

      i++;
    }

    sender.sendMessage(builder.toString());
  }

  @Command(
      aliases = {"reload"},
      desc = "Reloads Custom Apples' configuration.",
      max = 0)
  @CommandPermissions(Permission.GAPPLE_RELOAD_COMMAND)
  @CommandScopes({"player"})
  public void reload(final CommandContext args, final CommandSender sender) {
    this.customGapples.reloadConfig();
    this.customGapples.getGapplesManager().reload();
    sender.sendMessage(ChatColor.GREEN + "Configuration successfully reloaded.");
  }

  @TabCompletion
  public static class GappleCommandTabCompleter implements TabCompleter {
    private CustomGapples customGapples;

    public GappleCommandTabCompleter(CustomGapples customGapples) {
      this.customGapples = customGapples;
    }

    @Override
    public List<String> onTabComplete(
        CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
      List<String> completion = new ArrayList<>();

      switch (args.length) {
        case 1:
          List<String> arguments = Arrays.asList("get", "give", "list", "reload");
          for (String arg : arguments) {
            if (arg.startsWith(args[0].toLowerCase())) {
              completion.add(arg);
            }
          }
          break;
        case 2:
          if (args[0].toLowerCase().equals("get")) {
            List<String> gappleIds =
                this.customGapples.getGapplesManager().getGapples().stream()
                    .map(Gapple::getId)
                    .collect(Collectors.toList());
            for (String id : gappleIds) {
              if (id.startsWith(args[1])) completion.add(id);
            }
          } else if (args[0].toLowerCase().equals("give")) {
            List<String> online =
                Bukkit.getOnlinePlayers().stream()
                    .map(HumanEntity::getName)
                    .collect(Collectors.toList());
            for (String p : online) {
              if (p.startsWith(args[1])) completion.add(p);
            }
          }
          break;
        case 3:
          if (args[0].toLowerCase().equals("give")) {
            List<String> gappleIds =
                this.customGapples.getGapplesManager().getGapples().stream()
                    .map(Gapple::getId)
                    .collect(Collectors.toList());
            for (String id : gappleIds) {
              if (id.startsWith(args[2])) completion.add(id);
            }
          }
          break;
      }

      return completion;
    }
  }
}
