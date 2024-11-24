package space.heliodor.module.impl.combat;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumChatFormatting;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventUpdate;
import space.heliodor.module.Category;
import space.heliodor.module.Module;

public class AntiBot extends Module {
    public AntiBot() {
        super("AntiBot",0, Category.COMBAT);
    }

    @OnEvent
    public void onEvent(Event event) {
        this.setDisplayName("AntiBot " + EnumChatFormatting.GRAY.toString() + "Watchdog");
        if(event instanceof EventUpdate) {
            for(Entity e : mc.theWorld.loadedEntityList) {
                // && e.getName().contains(EnumChatFormatting.RED.toString())
                if(e.isInvisible() && e != mc.thePlayer) {
                    mc.theWorld.removeEntity(e);
                }
            }
        }
    }
}
