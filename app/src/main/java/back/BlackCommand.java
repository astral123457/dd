package back;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class BlackCommand implements CommandExecutor {

    // Método para obter o idioma com base no Player
    @SuppressWarnings("deprecation")
    private String getPlayerLanguage(Player player) {
        return mapLocaleToLanguage(player.getLocale());
    }

    // Método auxiliar para mapear locale para idioma
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
        // Comando "/black" para teleportar para a última morte
        if (label.equalsIgnoreCase("black")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                // Carregar mensagens
                MessageManager messageManager = new MessageManager();

                // Obter o idioma do jogador
                String language = getPlayerLanguage(player);

                // Obter a localização da última morte
                Location deathLocation = DeathListener.getDeathLocation(player);

                if (deathLocation != null) {
                    player.teleport(deathLocation);
                    player.sendMessage(Component.text("👀").append(Component.text(messageManager.getMessage("teleported", language), NamedTextColor.GOLD)));
                } else {
                    player.sendMessage(Component.text("👀").append(Component.text(messageManager.getMessage("no_death", language), NamedTextColor.GOLD)));
                }

                return true;
            }

            // Mensagem para remetente que não é jogador
            sender.sendMessage(Component.text("👀").append(Component.text("Este comando só pode ser usado por jogadores.", NamedTextColor.RED)));
            return false;
        }

        // Comando "/clearDeaths" para limpar os pontos de morte
        if (label.equalsIgnoreCase("clearDeaths")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                // Limpar todas as localizações de morte
                DeathListener.clearDeathLocations();
                player.sendMessage(Component.text("✅").append(Component.text("Todos os pontos de morte foram limpos!", NamedTextColor.GREEN)));
            } else {
                sender.sendMessage(Component.text("❌").append(Component.text("Este comando só pode ser usado por jogadores.", NamedTextColor.RED)));
            }

            return true;
        }

        return false; // Retorno padrão para comandos desconhecidos
    }
}