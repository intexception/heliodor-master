package space.heliodor.module.impl.player;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import space.heliodor.Heliodor;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventInitGui;
import space.heliodor.event.impl.EventUpdate;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.settings.OptionBool;
import space.heliodor.settings.OptionNumber;

import java.util.Random;

public class ChestStealer extends Module {
    public OptionBool silent = new OptionBool("Silent", true);
    public OptionNumber delay = new OptionNumber("Delay", 150, 10, 300, 10);
    public ChestStealer() {
        super("Stealer", Keyboard.KEY_K, Category.PLAYER);
        this.options.add(silent);
        this.options.add(delay);
    }

    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof EventInitGui) {
            if(!silent.isEnabled()) {
                new Thread(() -> {
                    try {
                        for(int i = 0; i < ((EventInitGui) event).getGui().inventoryRows * 9; i++) {
                            Slot slot = ((EventInitGui) event).getGui().inventorySlots.inventorySlots.get(i);
                            if(slot.getStack() != null) {
                                Thread.sleep((long) delay.getVal() + new Random().nextInt(30));
                                ((EventInitGui) event).getGui().handleMouseClick(slot, slot.slotNumber, 0, 1);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            else {
                this.mc.displayGuiScreen(null);
                this.mc.thePlayer.inventory.setItemStack(null);
                new Thread(() -> {
                    try {
                        for(int i = 0; i < ((EventInitGui) event).getGui().inventoryRows * 9; i++) {
                            Slot slot = ((EventInitGui) event).getGui().inventorySlots.inventorySlots.get(i);
                            if(slot.getStack() != null) {
                                Thread.sleep((long) delay.getVal());
                                ((EventInitGui) event).getGui().handleMouseClick(slot, slot.slotNumber, 0, 1);
                            }
                        }
                        mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }
}