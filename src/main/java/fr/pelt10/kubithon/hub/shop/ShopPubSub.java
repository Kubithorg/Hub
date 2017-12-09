package fr.pelt10.kubithon.hub.shop;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.pelt10.kubithon.hub.Hub;
import fr.pelt10.kubithon.hub.dataregistry.RedisKeys;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import redis.clients.jedis.JedisPubSub;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class ShopPubSub extends JedisPubSub {
    private boolean run = true;
    private Hub hub;

    public ShopPubSub(Hub hub) {
        this.hub = hub;
        hub.getDataManager().getJedisUtils().execute(jedis -> {
            while (run) {
                jedis.subscribe(this, RedisKeys.SHOP_LOGIN_PUBSUB);
            }
        });
    }

    @Override
    public void onMessage(String channel, String message) {
        JsonObject obj = (JsonObject) new JsonParser().parse(message);

        hub.getGame().getServer().getPlayer(UUID.fromString(obj.get("uuid").getAsString())).ifPresent(player -> {
            try {

                Text msg = Text.builder("\n\nPour autoriser la connexion cliquez ici.\n\n")
                        .onClick(TextActions.openUrl(new URL(obj.get("url").getAsString())))
                        .color(TextColors.GOLD)
                        .style(TextStyles.BOLD)
                        .build();

                player.sendMessage(msg);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void unsubscribe() {
        run = false;
        super.unsubscribe();
    }
}
