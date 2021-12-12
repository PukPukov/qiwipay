package AncapPay.database;

import AncapPay.Utils.UsefulFunctions;
import java.util.Date;
import AncapPay.Configurations.DataBaseCore;
import org.bukkit.entity.Player;

public class DataBaseWrite
{
    public static void addPlayerDonate(final Player p, final int amount) {
        DataBaseCore.getDataBase().getConfig().set("QiwiPay.TotalPay", DataBaseCore.getDataBase().getConfig().getInt("QiwiPay.TotalPay") + amount);
        if (DataBaseCore.getDataBase().getConfig().getConfigurationSection("QiwiPay.Players." + p.getName()) == null) {
            setNewPlayer(p, amount);
        }
        writeConf(p, amount);
        DataBaseCore.getDataBase().saveConfig();
    }

    public static void setNewPlayer(final Player p, final int amount) {
        DataBaseCore.getDataBase().getConfig().createSection("QiwiPay.Players." + p.getName());
        DataBaseCore.getDataBase().getConfig().createSection("QiwiPay.Players." + p.getName() + ".AmountAll");
        DataBaseCore.getDataBase().getConfig().createSection("QiwiPay.Players." + p.getName() + ".Donate");
        DataBaseCore.getDataBase().getConfig().set("QiwiPay.Players." + p.getName() + ".AmountAll", 0);
        DataBaseCore.getDataBase().saveConfig();
    }

    public static void writeConf(final Player p, final int amount) {
        DataBaseCore.getDataBase().getConfig().set("QiwiPay.Players." + p.getName() + ".AmountAll", DataBaseCore.getDataBase().getConfig().getInt("QiwiPay.Players." + p.getName() + ".AmountAll") + amount);
        final Date date = new Date();
        DataBaseCore.getDataBase().getConfig().createSection("QiwiPay.Players." + p.getName() + ".Donate." + UsefulFunctions.getDate(date));
        DataBaseCore.getDataBase().getConfig().set("QiwiPay.Players." + p.getName() + ".Donate." + UsefulFunctions.getDate(date), amount);
        DataBaseCore.getDataBase().saveConfig();
    }
}
