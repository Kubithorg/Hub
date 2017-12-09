package fr.pelt10.kubithon.hub.listeners;

import fr.pelt10.kubithon.hub.Hub;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class PlayerQuit extends KubiListener {
    public PlayerQuit(Hub hub) {
        super(hub);
    }

    @Listener
    public void onPlayerQuit(ClientConnectionEvent.Disconnect event) {
        event.setMessageCancelled(true);
    }
}
