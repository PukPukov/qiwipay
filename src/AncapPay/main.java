package AncapPay;

import AncapPay.Metrics;
import AncapPay.Configurations.MessagesConfiguration;
import AncapPay.Configurations.DataBaseCore;
import AncapPay.Configurations.MainConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin
{
    private static main instance;
    private static String qiwiToken;

    public static main getInstance() {
        return main.instance;
    }

    public static String getQiwi() {
        return main.qiwiToken;
    }

    public void onEnable() {
        this.getLogger().info("QiwiPay enabled.");
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
        main.instance = this;
        MainConfiguration.getMain().setUp();
        DataBaseCore.getDataBase().setUp();
        MessagesConfiguration.getMessages().setUp();
        reloadToken();
        this.getCommand("donate").setExecutor(new QiwiCommands());
        int pluginId = 12879;
        Metrics metrics = new Metrics(this, pluginId);
    }

    public void onDisable() {
        this.getLogger().info("QiwiPay disabled.");
        DataBaseCore.getDataBase().saveConfig();
    }

    public static void reloadToken() {
        main.qiwiToken = MainConfiguration.getMain().getConfig().getString("QiwiPay.Token");
    }
}
