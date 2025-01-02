package su.xmine.socialLinks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class XMineSocialLinks extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        Objects.requireNonNull(this.getCommand("telegram")).setExecutor(new LinkCommandExecutor("telegram", this));
        Objects.requireNonNull(this.getCommand("discord")).setExecutor(new LinkCommandExecutor("discord", this));
        Objects.requireNonNull(this.getCommand("vk")).setExecutor(new LinkCommandExecutor("vk", this));
        Objects.requireNonNull(this.getCommand("rules")).setExecutor(new LinkCommandExecutor("rules", this));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("slreload")) {
            reloadConfig();
            sender.sendMessage("XMineSocialLinks successfully reloaded");
            getLogger().info("XMineSocialLinks successfully reloaded.");
            return true;
        }
        return false;
    }

    private class LinkCommandExecutor implements CommandExecutor {
        private final String platform;
        private final JavaPlugin plugin;

        public LinkCommandExecutor(String platform, JavaPlugin plugin) {
            this.platform = platform;
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
            if (sender.hasPermission("xmine.socialLinks." + platform)) {
                Message message = new Message(
                    plugin.getConfig().getString(platform + ".value", "https://www.xmine.su"),
                    plugin.getConfig().getBoolean(platform + ".isJson", false));
                message.send(sender);
            } else {
                sender.sendMessage("Permission denied");
            }
            return true;
        }
    }
}
