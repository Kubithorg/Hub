package fr.pelt10.kubithon.dataregistry;

import lombok.Getter;

public class RedisKeys {
    @Getter
    private static final int HUB_DB_ID = 1;
    @Getter
    private static final String HUB_KEY_NAME = "HUB";
    @Getter
    private static final String HUB_PUBSUB_CHANNEL = "HUB_PUBSUB";
}
