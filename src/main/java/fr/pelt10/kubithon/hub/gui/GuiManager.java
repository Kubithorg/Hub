package fr.pelt10.kubithon.hub.gui;

import fr.pelt10.kubithon.hub.Hub;
import org.slf4j.Logger;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GuiManager {
    private static List<InventoryGUI> gui = new ArrayList<>();
    private Logger logger;
    private Hub hub;

    public GuiManager(Hub hub) {
        this.hub = hub;
        this.logger = hub.getLogger();
    }

    @Listener
    public void onClickInventory(ClickInventoryEvent event) {
        Player player = event.getCause().first(Player.class).get();
        if (!player.hasPermission("kubithon.hub.inventoryfree")) {
            event.setCancelled(true);
        }
        Inventory inventory = event.getTargetInventory();

        gui.stream().filter(inv -> inv.getDisplayName().equalsIgnoreCase(inventory.getName().get())).forEach(inv -> inv.onAction(event));
    }

    public void registerGUI(InventoryGUI gui) {
        if (this.gui.stream().anyMatch(g -> g.getClass().getName().equals(gui.getClass().getName()))) {
            throw new IllegalArgumentException("GUI already registered");
        }
        this.gui.add(gui);
    }

    public <T extends InventoryGUI> Optional<InventoryGUI> getGUI(Class<T> clazz) {
        return gui.stream().filter(g -> g.getClass().getName().equals(clazz.getName())).findFirst();
    }
}
