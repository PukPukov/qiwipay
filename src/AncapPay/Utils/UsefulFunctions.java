package AncapPay.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import AncapPay.Configurations.MessagesConfiguration;

public class UsefulFunctions {
    public static String color(String mes) {
        return ChatColor.translateAlternateColorCodes('&', mes);
    }

    public static void sendUsefulMessage(Player p, String path) {
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
