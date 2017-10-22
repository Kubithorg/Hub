package fr.pelt10.kubithon.gui.template;

import fr.pelt10.kubithon.Hub;
import fr.pelt10.kubithon.gui.InventoryGUI;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.text.Text;

public class MainMenu extends InventoryGUI {

    public MainMenu(Hub hub) {
        super(hub);
    }

    @Override
    public void onAction(ClickInventoryEvent event) {
        event.getCause().first(Player.class).ifPresent(player -> player.sendMessage(Text.of("Ceci est un super test !")));
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
