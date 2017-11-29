package fr.pelt10.kubithon.hub.com;

import java.util.ArrayList;
import java.util.List;

public class CommunicationManager {
    private List<CommunicationMessage> messages = new ArrayList<>();

    public void execute(Class<? extends CommunicationMessage> clazz, Object... datas) {
        messages.stream().filter(communicationMessage -> communicationMessage.getClass().getName().equals(clazz.getName())).findFirst().ifPresent(communicationMessage -> communicationMessage.send(datas));
    }

    public void registerMessage(CommunicationMessage message) {
        if(messages.stream().map(CommunicationMessage::getClass).anyMatch(name -> name.equals(message.getClass()))) {
            throw new UnsupportedOperationException(message.getClass().getCanonicalName() + " is already register !");
        }
        messages.add(message);
    }
}
