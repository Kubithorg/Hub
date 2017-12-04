package fr.pelt10.kubithon.hub.utils;

import fr.pelt10.kubithon.hub.Hub;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.ArrayList;
import java.util.List;

public class HidePlayers {
    private List<Player> players = new ArrayList<>();
    private Hub hub;
    private final String PERMISSION = "kubithon.hub.displayplayer";

    public HidePlayers(Hub hub) {
        this.hub = hub;
        hub.getGame().getEventManager().registerListeners(hub, this);
    }

    public void hidePlayerFor(Player player) {
        players.add(player);

        EntityPlayerMP thePlayer = (EntityPlayerMP)player;

        hub.getGame().getServer().getOnlinePlayers().stream().filter(p -> !p.hasPermission(PERMISSION)).forEach(p -> {
            hidePlayer(thePlayer, p);
        });
    }

    @Listener
    public void onConnect(ClientConnectionEvent.Join event) {
        Player player = event.getTargetEntity();
        if(!player.hasPermission(PERMISSION)) {
            players.stream().map(p -> (EntityPlayerMP)p).forEach(p -> hidePlayer(p, player));
        }
    }

    private void hidePlayer(EntityPlayerMP player, Player hide) {
        player.connection.sendPacket(new SPacketDestroyEntities(((EntityPlayerMP)hide).getEntityId()));
    }
}
