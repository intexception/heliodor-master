package space.heliodor.utils;
import javafx.scene.input.MouseButton;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseFilter;
import org.lwjgl.input.Mouse;
import space.heliodor.settings.OptionNumber;

import java.awt.*;

public class Slider {
    int min;
    int max;
    int x;
    int y;
    int width;
    int mouseX;
    int mouseY;
    public boolean dragging = isSliderHovered(mouseX, mouseY) ? Mouse.isButtonDown(0) ? true : false : false;
    OptionNumber option;

    public Slider(int min, int max, int x, int y, int width, OptionNumber option) {
        this.min = min;
        this.max = max;
        this.x = x;
        this.y = y;
        this.width = width;
        this.option = option;
    }

    public void draw(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        double percentBar = (option.getVal() - min)/(max - min);
        Gui.drawRect(x, y - 1.5f, x + width, (float) (y + 1.5), new Color(30,30,30,150).getRGB());
        Gui.drawRect(x, y - 1.5f, (float)(x + (percentBar * width)), y + 1.5f, -1);
        boolean dragging1 = isSliderHovered(mouseX, mouseY) && Mouse.isButtonDown(0);
        if(dragging1) {
            option.value = option.getMinimum() + ((mouseX - ((float)x)) / ((float) width) * (option.getMaximum() - option.getMinimum()));
        }
    }

    public boolean isSliderHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 10;
    }
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        System.out.println(isSliderHovered(mouseX,mouseY));
//        if (mouseButton == 0 && isSliderHovered(mouseX, mouseY)) {
//            option.value = option.getMinimum() + ((mouseX - ((float)x)) / ((float) width) * (option.getMaximum() - option.getMinimum()));
//        }
    }

    public void mouseReleased() {
        this.dragging = false;
    }
}
