package AncapPay;

import AncapPay.Configurations.DataBaseCore;
import AncapPay.Utils.QiwiModule;
import org.bukkit.Sound;
import AncapPay.Configurations.MainConfiguration;
import AncapPay.Utils.UsefulFunctions;
import AncapPay.Configurations.MessagesConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class QiwiCommands implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (true) {
            final Player p = (Player)sender;
            if (args.length == 0) {
                for (final String s : MessagesConfiguration.getMessages().getConfig().getStringList("Messages.Help")) {
                    p.sendMessage(UsefulFunctions.color(s));
                }
                return false;
            }
            final String lowerCase = args[0].toLowerCase();
            switch (lowerCase) {
                case "reload": {
                    if (p.hasPermission("qiwipay.reload")) {
                        MainConfiguration.getMain().reloadConfig();
                        MessagesConfiguration.getMessages().reloadConfig();
                        main.reloadToken();
                        UsefulFunctions.sendMessage(p, "Messages.Another.Reloaded");
                        UsefulFunctions.sendLog("&6Конфиг перезагружен");
                        UsefulFunctions.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
                        break;
                    }
                    UsefulFunctions.sendMessage(p, "Messages.Another.NoPerm");
                    UsefulFunctions.playSound(p, Sound.BLOCK_ANVIL_PLACE);
                    break;
                }
                case "pay": {
                    if (args.length != 2 || !isNumeric(args[1])) {
                        UsefulFunctions.sendMessage(p, "Messages.Another.ArgError");
                        return true;
                    }
                    QiwiModule.generateBill(p, Integer.parseInt(args[1]));
                    UsefulFunctions.sendLog(UsefulFunctions.config("messages","Messages.Console.PayLink", p, Integer.parseInt(args[1])));
                    UsefulFunctions.playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
                    break;
                }
                case "check": {
                    if (QiwiModule.getClients().containsKey(p.getUniqueId())) {
                        QiwiModule.checkBill(p);
                        break;
                    }
                    UsefulFunctions.sendMessage(p, "Messages.Another.NoBill");
                    UsefulFunctions.playSound(p, Sound.BLOCK_ANVIL_PLACE);
                    break;
                }
                case "reject": {
                    if (QiwiModule.getClients().containsKey(p.getUniqueId())) {
                        QiwiModule.getClients().remove(p.getUniqueId());
                        UsefulFunctions.sendMessage(p, "Messages.Another.BillRejected");
                        UsefulFunctions.sendLog(UsefulFunctions.config("messages","Messages.Console.RejectLink", p, 0));
                        UsefulFunctions.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
                        break;
                    }
                    UsefulFunctions.sendMessage(p, "Messages.Another.NoBill");
                    UsefulFunctions.playSound(p, Sound.BLOCK_ANVIL_PLACE);
                    break;
                }
                default: {
                    p.sendMessage(UsefulFunctions.config("messages","Messages.Another.NoArg", p, 0));
                    for (final String s2 : MessagesConfiguration.getMessages().getConfig().getStringList("Messages.Help")) {
                        p.sendMessage(UsefulFunctions.color(s2));
                    }
                    UsefulFunctions.playSound(p, Sound.BLOCK_ANVIL_PLACE);
                    break;
                }
            }
        }
        return false;
    }

    public static boolean isNumeric(final String str) {
        try {
            Integer.parseInt(str);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
