package fr.pelt10.kubithon.hub.utils;

import com.google.gson.Gson;
import lombok.Getter;

public class ServerInstance {
    private static Gson gson = new Gson();
    @Getter
    private String HubID;
    @Getter
    private String ip;
    @Getter
    private int port;

    public ServerInstance(String HubID, String ip, int port) {
        this.HubID = HubID;
        this.ip = ip;
        this.port = port;
    }

    public static String serialize(ServerInstance hubInstance) {
        return gson.toJson(hubInstance);
    }

    public static ServerInstance deserialize(String string) {
        return gson.fromJson(string, ServerInstance.class);
    }
}
