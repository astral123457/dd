package back;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Bukkit;
import java.util.HashMap;
import java.util.UUID;
import net.kyori.adventure.text.Component;

public class DeathListener implements Listener {

    // HashMaps para armazenar informações da morte e inventário dos jogadores
    private static final HashMap<UUID, Location> deathLocations = new HashMap<>();
    private static final HashMap<UUID, ItemStack[]> playerInventories = new HashMap<>();

    // Evento chamado quando um jogador morre
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player == null) return;

        Location deathLocation = player.getLocation();

        // Armazena a localização da morte do jogador
        deathLocations.put(player.getUniqueId(), deathLocation);

        // Salva o inventário do jogador antes de morrer
        if (player.getInventory().getContents() != null) {
            playerInventories.put(player.getUniqueId(), player.getInventory().getContents());
        }

        // Criar uma "cabeça" personalizada do jogador
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = head.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text("Cabeça de " + player.getName()));
            head.setItemMeta(meta);
        }

        // Dropar a cabeça no local da morte
        player.getWorld().dropItemNaturally(deathLocation, head);

        // Limpa o inventário do jogador ao morrer
        player.getInventory().clear();
    }

    // Método para obter a localização da última morte do jogador
    public static Location getDeathLocation(UUID playerId) {
        return deathLocations.get(playerId);
    }

    // Método para restaurar o inventário do jogador após a morte
    public static void restoreInventory(Player player) {
        if (player == null || !player.isOnline()) return;

        ItemStack[] inventory = playerInventories.get(player.getUniqueId());
        if (inventory != null) {
            player.getInventory().setContents(inventory);
            playerInventories.remove(player.getUniqueId()); // Remove após restauração
        }
    }

    // Método para limpar todas as localizações de morte
    public static void clearDeathLocations() {
        deathLocations.clear();
        Bukkit.getLogger().info("Todos os pontos de morte foram limpos.");
    }

    // Método para limpar todos os inventários salvos
    public static void clearPlayerInventories() {
        playerInventories.clear();
        Bukkit.getLogger().info("Todos os inventários dos jogadores foram limpos.");
    }

    // Método para obter o inventário salvo de um jogador
    public static ItemStack[] getPlayerInventory(UUID playerId) {
        return playerInventories.get(playerId);
    }

    // Método para limpar o inventário de um jogador específico
    public static void clearPlayerInventory(UUID playerId) {
        playerInventories.remove(playerId);
    }
}