package back;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class BlackCommand implements CommandExecutor {

    @SuppressWarnings("deprecation")
    private String getPlayerLanguage(Player player) {
        return mapLocaleToLanguage(player.getLocale());
    }

    private String mapLocaleToLanguage(String locale) {
        return switch (locale.toLowerCase().substring(0, 2)) {
            case "pt" -> "br";
            case "en" -> "en";
            case "es" -> "es";
            case "fr" -> "fr";
            case "de" -> "de";
            default -> "default";
        };
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("black")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                MessageManager messageManager = new MessageManager();
                String language = getPlayerLanguage(player);

                // Obtém a localização da última morte usando UUID
                Location deathLocation = DeathListener.getDeathLocation(player.getUniqueId());

                if (deathLocation != null) {
                    player.teleport(deathLocation);
                    player.sendMessage(Component.text("👀 ").append(Component.text(messageManager.getMessage("teleported", language), NamedTextColor.GOLD)));
                    player.sendMessage(Component.text("👀 ").append(Component.text(messageManager.getMessage("warnin", language), NamedTextColor.GOLD)));
                } else {
                    player.sendMessage(Component.text("👀 ").append(Component.text(messageManager.getMessage("no_death", language), NamedTextColor.GOLD)));
                }

                return true;
            }

            sender.sendMessage(Component.text("👀 ").append(Component.text("Este comando só pode ser usado por jogadores.", NamedTextColor.RED)));
            return false;
        }

        if (command.getName().equalsIgnoreCase("clearDeaths")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                DeathListener.clearDeathLocations();
                player.sendMessage(Component.text("✅ ").append(Component.text("Todos os pontos de morte foram limpos!", NamedTextColor.GREEN)));
            } else {
                sender.sendMessage(Component.text("❌ ").append(Component.text("Este comando só pode ser usado por jogadores.", NamedTextColor.RED)));
            }

            return true;
        }

        return false;
    }
}