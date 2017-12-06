package fr.pelt10.kubithon.hub.listeners;

import fr.pelt10.kubithon.hub.Hub;

public abstract class KubiListener {

    public KubiListener(Hub hub) {
        hub.getGame().getEventManager().registerListeners(hub, this);
    }
}
