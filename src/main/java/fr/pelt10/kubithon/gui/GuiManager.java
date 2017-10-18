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
    private List<InventoryGUI> gui = new ArrayList<>();

    public GuiManager(Hub hub) {
        this.hub = hub;
        this.logger = hub.getLogger();
    }

    @Listener
    public void onClickInventory(ClickInventoryEvent event){
        event.setCancelled(true);
        Inventory inventory = event.getTargetInventory();
        logger.info("Inventory Name : " + inventory.getName().get());

        gui.stream().filter(inv -> inv.getName().equalsIgnoreCase(inventory.getName().get())).forEach(inv -> inv.onAction(event));
    }

    public void registerGUI(InventoryGUI gui) {
        if (this.gui.stream().anyMatch(g -> g.getName().equalsIgnoreCase(gui.getName()))){
            throw new IllegalArgumentException("An GUI already have this name.");
        }
        this.gui.add(gui);
    }

    public Optional<InventoryGUI> getGUI(String name) {
        return gui.stream().filter(gui -> gui.getName().equalsIgnoreCase("name")).findFirst();
    }
}
