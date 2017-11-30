package fr.pelt10.kubithon.hub.gui.template;

import fr.pelt10.kubithon.hub.Hub;
import fr.pelt10.kubithon.hub.gui.InventoryGUI;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.text.Text;

public class MainMenu extends InventoryGUI {
    private Hub hub;

    public MainMenu(Hub hub) {
        super(hub);
        this.hub = hub;
    }

    @Override
    public void onAction(ClickInventoryEvent event) {
        hub.getGuiManager().getGUI(HubMenu.class).ifPresent(menu -> event.getCause().first(Player.class).ifPresent(player -> player.openInventory(menu.getInventory())));
    }

    @Override
    public Inventory getInventory() {
        return getDefaultInventory(9, 6);
    }

    @Override
    public String getDisplayName() {
        return "Menu principal";
    }
}
