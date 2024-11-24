package space.heliodor.module.impl.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventPreMotionUpdate;
import space.heliodor.event.impl.EventRecievePacket;
import space.heliodor.event.impl.EventSendPacket;
import space.heliodor.module.Category;
import space.heliodor.module.Module;

public class AntiVoid extends Module {
    public AntiVoid() {
        super("AntiVoid",0, Category.PLAYER);
    }

    @OnEvent
    public void onEvent(Event event) {
        this.setDisplayName("AntiVoid " + EnumChatFormatting.GRAY.toString() + "Position");
        if(event instanceof EventPreMotionUpdate) {
            if (mc.thePlayer.fallDistance > 3 && !mc.thePlayer.capabilities.isFlying && !canAntiVoid()) {
                mc.thePlayer.sendQueue.sendQueueBypass(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3, mc.thePlayer.posZ, false));
                mc.thePlayer.sendQueue.sendQueueBypass(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
            }
        }
        if(event instanceof EventSendPacket) {
            if (mc.thePlayer.fallDistance > 3 && !mc.thePlayer.capabilities.isFlying && !canAntiVoid()) {
                if(((EventSendPacket) event).getPacket() instanceof C03PacketPlayer) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public boolean canAntiVoid() {
        for (int block = 0; block < mc.thePlayer.posY + 1.6; block += 2) {
            if (mc.theWorld.isAirBlock(new BlockPos(0, -block, 0))) {
                return false;
            }
        }
        return true;
    }
}
