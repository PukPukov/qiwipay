package AncapPay.Configurations;

import java.io.InputStream;
import java.io.IOException;
import org.bukkit.Bukkit;
import java.nio.file.Files;

import org.bukkit.configuration.file.YamlConfiguration;
import AncapPay.main;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;

public class MainConfiguration
{
    private static MainConfiguration mcfg;
    private File cfgFile;
    private FileConfiguration cfg;

    public void setUp() {
        if (this.cfgFile == null) {
            this.cfgFile = new File(main.getInstance().getDataFolder(), "QiwiConfiguration.yml");
        }
        this.cfg = YamlConfiguration.loadConfiguration(this.cfgFile);
        if (!this.cfgFile.exists()) {
            try (final InputStream in = main.getInstance().getResource("QiwiConfiguration.yml")) {
                Files.copy(in, this.cfgFile.toPath());
                this.cfg = YamlConfiguration.loadConfiguration(this.cfgFile);
                Bukkit.getServer().getConsoleSender().sendMessage("[QiwiPay] Файл конфигурации успешно создан.");
            }
            catch (IOException e) {
                Bukkit.getServer().getConsoleSender().sendMessage("[QiwiPay] Произошла ошибка при создании файла конфигурации.");
            }
        }
    }

    public void reloadConfig() {
        this.cfgFile = new File(main.getInstance().getDataFolder(), "QiwiConfiguration.yml");
        this.cfg = YamlConfiguration.loadConfiguration(this.cfgFile);
    }

    public FileConfiguration getConfig() {
        if (this.cfg == null) {
            this.reloadConfig();
        }
        return this.cfg;
    }

    public void saveConfig() {
        try {
            this.getConfig().save(this.cfgFile);
        }
        catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("Ошибка в сохранении файла конфигурации: " + this.cfgFile);
        }
    }

    public static MainConfiguration getMain() {
        return MainConfiguration.mcfg;
    }

    static {
        MainConfiguration.mcfg = new MainConfiguration();
    }
}
