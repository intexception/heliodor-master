package space.heliodor.module.impl.movement;

import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventUpdate;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.utils.movement.Utils;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint",0, Category.MOVEMENT);
    }

    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            if(mc.thePlayer.moveForward > 0) {
                mc.thePlayer.setSprinting(true);
            }
        }
    }
}
