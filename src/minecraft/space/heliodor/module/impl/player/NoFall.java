package space.heliodor.module.impl.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventUpdate;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.settings.OptionMode;

public class NoFall extends Module {
    private static final OptionMode mode = new OptionMode("NoFall Mode", "Spoof", "Spoof", "Watchdog");
    public NoFall() {
        super("NoFall",0, Category.PLAYER);
        this.options.add(mode);
    }

    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            switch(mode.name()) {
                case "Spoof": {
                    this.setDisplayName("No Fall " + EnumChatFormatting.GRAY.toString() + "Spoof");
                    if (mc.thePlayer.fallDistance > 2.5) {
                        if(mc.theWorld.isAirBlock(new BlockPos(0,-1,0))) {
                            mc.thePlayer.sendQueue.sendQueueBypass(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                        }
                    }
                    break;
                }
                case "Watchdog": {
                    this.setDisplayName("No Fall " + EnumChatFormatting.GRAY.toString() + "Watchdog");
                    if(mc.thePlayer.fallDistance > 2.5) {
                        if(mc.theWorld.isAirBlock(new BlockPos(0,-1,0))) {
                            mc.thePlayer.sendQueue.sendQueueBypass(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, -mc.thePlayer.posY, mc.thePlayer.posZ, true));
                        }
                    }
                    break;
                }
            }
        }
    }
}
