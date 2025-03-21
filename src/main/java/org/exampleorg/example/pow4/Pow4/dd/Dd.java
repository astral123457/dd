package org.exampleorg.example.pow4.Pow4.dd;

import com.google.gson.GsonBuilder;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class Dd extends JavaPlugin implements Listener {

    // Armazena as localizações de morte usando o UUID do jogador como chave
    private final HashMap<UUID, Location> deathLocations = new HashMap<>();


    private static final String FOLDER_PATH = "plugins/Dd";
    private static final String CONFIG_FILE = FOLDER_PATH + "/config.json";
    private static final String MESSAGES_FILE_PATH = FOLDER_PATH + "/messages.json";

    @Override
    public void onEnable() {
        // Registrar eventos
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Plugin Dd habilitado!");

        createFolderAndConfig();
        boolean isEnabled = loadPluginStatus();

        if (!isEnabled) {
            getLogger().warning("Plugin desativado via configuração.");
            getServer().getPluginManager().disablePlugin(this);
            return; // Finaliza a inicialização se o plugin estiver desativado
        }

        // Carregar idioma
        String language = loadLanguage();
        getLogger().info("Idioma configurado: " + language);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Dd desabilitado!");
    }

    // Evento que captura a morte do jogador
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Carregar mensagens
        MessageManager messageManager = new MessageManager();

        Player player = event.getEntity();
        Location deathLocation = player.getLocation();

        // Armazena a localização da morte
        deathLocations.put(player.getUniqueId(), deathLocation);

        // Envia mensagem de morte traduzida
        String message = messageManager.getMessage("death_registered", "br");
        player.sendMessage(message);
    }

    // Comando para retornar ao local da morte
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Carregar mensagens
        MessageManager messageManager = new MessageManager();



        if (label.equalsIgnoreCase("black") || label.equalsIgnoreCase("back") ) {
            if (sender instanceof Player player) {
                UUID playerId = player.getUniqueId();

                String language = player.getLocale().toLowerCase();
                if (language.startsWith("pt")) {
                    language = "br";
                } else if (language.startsWith("en")) {
                    language = "en";
                } else if (language.startsWith("es")) {
                    language = "es";
                } else if (language.startsWith("fr")) {
                    language = "fr";
                } else if (language.startsWith("de")) {
                    language = "de";
                } else {
                    language = "default"; // Idioma padrão caso não seja reconhecido
                }

                // Verifica se há uma localização de morte armazenada
                if (deathLocations.containsKey(playerId)) {
                    Location deathLocation = deathLocations.get(playerId);

                    // Teletransporta o jogador para a localização de morte
                    player.teleport(deathLocation);

                    // Mensagens traduzidas ao ser teletransportado
                    String teleportMessage = messageManager.getMessage("teleported", language);
                    player.sendMessage(teleportMessage);

                    String warningMessage = messageManager.getMessage("warning", language);
                    player.sendMessage(warningMessage);
                } else {
                    String noDeathLocation = messageManager.getMessage("no_death", language);
                    player.sendMessage(noDeathLocation);
                }
            } else {
                String onlyPlayers = messageManager.getMessage("only_players", "en");
                sender.sendMessage(onlyPlayers);
            }
            return true;
        }
        return false;
    }
    private void createFolderAndConfig() {

        // Criar a pasta blockdata, se não existir
        File folder = new File(FOLDER_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Criar o arquivo config.json com configuração padrão
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            JsonObject defaultConfig = new JsonObject();
            defaultConfig.addProperty("enabled", true);
            defaultConfig.addProperty("language", "br"); // Adiciona o idioma padrão

            try (FileWriter writer = new FileWriter(configFile)) {
                // Gson com Pretty Printing
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(defaultConfig, writer);
                getLogger().info("Arquivo config.json criado com configuração padrão.");
            } catch (IOException e) {
                getLogger().severe("Erro ao criar o arquivo config.json: " + e.getMessage());
            }
        }

        // Criar o arquivo messages.json com mensagens padrão, apenas se não existir
        File messagesFile = new File(MESSAGES_FILE_PATH);
        if (!messagesFile.exists()) {
            JsonObject messages = new JsonObject();


// Mensagem de morte registrada
            JsonObject deathRegisteredMessages = new JsonObject();
            deathRegisteredMessages.addProperty("br", "Sua localização de morte foi registrada!");
            deathRegisteredMessages.addProperty("en", "Your death location has been recorded!");
            messages.add("death_registered", deathRegisteredMessages);

// Mensagem de teletransporte
            JsonObject teleportedMessages = new JsonObject();
            teleportedMessages.addProperty("br", "§4 Você foi teletransportado para o local onde sofreu §f⛏§4 até a morte.");
            teleportedMessages.addProperty("en", "§4 You were teleported to the location where you died §f⛏§4.");
            messages.add("teleported", teleportedMessages);

// Mensagem de aviso
            JsonObject warningMessages = new JsonObject();
            warningMessages.addProperty("br", "§b Cuidado, o assassino sempre volta ao local do crime: o Mob §aZumbi dos Palmares§8. §f☠");
            warningMessages.addProperty("en", "§b Be careful, the killer always returns to the crime scene: the Mob §aZombie of the Palmares§8. §f☠");
            messages.add("warning", warningMessages);

// Nenhuma localização de morte
            JsonObject noDeathMessages = new JsonObject();
            noDeathMessages.addProperty("br", "Nenhuma localização de morte registrada.");
            noDeathMessages.addProperty("en", "No death location recorded.");
            messages.add("no_death", noDeathMessages);

// Apenas jogadores
            JsonObject onlyPlayersMessages = new JsonObject();
            onlyPlayersMessages.addProperty("br", "Apenas jogadores podem usar este comando.");
            onlyPlayersMessages.addProperty("en", "Only players can use this command.");
            messages.add("only_players", onlyPlayersMessages);

            try (FileWriter writer = new FileWriter(messagesFile)) {

                // Gson com Pretty Printing
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(messages, writer);
                getLogger().info("Arquivo messages.json criado com mensagens padrão.");
            } catch (IOException e) {
                getLogger().severe("Erro ao criar o arquivo messages.json: " + e.getMessage());
            }
        }
    }


    private boolean loadPluginStatus() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(CONFIG_FILE)));
            JsonObject config = new Gson().fromJson(content, JsonObject.class);
            return config.get("enabled").getAsBoolean();
        } catch (IOException e) {
            getLogger().severe("Erro ao ler o arquivo config.json: " + e.getMessage());
        }
        return false; // Desabilita o plugin em caso de erro
    }

    public String loadLanguage() {
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(CONFIG_FILE)));
                JsonObject config = new Gson().fromJson(content, JsonObject.class);
                return config.get("language").getAsString(); // Retorna o idioma configurado
            } catch (IOException e) {
                getLogger().severe("Erro ao ler o arquivo config.json: " + e.getMessage());
            }
        }
        return "br"; // Padrão para português, caso ocorra um erro
    }
}


