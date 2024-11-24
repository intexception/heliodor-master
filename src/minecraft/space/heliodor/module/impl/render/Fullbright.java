package space.heliodor.module.impl.render;

import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.module.Category;
import space.heliodor.module.Module;

public class Fullbright extends Module {
    private float defaultBrightness;
    public Fullbright() {
        super("Fullbright",0, Category.RENDER);
    }

    @Override
    public void onEnable() {
        defaultBrightness = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 10;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = defaultBrightness;
    }
}
