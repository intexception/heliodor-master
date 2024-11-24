package space.heliodor.module.impl.render;

import org.lwjgl.input.Keyboard;
import space.heliodor.Heliodor;
import space.heliodor.event.OnEvent;
import space.heliodor.module.Category;
import space.heliodor.module.Module;

public class ClickGUI extends Module {
    public ClickGUI() {
        super("ClickGUI", Keyboard.KEY_RSHIFT, Category.RENDER);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Heliodor.INSTANCE().clickGui);
        this.toggle();
    }
}
