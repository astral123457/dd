package back;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import java.util.UUID;

public class HeadInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player == null) return; // Verifica se o jogador não é nulo

        UUID playerId = player.getUniqueId(); // Obtendo UUID do jogador

        // Obtém o inventário salvo do jogador
        ItemStack[] savedInventory = DeathListener.getPlayerInventory(playerId);
        if (savedInventory != null) {
            player.getInventory().setContents(savedInventory);
            DeathListener.clearPlayerInventory(playerId); // Limpa após restauração
            player.sendMessage("✅ Seu inventário foi restaurado!");
        } 
    }
}