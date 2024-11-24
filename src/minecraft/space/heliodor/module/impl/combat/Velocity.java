package space.heliodor.module.impl.combat;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.EnumChatFormatting;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventRecievePacket;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.settings.OptionNumber;

public class Velocity extends Module {
    private static final OptionNumber verticalVelocity = new OptionNumber("Vertical Velocity", 0f, 0f, 100f, 10f);
    private static final OptionNumber horizontalVelocity = new OptionNumber("Horizontal Velocity", 0f, 0f, 100f, 10f);
    public Velocity() {
        super("Velocity",0, Category.COMBAT);
        options.add(verticalVelocity);
        options.add(horizontalVelocity);
    }

    @OnEvent
    public void onEvent(Event event) {
        this.setDisplayName("Velocity " + EnumChatFormatting.GRAY.toString() + horizontalVelocity.getVal() + "% " + verticalVelocity.getVal() + "%");
        if(event instanceof EventRecievePacket) {
            if(((EventRecievePacket) event).getPacket() instanceof S12PacketEntityVelocity || ((EventRecievePacket) event).getPacket() instanceof S27PacketExplosion) {
                if(((EventRecievePacket) event).getPacket() instanceof S12PacketEntityVelocity) {
                    if(verticalVelocity.getVal() == 0 && horizontalVelocity.getVal() == 0) {
                        ((S12PacketEntityVelocity) ((EventRecievePacket) event).getPacket()).motionX = 0;
                        ((S12PacketEntityVelocity) ((EventRecievePacket) event).getPacket()).motionY = 0;
                        ((S12PacketEntityVelocity) ((EventRecievePacket) event).getPacket()).motionZ = 0;
                    }
                    else {
                        ((S12PacketEntityVelocity) ((EventRecievePacket) event).getPacket()).motionX *= horizontalVelocity.getVal();
                        ((S12PacketEntityVelocity) ((EventRecievePacket) event).getPacket()).motionZ *= horizontalVelocity.getVal();
                    }
                }
                if(((EventRecievePacket) event).getPacket() instanceof S27PacketExplosion) {
                    if(verticalVelocity.getVal() == 0 && horizontalVelocity.getVal() == 0) {
                        ((S27PacketExplosion) ((EventRecievePacket) event).getPacket()).strength *= 0;
                    }
                    else {
                        ((S27PacketExplosion) ((EventRecievePacket) event).getPacket()).strength *= (horizontalVelocity.getVal() + verticalVelocity.getVal() / 2);
                    }
                }
                event.setCancelled(true);
            }
        }
    }
}
