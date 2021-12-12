package AncapPay.Utils;

import com.qiwi.billpayments.sdk.client.BillPaymentClientFactory;
import com.qiwi.billpayments.sdk.web.ApacheWebClient;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.apache.http.impl.client.HttpClients;
import AncapPay.main;
import AncapPay.database.DataBaseWrite;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import com.qiwi.billpayments.sdk.model.out.BillResponse;
import org.bukkit.Sound;
import java.net.URISyntaxException;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import com.qiwi.billpayments.sdk.model.in.CreateBillInfo;
import com.qiwi.billpayments.sdk.model.in.Customer;
import java.time.ZonedDateTime;
import com.qiwi.billpayments.sdk.model.MoneyAmount;
import java.util.Currency;
import AncapPay.Configurations.MainConfiguration;
import java.math.BigDecimal;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.HashMap;
import com.qiwi.billpayments.sdk.client.BillPaymentClient;

public class QiwiModule
{
    public static BillPaymentClient client;
    private static HashMap<UUID, String> clients;

    public static HashMap<UUID, String> getClients() {
        return QiwiModule.clients;
    }

    public static void generateBill(final Player p, final int sum) {
        if (!getClients().containsKey(p.getUniqueId())) {
            final CreateBillInfo billInfo = new CreateBillInfo(UUID.randomUUID().toString(), new MoneyAmount(BigDecimal.valueOf(sum), Currency.getInstance(MainConfiguration.getMain().getConfig().getString("QiwiPay.Currency"))), UsefulFunctions.config("messages","Messages.Bill.Message", p, sum), ZonedDateTime.now().plusHours(1L), new Customer(MainConfiguration.getMain().getConfig().getString("QiwiPay.Email"), UUID.randomUUID().toString(), MainConfiguration.getMain().getConfig().getString("QiwiPay.Phone")), MainConfiguration.getMain().getConfig().getString("QiwiPay.Site"));
            try {
                final BillResponse response = QiwiModule.client.createBill(billInfo);
                QiwiModule.clients.put(p.getUniqueId(), response.getBillId());
                String url = response.getPayUrl();
                String msg = UsefulFunctions.config("messages","Messages.Json.Message", p, sum);
                String msg2 = UsefulFunctions.config("messages","Messages.Json.Message2", p, sum);
                String msgBorders = UsefulFunctions.config("messages","Messages.Json.Message3", p, sum);
                p.sendMessage(msgBorders);
                p.spigot().sendMessage(new ComponentBuilder(msg)
                        .event(new ClickEvent (ClickEvent.Action.OPEN_URL, url))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(UsefulFunctions.config("messages","Messages.Json.Message4", p, sum))))
                        .create());
                p.sendMessage(msg2);
                p.sendMessage(msgBorders);
            }
            catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
        }
        else {
            UsefulFunctions.sendMessage(p, "Messages.Another.ActiveBill");
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0f, 1.0f);
        }
    }

    public static void onPaid(final Player p) {
        final BillResponse response = QiwiModule.client.getBillInfo(getClients().get(p.getUniqueId()));
        final int amount = response.getAmount().getValue().intValue() * MainConfiguration.getMain().getConfig().getInt("QiwiPay.Multiplication");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MainConfiguration.getMain().getConfig().getString("QiwiPay.Command").replace("%player%", p.getName()).replace("%amount%", "" + amount));
        getClients().remove(p.getUniqueId());
        UsefulFunctions.sendMessage(p, "Messages.Status.Paid");
        DataBaseWrite.addPlayerDonate(p, amount);
        UsefulFunctions.playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
        UsefulFunctions.sendLog(ChatColor.LIGHT_PURPLE + p.getName() + " "+UsefulFunctions.config("messages","Messages.Console.Message", p, amount));
    }

    public static void checkBill(final Player p) {
        final BillResponse response = QiwiModule.client.getBillInfo(getClients().get(p.getUniqueId()));
        switch (response.getStatus().getValue()) {
            case PAID: {
                QiwiModule.onPaid(p);
                break;
            }
            case WAITING: {
                UsefulFunctions.sendMessage(p, "Messages.Status.Waiting");
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0f, 1.0f);
                break;
            }
            case REJECTED: {
                UsefulFunctions.sendMessage(p, "Messages.Status.Rejected");
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0f, 1.0f);
                break;
            }
            case EXPIRED: {
                UsefulFunctions.sendMessage(p, "Messages.Status.Expired");
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_FALL, 1.0f, 1.0f);
                break;
            }
        }
    }

    static {
        QiwiModule.client = BillPaymentClientFactory.createCustom(main.getQiwi(), new ApacheWebClient(HttpClients.createDefault()));
        QiwiModule.clients = new HashMap<UUID, String>();
    }
}
