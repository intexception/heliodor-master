package space.heliodor.module.impl.player;

import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventUpdate;
import space.heliodor.module.Category;
import space.heliodor.module.Module;

public class Step extends Module {
    public Step() {
        super("Step", 0, Category.PLAYER);
    }
    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            mc.thePlayer.stepHeight = 1f;
        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.stepHeight = 0.6f;
    }
}

