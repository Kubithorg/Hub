package fr.pelt10.kubithon.gui;

import fr.pelt10.kubithon.Hub;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GuiManager {
    private Logger logger;
    private Hub hub;
    private static List<InventoryGUI> gui = new ArrayList<>();

    public GuiManager(Hub hub) {
        this.hub = hub;
        this.logger = hub.getLogger();
    }

    @Listener
    public void onClickInventory(ClickInventoryEvent event){
        event.setCancelled(true);
        Inventory inventory = event.getTargetInventory();

        gui.stream().filter(inv -> inv.getDisplayName().equalsIgnoreCase(inventory.getName().get())).forEach(inv -> inv.onAction(event));
    }

    public void registerGUI(InventoryGUI gui) {
        if (this.gui.stream().anyMatch(g -> g.getClass().getName().equals(gui.getClass().getName()))){
            throw new IllegalArgumentException("GUI already registered");
        }
        this.gui.add(gui);
    }

    public <T extends InventoryGUI> Optional<InventoryGUI> getGUI(Class<T> clazz) {
        return gui.stream().filter(g -> g.getClass().getName().equals(clazz.getName())).findFirst();
    }
}
