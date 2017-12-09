package fr.pelt10.kubithon.hub.shop;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.pelt10.kubithon.hub.Hub;
import fr.pelt10.kubithon.hub.dataregistry.RedisKeys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ShopManager {
    private ShopPubSub shopPubSub;

    public ShopManager(Hub hub) {
        shopPubSub = new ShopPubSub(hub);
        new Thread(shopPubSub).start();

        Task.builder().execute(() ->

                hub.getDataManager().getJedisUtils().execute(jedis -> {
                    String data = jedis.lpop(RedisKeys.SHOP_COMMAND);
                    while (data != null) {
                        JsonObject obj = (JsonObject) new JsonParser().parse(data);

                        if (obj.has("timestamp") && (System.currentTimeMillis() - obj.get("timestamp").getAsLong() <= 30000)) {
                            jedis.rpush(RedisKeys.SHOP_COMMAND, obj.toString());
                            return;
                        }

                        Optional<Player> optionalPlayer = hub.getGame().getServer().getPlayer(UUID.fromString(obj.get("id").getAsString()));
                        if (optionalPlayer.isPresent()) {
                            JsonArray jsonArray = obj.get("commands").getAsJsonArray();
                            for (int i = 0; i < jsonArray.size(); i++) {
                                hub.getGame().getCommandManager().process(hub.getGame().getServer().getConsole(), jsonArray.get(i).getAsString());
                            }
                        } else {
                            obj.addProperty("timestamp", System.currentTimeMillis());
                            jedis.rpush(RedisKeys.SHOP_COMMAND, obj.toString());
                        }
                        data = jedis.lpop(RedisKeys.SHOP_COMMAND);
                    }
                })
        ).interval(1, TimeUnit.MINUTES).submit(hub);
    }

    public void disable() {
        shopPubSub.unsubscribe();
    }
}
