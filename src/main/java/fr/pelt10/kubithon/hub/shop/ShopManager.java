package fr.pelt10.kubithon.hub.shop;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.pelt10.kubithon.hub.Hub;
import fr.pelt10.kubithon.hub.dataregistry.RedisKeys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ShopManager {
    private ShopPubSub shopPubSub;

    public ShopManager(Hub hub) {
        shopPubSub = new ShopPubSub(hub);
        new Thread(shopPubSub).start();

        Task.builder().execute(() ->
                hub.getDataManager().getJedisUtils().execute(jedis -> {
                    Set<String> datas = jedis.smembers(RedisKeys.SHOP_COMMAND);
                    if(datas != null){
                        datas.forEach(s -> {
                            JsonObject obj = (JsonObject) new JsonParser().parse(s);

                            String uuid = obj.get("id").getAsString().replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");

                            hub.getGame().getServer().getPlayer(UUID.fromString(uuid)).ifPresent(player -> {
                                JsonArray jsonArray = obj.get("commands").getAsJsonArray();
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    hub.getGame().getCommandManager().process(hub.getGame().getServer().getConsole(), jsonArray.get(i).getAsString());
                                }
                                jedis.srem(RedisKeys.SHOP_COMMAND, s);
                            });
                        });
                    }
                })
        ).interval(1, TimeUnit.MINUTES).submit(hub);
    }

    public void disable() {
        shopPubSub.unsubscribe();
    }
}
