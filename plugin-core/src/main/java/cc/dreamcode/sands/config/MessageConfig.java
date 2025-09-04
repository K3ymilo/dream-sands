package cc.dreamcode.sands.config;

import cc.dreamcode.notice.bukkit.BukkitNotice;
import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.annotation.Headers;

@Configuration(child = "message.yml")
@Headers({
        @Header("## Dream-Sands (Message-Config) ##"),
        @Header("Dostepne type: (DO_NOT_SEND, CHAT, ACTION_BAR, SUBTITLE, TITLE, TITLE_SUBTITLE)")
})
public class MessageConfig extends OkaeriConfig {

    @CustomKey("command-usage")
    public BukkitNotice usage = BukkitNotice.chat("&7Przyklady uzycia komendy: &c{label}");
    @CustomKey("command-usage-help")
    public BukkitNotice usagePath = BukkitNotice.chat("&f{usage} &8- &7{description}");

    @CustomKey("command-usage-not-found")
    public BukkitNotice usageNotFound = BukkitNotice.chat("&cNie znaleziono pasujacych do kryteriow komendy.");
    @CustomKey("command-path-not-found")
    public BukkitNotice pathNotFound = BukkitNotice.chat("&cTa komenda jest pusta lub nie posiadasz dostepu do niej.");
    @CustomKey("command-no-permission")
    public BukkitNotice noPermission = BukkitNotice.chat("&cNie posiadasz uprawnien.");
    @CustomKey("command-not-player")
    public BukkitNotice notPlayer = BukkitNotice.chat("&cTa komende mozna tylko wykonac z poziomu gracza.");
    @CustomKey("command-not-console")
    public BukkitNotice notConsole = BukkitNotice.chat("&cTa komende mozna tylko wykonac z poziomu konsoli.");
    @CustomKey("command-invalid-format")
    public BukkitNotice invalidFormat = BukkitNotice.chat("&cPodano nieprawidlowy format argumentu komendy. ({input})");

    @CustomKey("player-not-found")
    public BukkitNotice playerNotFound = BukkitNotice.chat("&cPodanego gracza nie znaleziono.");
    @CustomKey("world-not-found")
    public BukkitNotice worldNotFound = BukkitNotice.chat("&cPodanego swiata nie znaleziono.");
    @CustomKey("cannot-do-at-my-self")
    public BukkitNotice cannotDoAtMySelf = BukkitNotice.chat("&cNie mozesz tego zrobic na sobie.");
    @CustomKey("number-is-not-valid")
    public BukkitNotice numberIsNotValid = BukkitNotice.chat("&cPodana liczba nie jest cyfra.");

    @CustomKey("config-reloaded")
    public BukkitNotice reloaded = BukkitNotice.chat("&aPrzeladowano! &7({time})");
    @CustomKey("config-reload-error")
    public BukkitNotice reloadError = BukkitNotice.chat("&cZnaleziono problem w konfiguracji: &6{error}");

    @CustomKey("killstreak-message")
    public BukkitNotice killStreakMessage = BukkitNotice.chat("&aTwój killstreak wynosi: &f{killstreak}ks");

    @CustomKey("setspawn-location")
    public BukkitNotice setSpawnLocation = BukkitNotice.chat("&8[&f!&8] &aPomyslnie ustawiono lokalizacje spawna.");
    @CustomKey("spawn-already-teleporting-message")
    public BukkitNotice spawnAlreadyTeleportingMessage = BukkitNotice.title("&aTrwa Teleportacja...");
    @CustomKey("success-teleport-spawn-message")
    public BukkitNotice successMessage = BukkitNotice.subtitle("&7Zostałeś &fprzeteleportowany&7!");
    @CustomKey("move-teleport-spawn-message")
    public BukkitNotice moveTeleportSpawnMessage = BukkitNotice.subtitle("&cTwoja teleportacja została przerwana!");
    @CustomKey("setspawn-end-location")
    public BukkitNotice setEndSpawnLocation = BukkitNotice.chat("&8[&f!&8] &aPomyslnie ustawiono lokalizacje spawna endu.");
    @CustomKey("setspawn-nether-location")
    public BukkitNotice setSpawnNetherLocation = BukkitNotice.chat("&8[&f!&8] &aPomyslnie ustawiono lokalizacje spawna netheru.");
    @CustomKey("teleport-message")
    public BukkitNotice teleportMessage = BukkitNotice.subtitle("&7Zostaniesz &fprzeteleportowany &fza &e{time}&7!");
}
