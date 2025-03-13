package org.exampleorg.example.pow4.Pow4.dd;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Dd extends JavaPlugin implements Listener {

    // Armazena as localizações de morte usando o UUID do jogador como chave
    private final HashMap<UUID, Location> deathLocations = new HashMap<>();

    @Override
    public void onEnable() {
        // Registrar eventos
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Plugin Dd habilitado!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Dd desabilitado!");
    }

    // Evento que captura a morte do jogador
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location deathLocation = player.getLocation();

        // Armazena a localização da morte
        deathLocations.put(player.getUniqueId(), deathLocation);
        player.sendMessage("Sua localização de morte foi registrada!");
    }

    // Comando para retornar ao local da morte
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("black")) {
            if (sender instanceof Player player) {
                UUID playerId = player.getUniqueId();

                // Verifica se há uma localização de morte armazenada
                if (deathLocations.containsKey(playerId)) {
                    Location deathLocation = deathLocations.get(playerId);

                    // Teletransporta o jogador para a localização de morte
                    player.teleport(deathLocation);
                    player.sendMessage("Você foi teletransportado para o local onde morreu.");
                } else {
                    player.sendMessage("Nenhuma localização de morte registrada.");
                }
            } else {
                sender.sendMessage("Apenas jogadores podem usar este comando.");
            }
            return true;
        }
        return false;
    }
}

