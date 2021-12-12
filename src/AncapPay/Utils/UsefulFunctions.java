package AncapPay.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import AncapPay.Configurations.DataBaseCore;
import AncapPay.Configurations.MainConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import AncapPay.Configurations.MessagesConfiguration;

public class UsefulFunctions {
    public static String color(String mes) {
        return mes.replace("&", "§");
    }

    public static String placeholder(String mes, Player p, int amount) {
        String mes1 = mes.replace("%player%", p.getName());
        String mes2 = mes1.replace("%amount%", String.valueOf(amount));
        return mes2;
    }

    public static String configOperator(String mes, Player p, int amount) {
        String mes1 = UsefulFunctions.placeholder(mes, p, amount);
        String mes2 = UsefulFunctions.color(mes1);
        return mes2;
    }

    public static String config(String db, String path, Player p, int amount) {
        if (db.equals("messages")) {
            String mes = MessagesConfiguration.getMessages().getConfig().getString(path);
            String mes1 = UsefulFunctions.configOperator(mes, p, amount);
            return mes1;
        }
        if (db.equals("qiwi")) {
            String mes = MainConfiguration.getMain().getConfig().getString(path);
            String mes1 = UsefulFunctions.configOperator(mes, p, amount);
            return mes1;
        }
        if (db.equals("database")) {
            String mes = DataBaseCore.getDataBase().getConfig().getString(path);
            String mes1 = UsefulFunctions.configOperator(mes, p, amount);
            return mes1;
        }
        else return "<no existing string file selected>";
    }

    public static void sendMessage(Player p, String path) {
        String messages = MessagesConfiguration.getMessages().getConfig().getString(path);
        p.sendMessage(color(messages));
    }

    public static void sendLog(String log) {
        Logger.getLogger("QiwiPay").info(color(log));
    }

    public static void playSound(Player p, Sound sound) {
        p.playSound(p.getLocation(), sound, 1.0F, 1.0F);
    }

    public static String getDate(Date date) {
        String strDateFormat = "d-MM-yyyy_H-m-s";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }
}
