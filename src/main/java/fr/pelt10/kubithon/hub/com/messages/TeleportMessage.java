package fr.pelt10.kubithon.hub.com.messages;

import fr.pelt10.kubithon.hub.com.CommunicationMessage;
import fr.pelt10.kubithon.hub.dataregistry.JedisUtils;

import java.util.UUID;

public class TeleportMessage implements CommunicationMessage {
    private JedisUtils jedisUtils;

    public TeleportMessage(JedisUtils jedisUtils) {
        this.jedisUtils = jedisUtils;
    }

    @Override
    public void execute(Object[] datas) {
        if (datas.length != 2 && datas[0] instanceof UUID && datas[1] instanceof String) {

        }
    }
}
