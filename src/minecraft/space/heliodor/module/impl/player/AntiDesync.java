package space.heliodor.module.impl.player;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventRecievePacket;
import space.heliodor.module.Category;
import space.heliodor.module.Module;

public class AntiDesync extends Module {
    public AntiDesync() {
        super("AntiDesync",0, Category.PLAYER);
    }

    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof EventRecievePacket && mc.thePlayer != null && mc.theWorld != null) {
            if(((EventRecievePacket) event).getPacket() instanceof S08PacketPlayerPosLook) {
                S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) ((EventRecievePacket) event).getPacket();
                packet.yaw = mc.thePlayer.rotationYaw;
                packet.pitch = mc.thePlayer.rotationPitch;
            }
        }
    }
}
