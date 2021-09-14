package AncapPay;

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
        if (sender instanceof Player) {
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
                    if (args.length != 1 || isNumeric(args[1])) {
                        QiwiModule.generateBill(p, Integer.parseInt(args[1]));
                        UsefulFunctions.sendLog("&d" + p.getName() + " &6создал ссылку на оплату &d" + args[1]);
                        UsefulFunctions.playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
                        break;
                    }
                    UsefulFunctions.sendMessage(p, "Messages.Another.ArgError");
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
                        UsefulFunctions.sendLog("&d" + p.getName() + " &6отменил оплату");
                        UsefulFunctions.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
                        break;
                    }
                    UsefulFunctions.sendMessage(p, "Messages.Another.NoBill");
                    UsefulFunctions.playSound(p, Sound.BLOCK_ANVIL_PLACE);
                    break;
                }
                default: {
                    p.sendMessage(UsefulFunctions.color("&cОшибка аргумента!"));
                    for (final String s2 : MessagesConfiguration.getMessages().getConfig().getStringList("Messages.Help")) {
                        p.sendMessage(UsefulFunctions.color(s2));
                    }
                    UsefulFunctions.playSound(p, Sound.BLOCK_ANVIL_PLACE);
                    break;
                }
            }
        }
        else if (args[0] != null) {
            if (args[0].toLowerCase().equals("reload")) {
                MainConfiguration.getMain().reloadConfig();
                MessagesConfiguration.getMessages().reloadConfig();
                main.reloadToken();
                UsefulFunctions.sendLog("&6Конфиг перезагружен");
            }
            else {
                sender.sendMessage("Не для консоли.");
            }
        }
        else {
            for (final String s3 : MessagesConfiguration.getMessages().getConfig().getStringList("Messages.Help")) {
                sender.sendMessage(UsefulFunctions.color(s3));
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
