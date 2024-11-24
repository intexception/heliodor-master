package space.heliodor.module.impl.render;

import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.util.EnumChatFormatting;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventPreMotionUpdate;
import space.heliodor.event.impl.EventRecievePacket;
import space.heliodor.event.impl.EventUpdate;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.settings.OptionMode;

public class Atmosphere extends Module {
    public Atmosphere() {
        super("Atmosphere",0, Category.RENDER);
    }

    @OnEvent
    public void onEvent(Event event) {
        this.setDisplayName("Atmosphere " + EnumChatFormatting.GRAY.toString() + "Night");
    }
}
