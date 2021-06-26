package cl.bgmp.customgapples;

import cl.bgmp.bukkit.util.BukkitCommandsManager;
import cl.bgmp.bukkit.util.CommandsManagerRegistration;
import cl.bgmp.customgapples.command.GappleCommand;
import cl.bgmp.customgapples.gapple.GapplesManager;
import cl.bgmp.customgapples.injection.CustomGapplesModule;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandNumberFormatException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandPermissionsException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandUsageException;
import cl.bgmp.minecraft.util.commands.exceptions.MissingNestedCommandException;
import cl.bgmp.minecraft.util.commands.exceptions.ScopeMismatchException;
import cl.bgmp.minecraft.util.commands.exceptions.WrappedCommandException;
import cl.bgmp.minecraft.util.commands.injection.SimpleInjector;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomGapples extends JavaPlugin {
  private BukkitCommandsManager commandsManager = new BukkitCommandsManager();
  private CommandsManagerRegistration commandRegistry;
  private CustomGapplesConfig config;

  @Inject GapplesManager gapplesManager;

  public GapplesManager getGapplesManager() {
    return this.gapplesManager;
  }

  public CustomGapplesConfig getCustomGapplesConfig() {
    return this.config;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    try {
      this.commandsManager.execute(command.getName(), args, sender, sender);
    } catch (ScopeMismatchException exception) {
      final String[] scopes = exception.getScopes();
      if (!Arrays.asList(scopes).contains("player")) {
        sender.sendMessage(ChatColor.RED + "You must use this command via the server console!");
      } else {
        sender.sendMessage(ChatColor.RED + "You must be a player to execute this command!");
      }
    } catch (CommandPermissionsException exception) {
      sender.sendMessage(ChatColor.RED + "You do not have permission");
    } catch (MissingNestedCommandException exception) {
      sender.sendMessage(ChatColor.YELLOW + "⚠ " + ChatColor.RED + exception.getUsage());
    } catch (CommandUsageException exception) {
      sender.sendMessage(ChatColor.RED + exception.getMessage());
      sender.sendMessage(ChatColor.RED + exception.getUsage());
    } catch (WrappedCommandException exception) {
      if (exception.getCause() instanceof CommandNumberFormatException) {
        sender.sendMessage(ChatColor.RED + "Expected a number, received a string instead");
      } else {
        sender.sendMessage(ChatColor.RED + "An unknown exception has occurred");
        exception.printStackTrace();
      }
    } catch (CommandException exception) {
      sender.sendMessage(ChatColor.RED + exception.getMessage());
    }
    return true;
  }

  @Override
  public void onEnable() {
    this.loadConfig();

    CustomGapplesModule module = new CustomGapplesModule(this, this.config);
    Injector injector = module.createInjector();

    injector.injectMembers(this);
    injector.injectMembers(this.config);

    this.gapplesManager.setGapples(this.config.getGapples());

    this.commandRegistry =
        new CommandsManagerRegistration(
            this, this, new GappleCommand.GappleCommandTabCompleter(this), this.commandsManager);
    this.commandsManager.setInjector(new SimpleInjector(this));
    this.commandRegistry.register(GappleCommand.AppleParentCommand.class);
  }

  @Override
  public void onDisable() {}

  @Override
  public void reloadConfig() {
    super.reloadConfig();

    try {
      this.config = new CustomGapplesConfig(this.getConfig(), this.getDataFolder());
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
  }

  private void loadConfig() {
    this.saveDefaultConfig();
    this.reloadConfig();
    if (this.config != null) return;

    this.getServer().getPluginManager().disablePlugin(this);
  }
}
