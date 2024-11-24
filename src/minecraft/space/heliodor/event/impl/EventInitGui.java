package space.heliodor.event.impl;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import space.heliodor.event.Event;

public class EventInitGui extends Event {
    public GuiChest gui;
    public void setGui(GuiChest gui) {
        this.gui = gui;
    }

    public GuiChest getGui() {
        return this.gui;
    }
}
