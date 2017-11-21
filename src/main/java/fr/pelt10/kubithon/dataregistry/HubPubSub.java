package fr.pelt10.kubithon.dataregistry;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.stream.Collectors;

public class HubPubSub extends JedisPubSub implements Runnable {
    private JedisPool jedisPool;
    private List<HubInstance> hubInstanceList;
    private boolean run = true;

    public HubPubSub(JedisPool jedisPool, List<HubInstance> hubInstanceList) {
        this.jedisPool = jedisPool;
        this.hubInstanceList = hubInstanceList;
    }

    @Override
    public void run() {
        while (run) {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(this, RedisKeys.HUB_PUBSUB_CHANNEL);
            }
        }
    }

    @Override
    public void onMessage(String channel, String message) {
        String command = message.split(" ")[0];
        String[] args = message.replace(command + " ", "").split(" ");

        switch (command) {
            case RedisKeys.PUBSUB_CMD_NEW_HUB:
                hubInstanceList.add(HubInstance.deserialize(args[0]));
                break;
            case RedisKeys.PUBSUB_CMD_DELETE_HUB:
                hubInstanceList  = hubInstanceList.stream().filter(hub -> hub.getHubID().equals(args[0])).collect(Collectors.toList());
                break;
            default:
                //TODO Log ?
                break;
        }
    }

    @Override
    public void unsubscribe() {
        run = false;
        super.unsubscribe();
    }
}
