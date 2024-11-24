package space.heliodor.module.impl.player;

import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventPreMotionUpdate;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.settings.OptionNumber;

public class GameSpeed extends Module {
    public static final OptionNumber speed = new OptionNumber("Speed",1f,0.1f, 2f, 0.1f);
    public GameSpeed() {
        super("GameSpeed",0, Category.PLAYER);
        this.options.add(speed);
    }

    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof EventPreMotionUpdate) {
            mc.timer.timerSpeed = (float) speed.getVal();
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
    }
}
