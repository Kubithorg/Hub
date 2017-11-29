package fr.pelt10.kubithon.hub.com.messages;

import fr.pelt10.kubithon.hub.com.CommunicationMessage;
import fr.pelt10.kubithon.hub.dataregistry.HubPubSub;
import fr.pelt10.kubithon.hub.dataregistry.JedisUtils;
import fr.pelt10.kubithon.hub.dataregistry.RedisKeys;

import java.util.UUID;

public class PlayerTeleportMessage implements CommunicationMessage {
    private JedisUtils jedisUtils;

    public PlayerTeleportMessage(JedisUtils jedisUtils) {
        this.jedisUtils = jedisUtils;
    }

    @Override
    public void send(Object[] datas) {
        if (datas.length != 2 && datas[0] instanceof UUID && datas[1] instanceof String) {
            jedisUtils.execute(jedis -> jedis.publish(RedisKeys.COMM_PUBSUB_CHANNEL, RedisKeys.COMM_PLAYER_TP + " " + ((UUID)datas[0]).toString() + " " + datas[1]));
        }
    }
}
