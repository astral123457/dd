package back;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

public class DeathListener implements Listener {

    // HashMap para armazenar as localizações de morte dos jogadores
    private static final HashMap<Player, Location> deathLocations = new HashMap<>();

    // Método para obter a localização da última morte de um jogador
    public static Location getDeathLocation(Player player) {
        return deathLocations.get(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location deathLocation = player.getLocation();

        // Salva a localização da morte no HashMap
        deathLocations.put(player, deathLocation);
    }

    // Método para limpar todas as localizações de morte
    public static void clearDeathLocations() {
        deathLocations.clear();
        System.out.println("Todos os pontos de morte foram limpos.");
    }
}

