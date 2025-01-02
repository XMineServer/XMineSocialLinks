package su.xmine.socialLinks;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
    private final Logger logger = Logger.getLogger(Message.class.getSimpleName());
    @NotNull private final String value;
    private final boolean isJson;

    public Message(@NotNull String value, boolean isJson) {
        this.value = value;
        this.isJson = isJson;
    }

    public void send(CommandSender sender) {
        if (value.isEmpty())
            return;
        BaseComponent[] components;
        if (isJson) {
            try {
                components = ComponentSerializer.parse(value);
            } catch (Exception e) {
                logger.log(Level.WARNING, "JSON text parse error: " + value, e);
                components = translateHexColorCodes(value);
            }
        } else {
            components = translateHexColorCodes(value);
        }
        sender.spigot().sendMessage(components);
    }

    public static BaseComponent[] translateHexColorCodes(String message) {
        Matcher matcher = Pattern.compile("#([A-Fa-f0-9]{6})").matcher(message);
        List<BaseComponent> componentList = new ArrayList<>();
        int old = 0;
        ChatColor color = ChatColor.WHITE;
        while (matcher.find()) {
            TextComponent component = new TextComponent(message.substring(old, matcher.start()));
            old = matcher.end();
            component.setColor(color);
            color = ChatColor.of(matcher.group());
            componentList.add(component);
        }
        TextComponent component = new TextComponent(message.substring(old));
        component.setColor(color);
        componentList.add(component);
        return componentList.toArray(BaseComponent[]::new);
    }
}
