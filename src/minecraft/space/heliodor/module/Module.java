package space.heliodor.module;

import net.minecraft.client.Minecraft;
import space.heliodor.event.Event;
import space.heliodor.settings.Option;
import space.heliodor.utils.Slider;

import java.util.ArrayList;
import java.util.List;

public class Module {
    public String name;
    public String displayName;
    public boolean isToggled;
    public int key;
    public float x;
    public float y;
    public float animationX;
    public float animationY;
    public boolean extended;
    public boolean inProcess;
    public Category category;
    public Minecraft mc = Minecraft.getMinecraft();
    public List<Option> options = new ArrayList<>();

    public Module(String name, int key, Category category) {
        this.name = name;
        this.key = key;
        this.category = category;
    }
    public void onEvent(Event event) {}

    public void toggle() {
        isToggled = !isToggled;
        if(isToggled) onEnable();
        else onDisable();
    }
    public boolean isToggled() {
        return this.isToggled;
    }

    public int getKey() {
        return this.key;
    }

    public String getDisplayName() {
        return displayName != null ? displayName : name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public void set(boolean x, boolean y, float val) {
        if(x) {
            this.x = val;
        }
        if(y) {
            this.y = val;
        }
    }

    public float get(boolean x, boolean y) {
        if(x) return this.x;
        if(y) return this.y;
        return 0;
    }
}
