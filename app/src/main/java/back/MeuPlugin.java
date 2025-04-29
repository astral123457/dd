package back;


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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.HashMap;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import back.DeathListener;

public class MeuPlugin extends JavaPlugin {

    private static final String FOLDER_PATH = "plugins/black";
    private static final String CONFIG_FILE = FOLDER_PATH + "/config.json";
    private static final String MESSAGES_FILE_PATH = FOLDER_PATH + "/messages.json";

    @Override
    public void onEnable() {

        createFolderAndConfig();
        // Registrar o listener de eventos
        getServer().getPluginManager().registerEvents(new DeathListener(), this);

        // Registrar o comando
        getCommand("black").setExecutor(new BlackCommand());

        getLogger().info("Plugin ativado!");

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
        getLogger().info("Plugin desativado!");
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
        deathRegisteredMessages.addProperty("es", "¡Se ha registrado tu ubicación de muerte!");
        deathRegisteredMessages.addProperty("fr", "Votre emplacement de décès a été enregistré !");
        deathRegisteredMessages.addProperty("de", "Ihr Sterbeort wurde registriert!");
        deathRegisteredMessages.addProperty("ru", "Ваше место смерти было зарегистрировано!");
        deathRegisteredMessages.addProperty("zh", "您的死亡位置已被记录！");
        deathRegisteredMessages.addProperty("zh-tw", "您的死亡位置已被記錄！");
        deathRegisteredMessages.addProperty("ja", "死亡場所が記録されました！");
        deathRegisteredMessages.addProperty("ko", "사망 위치가 기록되었습니다!");
        deathRegisteredMessages.addProperty("it", "La tua posizione di morte è stata registrata!");
        deathRegisteredMessages.addProperty("nl", "Je sterfplek is geregistreerd!");
        deathRegisteredMessages.addProperty("pl", "Twoja lokalizacja śmierci została zarejestrowana!");
        deathRegisteredMessages.addProperty("sv", "Din dödsplats har registrerats!");
        deathRegisteredMessages.addProperty("cs", "Vaše místo smrti bylo zaznamenáno!");
        deathRegisteredMessages.addProperty("hu", "A halálhelyed rögzítve lett!");
        deathRegisteredMessages.addProperty("tr", "Ölüm yeriniz kaydedildi!");
        deathRegisteredMessages.addProperty("ar", "تم تسجيل موقع وفاتك!");
        deathRegisteredMessages.addProperty("fi", "Kuolinpaikkasi on tallennettu!");
        deathRegisteredMessages.addProperty("da", "Din dødsplacering er blevet registreret!");
        messages.add("death_registered", deathRegisteredMessages);


        // Mensagem de teletransporte
        JsonObject teleportedMessages = new JsonObject();
        teleportedMessages.addProperty("br", "§4 Você foi teletransportado para o local onde sofreu §f⛏§4 até a morte.");
        teleportedMessages.addProperty("en", "§4 You were teleported to the location where you died §f⛏§4.");
        teleportedMessages.addProperty("es", "§4 Fuiste teletransportado al lugar donde moriste §f⛏§4.");
        teleportedMessages.addProperty("fr", "§4 Vous avez été téléporté à l'endroit où vous êtes mort §f⛏§4.");
        teleportedMessages.addProperty("de", "§4 Sie wurden an den Ort teleportiert, an dem Sie gestorben sind §f⛏§4.");
        teleportedMessages.addProperty("ru", "§4 Вы были телепортированы на место вашей смерти §f⛏§4.");
        teleportedMessages.addProperty("zh", "§4 您被传送到您死亡的位置 §f⛏§4。");
        teleportedMessages.addProperty("zh-tw", "§4 您被傳送到您死亡的位置 §f⛏§4。");
        teleportedMessages.addProperty("ja", "§4 死亡した場所にテレポートされました §f⛏§4。");
        teleportedMessages.addProperty("ko", "§4 사망한 위치로 텔레포트되었습니다 §f⛏§4.");
        teleportedMessages.addProperty("it", "§4 Sei stato teletrasportato nel luogo in cui sei morto §f⛏§4.");
        teleportedMessages.addProperty("nl", "§4 Je bent geteleporteerd naar de locatie waar je stierf §f⛏§4.");
        teleportedMessages.addProperty("pl", "§4 Zostałeś przeteleportowany na miejsce swojej śmierci §f⛏§4.");
        teleportedMessages.addProperty("sv", "§4 Du har teleporterats till platsen där du dog §f⛏§4.");
        teleportedMessages.addProperty("cs", "§4 Byli jste teleportováni na místo, kde jste zemřeli §f⛏§4.");
        teleportedMessages.addProperty("hu", "§4 Teleportáltak arra a helyre, ahol meghaltál §f⛏§4.");
        teleportedMessages.addProperty("tr", "§4 Öldüğünüz yere ışınlandınız §f⛏§4.");
        teleportedMessages.addProperty("ar", "§4 تم نقلك إلى المكان الذي مت فيه §f⛏§4.");
        teleportedMessages.addProperty("fi", "§4 Sinut siirrettiin sijaintiin, jossa kuolit §f⛏§4.");
        teleportedMessages.addProperty("da", "§4 Du blev teleporteret til stedet, hvor du døde §f⛏§4.");
        messages.add("teleported", teleportedMessages);


        // Mensagem de aviso
        JsonObject warningMessages = new JsonObject();
        warningMessages.addProperty("br", "§b Cuidado, o assassino sempre volta ao local do crime: o Mob §aZumbi dos Palmares§8. §f☠");
        warningMessages.addProperty("en", "§b Be careful, the killer always returns to the crime scene: the Mob §aZombie of the Palmares§8. §f☠");
        warningMessages.addProperty("es", "§b Ten cuidado, el asesino siempre regresa al lugar del crimen: el Mob §aZombie de los Palmares§8. §f☠");
        warningMessages.addProperty("fr", "§b Attention, le tueur revient toujours sur les lieux du crime : le Mob §aZombie des Palmares§8. §f☠");
        warningMessages.addProperty("de", "§b Vorsicht, der Mörder kehrt immer zum Tatort zurück: das Mob §aZombie der Palmares§8. §f☠");
        warningMessages.addProperty("ru", "§b Осторожно, убийца всегда возвращается на место преступления: Моб §aЗомби из Пальмареса§8. §f☠");
        warningMessages.addProperty("zh", "§b 小心，凶手总是回到犯罪现场：怪物§a棕榈地的僵尸§8。 §f☠");
        warningMessages.addProperty("zh-tw", "§b 小心，凶手總是回到犯罪現場：怪物§a棕櫚地的殭屍§8。 §f☠");
        warningMessages.addProperty("ja", "§b 注意してください、殺人犯はいつも犯罪現場に戻ってきます：モブ §aパルマレスのゾンビ§8. §f☠");
        warningMessages.addProperty("ko", "§b 조심하세요, 살인자는 항상 범죄 현장으로 돌아옵니다: 몹 §a팔마레스의 좀비§8. §f☠");
        warningMessages.addProperty("it", "§b Attento, l'assassino torna sempre sulla scena del crimine: il Mob §aZombie dei Palmares§8. §f☠");
        warningMessages.addProperty("nl", "§b Wees voorzichtig, de moordenaar keert altijd terug naar de plaats delict: de Mob §aZombie van de Palmares§8. §f☠");
        warningMessages.addProperty("pl", "§b Uważaj, zabójca zawsze wraca na miejsce zbrodni: Mob §aZombie z Palmares§8. §f☠");
        warningMessages.addProperty("sv", "§b Var försiktig, mördaren återvänder alltid till brottsplatsen: Mobben §aZombie från Palmares§8. §f☠");
        warningMessages.addProperty("cs", "§b Dávejte pozor, vrah se vždy vrací na místo činu: Mob §aZombie z Palmares§8. §f☠");
        warningMessages.addProperty("hu", "§b Vigyázz, a gyilkos mindig visszatér a tetthelyre: a Mob §aPalmares Zombija§8. §f☠");
        warningMessages.addProperty("tr", "§b Dikkat edin, katil her zaman suç mahalline geri döner: Mob §aPalmares'in Zombisi§8. §f☠");
        warningMessages.addProperty("ar", "§b كن حذرًا، القاتل دائمًا يعود إلى مسرح الجريمة: المخلوق §aزومبي بالميراس§8. §f☠");
        warningMessages.addProperty("fi", "§b Ole varovainen, tappaja palaa aina rikospaikalle: Mobiili §aPalmaresin Zombi§8. §f☠");
        warningMessages.addProperty("da", "§b Pas på, morderen vender altid tilbage til gerningsstedet: Mobben §aZombie fra Palmares§8. §f☠");
        messages.add("warning", warningMessages);


        // Nenhuma localização de morte
        JsonObject noDeathMessages = new JsonObject();
        noDeathMessages.addProperty("br", "Nenhuma localização de morte registrada.");
        noDeathMessages.addProperty("en", "No death location recorded.");
        noDeathMessages.addProperty("es", "No se registró ninguna ubicación de muerte.");
        noDeathMessages.addProperty("fr", "Aucune localisation de décès enregistrée.");
        noDeathMessages.addProperty("de", "Kein Sterbeort aufgezeichnet.");
        noDeathMessages.addProperty("ru", "Не зарегистрировано местоположений смерти.");
        noDeathMessages.addProperty("zh", "没有记录死亡位置。");
        noDeathMessages.addProperty("zh-tw", "沒有記錄死亡位置。");
        noDeathMessages.addProperty("ja", "死亡位置が記録されていません。");
        noDeathMessages.addProperty("ko", "사망 위치가 기록되지 않았습니다.");
        noDeathMessages.addProperty("it", "Nessuna posizione di morte registrata.");
        noDeathMessages.addProperty("nl", "Geen sterflocatie geregistreerd.");
        noDeathMessages.addProperty("pl", "Nie zarejestrowano lokalizacji śmierci.");
        noDeathMessages.addProperty("sv", "Ingen dödsplats registrerad.");
        noDeathMessages.addProperty("cs", "Žádné místo úmrtí nebylo zaznamenáno.");
        noDeathMessages.addProperty("hu", "Nincs halálhely rögzítve.");
        noDeathMessages.addProperty("tr", "Ölüm konumu kaydedilmedi.");
        noDeathMessages.addProperty("ar", "لم يتم تسجيل موقع الوفاة.");
        noDeathMessages.addProperty("fi", "Ei kuolinpaikkaa tallennettu.");
        noDeathMessages.addProperty("da", "Ingen dødsplacering registreret.");
        messages.add("no_death", noDeathMessages);


        // Apenas jogadores
        JsonObject onlyPlayersMessages = new JsonObject();
        onlyPlayersMessages.addProperty("br", "Apenas jogadores podem usar este comando.");
        onlyPlayersMessages.addProperty("en", "Only players can use this command.");
        onlyPlayersMessages.addProperty("es", "Solo los jugadores pueden usar este comando.");
        onlyPlayersMessages.addProperty("fr", "Seuls les joueurs peuvent utiliser cette commande.");
        onlyPlayersMessages.addProperty("de", "Nur Spieler können diesen Befehl verwenden.");
        onlyPlayersMessages.addProperty("ru", "Только игроки могут использовать эту команду.");
        onlyPlayersMessages.addProperty("zh", "只有玩家可以使用此命令。");
        onlyPlayersMessages.addProperty("zh-tw", "只有玩家可以使用此指令。");
        onlyPlayersMessages.addProperty("ja", "このコマンドを使用できるのはプレイヤーのみです。");
        onlyPlayersMessages.addProperty("ko", "이 명령어는 플레이어만 사용할 수 있습니다.");
        onlyPlayersMessages.addProperty("it", "Solo i giocatori possono usare questo comando.");
        onlyPlayersMessages.addProperty("nl", "Alleen spelers kunnen dit commando gebruiken.");
        onlyPlayersMessages.addProperty("pl", "Tylko gracze mogą używać tej komendy.");
        onlyPlayersMessages.addProperty("sv", "Endast spelare kan använda detta kommando.");
        onlyPlayersMessages.addProperty("cs", "Tento příkaz mohou použít pouze hráči.");
        onlyPlayersMessages.addProperty("hu", "Csak a játékosok használhatják ezt a parancsot.");
        onlyPlayersMessages.addProperty("tr", "Bu komutu yalnızca oyuncular kullanabilir.");
        onlyPlayersMessages.addProperty("ar", "فقط اللاعبين يمكنهم استخدام هذا الأمر.");
        onlyPlayersMessages.addProperty("fi", "Vain pelaajat voivat käyttää tätä komentoa.");
        onlyPlayersMessages.addProperty("da", "Kun spillere kan bruge denne kommando.");
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