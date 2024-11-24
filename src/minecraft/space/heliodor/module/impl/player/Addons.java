package space.heliodor.module.impl.player;

import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S02PacketChat;
import space.heliodor.Heliodor;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventInitGui;
import space.heliodor.event.impl.EventRecievePacket;
import space.heliodor.event.impl.EventUpdate;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.utils.SendMessage;

public class Addons extends Module {
    public Addons() {
        super("Auto Paper", 0, Category.PLAYER);
    }
    private ItemStack itemStack;
    private boolean isInGui;
    private boolean openGui;

    @Override
    public void onEnable() {
        isInGui = false;
        openGui = false;
        itemStack = null;
    }

    @OnEvent
    public void onEvent(Event event) {
        this.setDisplayName("Auto Paper Challenge");
        if(event instanceof EventRecievePacket) {
            if(((EventRecievePacket) event).getPacket() instanceof S02PacketChat) {
                String message = ((S02PacketChat) ((EventRecievePacket) event).getPacket()).getChatComponent().getUnformattedText();
                if(message.contains("activated")) {
                    isInGui = false;
                }
                if(message.contains("opened")) {
                    isInGui = false;
                }
                if(message.contains("starts in 5")) {
                    openGui = true;
                }
            }
        }
        if(event instanceof EventUpdate && !isInGui && openGui) {
            for(int i = 0; i < 9; i++) {
                itemStack = mc.thePlayer.inventory.getStackInSlot(i);
                if(itemStack != null) {
                    if(itemStack.getItem() == Items.blaze_powder) {
                        mc.thePlayer.inventory.currentItem = i;
                        mc.gameSettings.keyBindUseItem.pressed = true;
                        isInGui = true;
                    }
                }
            }
        }
        if(event instanceof EventInitGui && this.isToggled()) {
            if(isInGui && openGui) {
                if(Heliodor.INSTANCE().moduleMgr.getModuleByName("Stealer").isToggled()) {
                    Heliodor.INSTANCE().moduleMgr.getModuleByName("Stealer").toggle();
                }
                this.mc.displayGuiScreen(null);
                this.mc.thePlayer.inventory.setItemStack(null);
                mc.gameSettings.keyBindUseItem.pressed = false;
                new Thread(() -> {
                    try {
                        for(int i = 0; i < ((EventInitGui) event).getGui().inventoryRows * 9; i++) {
                            Slot slot = ((EventInitGui) event).getGui().inventorySlots.inventorySlots.get(i);
                            if(slot.getStack() != null && slot.slotNumber == 16) {
                                ((EventInitGui) event).getGui().handleMouseClick(slot, slot.slotNumber, 0, 1);
                                ((EventInitGui) event).getGui().handleMouseClick(slot, slot.slotNumber, 0, 6);
                                SendMessage.log("Paper Challenge activated!");
                                isInGui = false;
                                openGui = false;
                                ((EventInitGui) event).gui.onGuiClosed();
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
