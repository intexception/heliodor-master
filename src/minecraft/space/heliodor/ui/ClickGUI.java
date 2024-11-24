package space.heliodor.ui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import space.heliodor.Heliodor;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.settings.OptionBool;
import space.heliodor.settings.OptionMode;
import space.heliodor.settings.OptionNumber;
import space.heliodor.settings.Option;
import space.heliodor.utils.Stopwatch;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClickGUI extends GuiScreen {
    public ClickGUI() {

    }

    int widthAnimation = width / 2 + 140;
    float scale;
    @Override
    public void initGui() {
        super.initGui();
        scale = 0;
        widthAnimation = width / 2 + 140;
        currentCategory = "C";
        categories.add("C");
        categories.add("M");
        categories.add("R");
        categories.add("P");
    }

    Module currentModule;
    String currentModuleName;
    Option currentOption;
    boolean showSettings = false;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glPushMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glTranslated(width / 2, height / 2, width / 2);
        for(int offset = 15; offset > 1; offset--) {
            Gui.drawRect(-175 - offset * 0.05f, -125 - offset * 0.05f, 175 + offset * 0.05f, 125 + offset * 0.05f, new Color(0, 0, 0, offset * 2).getRGB());
        }
        Gui.drawRect(-175, -125, 175, 125, new Color(35,35,35,255).getRGB());
        for(int offset = 20; offset > 1; offset--) {
            Gui.drawRect(-175, -125, 175, (float) (-110 + offset * 0.3), new Color(0, 0, 0, offset * 2).getRGB());
        }

        Gui.drawRect(-175, -125, 175, -105, new Color(25,25,25).getRGB());
        currentCategory = categories.get(index);
        if(currentCategory != null) {
            switch (currentCategory) {
                case "C": {
                    Heliodor.INSTANCE().icons.drawStringWithShadow("A", 0, -119, -1);
                    break;
                }
                case "M": {
                    Heliodor.INSTANCE().icons.drawStringWithShadow("J", 0, -119, -1);
                    break;
                }
                case "R": {
                    Heliodor.INSTANCE().icons.drawStringWithShadow("C", 0, -119, -1);
                    break;
                }
                case "P": {
                    Heliodor.INSTANCE().icons.drawStringWithShadow("F", 0, -119, -1);
                    break;
                }
            }
        }
        else {
            Heliodor.INSTANCE().font1.drawCenteredString("Unknown error! Contact owner for help", 0,0, -1);
        }
        if(currentCategory == "C") current = Category.COMBAT;
        if(currentCategory == "M") current = Category.MOVEMENT;
        if(currentCategory == "R") current = Category.RENDER;
        if(currentCategory == "P") current = Category.PLAYER;
        if(showSettings) {
            float yPos = -90;
            for(Option option : currentModule.options) {
                yPos += 10;
                option.y = height / 2 + yPos;
                option.x = width / 2 - 30;
                currentOption = option;

                if (currentOption instanceof OptionNumber) {
                    OptionNumber optionNumber = (OptionNumber) currentOption;
                    DecimalFormat decimalFormat = new DecimalFormat("#.#");
                    String value = decimalFormat.format(optionNumber.value);
                    Heliodor.INSTANCE().font1.drawString(optionNumber.name + " - " + value, 0 - Heliodor.INSTANCE().font1.getWidth(optionNumber.name + " - " + value) / 2 + 1.6f, yPos, -1);
                }
                if (currentOption instanceof OptionMode) {
                    OptionMode optionMode = (OptionMode) currentOption;
                    Heliodor.INSTANCE().font1.drawString(optionMode.name + " - " + optionMode.name(),0 - Heliodor.INSTANCE().font1.getWidth(optionMode.name + " - " + optionMode.name()) / 2 + 1.6f, yPos, -1);
                }
                if (currentOption instanceof OptionBool) {
                    OptionBool modeSetting = (OptionBool) currentOption;
                    Heliodor.INSTANCE().font1.drawString(modeSetting.name + " - " + (modeSetting.isEnabled ? "Enabled" : "Disabled"), 0 - Heliodor.INSTANCE().font1.getWidth(modeSetting.name + " - " + (modeSetting.isEnabled ? "Enabled" : "Disabled")) / 2 + 1.6f, yPos, -1);
                }
            }
        }
        float x = 0;
        float y = -90;
        if(!Heliodor.INSTANCE().moduleMgr.getModulesByCategory(current).isEmpty()) {
            for (Module module : Heliodor.INSTANCE().moduleMgr.getModulesByCategory(current)) {
                if (!showSettings) {
                    x = 0 - Heliodor.INSTANCE().font1.getWidth(module.name) / 2 + 1.6f;
                    if(!module.isToggled()) {
                        Gui.drawRect(0,0,0,0,0);
                        Heliodor.INSTANCE().font1.drawStringWithShadow(module.name , x, y, new Color(143, 143, 144,255).getRGB());
                    }
                    else {
                        Gui.drawRect(0,0,0,0,0);

                        Heliodor.INSTANCE().font1.drawString(module.name, x, y, -1);
                    }
                    currentModuleName = module.name;
                    module.x = width / 2 + x;
                    module.y = height / 2 + y;
                    y += 17.5f;
                }
            }
        }
        Heliodor.INSTANCE().font1.drawStringWithShadow("<", -170, -119, -1);
        Heliodor.INSTANCE().font1.drawStringWithShadow(">", 165, -119, -1);
        GL11.glPopMatrix();
    }

    String currentCategory;
    Category current;
    Stopwatch delay = new Stopwatch();
    public ArrayList<String> categories = new ArrayList<>();
    private int index;
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == 0) {
            if(mouseY > height / 2 - 119 && mouseY < height / 2 - 110) {
                if(mouseX > width / 2 - 170 && mouseX < width / 2 - 160) {
                    showSettings = false;
                    if(index != 0) {
                        if(index < categories.size() - 1) {
                            index--;
                        }
                        else {
                            index = 0;
                        }
                    }
                    else {
                        index = 0;
                    }
                }
                if(mouseX < width / 2 + 170 && mouseX > width / 2 + 160) {
                    showSettings = false;
                    if(index < categories.size() - 1) {
                        index++;
                    }
                    else {
                        index = 0;
                    }
                }
            }
        }
        if(current != null && !showSettings) {
            for(Module module : Heliodor.INSTANCE().moduleMgr.getModulesByCategory(current)) {
                if(mouseX > module.get(true, false) && mouseX < module.get(true, false) +  Heliodor.INSTANCE().font1.getWidth(module.name)) {
                    if(mouseY > module.get(false, true) && mouseY < module.get(false, true) +  Heliodor.INSTANCE().font1.getHeight(module.name)) {
                        if(mouseButton == 1) {
                            currentModule = module;
                            widthAnimation = width / 2 + 140;
                            delay.reset();
                            showSettings = true;
                        }
                        if(mouseButton == 0) {
                            module.toggle();
                        }
                    }
                }
            }
        }

        if(showSettings && !currentModule.options.isEmpty()) {
            System.out.println("DOG!!!");
            for(Option currento : currentModule.options) {
                System.out.println("DOG!!!");
                if (mouseX > currento.x && mouseX < currento.x +  Heliodor.INSTANCE().font1.getWidth(currento.name + ": " + "")) {
                    System.out.println("DOG!!!111111");
                    if (mouseY > currento.y & mouseY < currento.y +  Heliodor.INSTANCE().font1.getHeight(currento.name + ": " + "") && delay.elapsed(100)) {
                        System.out.println("DOG!!!111");
                        if (currento instanceof OptionBool) {
                            OptionBool modeSetting = (OptionBool) currento;
                            modeSetting.toggle();
                        }
                        if (currento instanceof OptionMode && mouseButton == 0) {
                            OptionMode optionMode = (OptionMode) currento;
                            optionMode.changeSetting();
                        }
                        if (currento instanceof OptionNumber) {
                            OptionNumber modeSetting = (OptionNumber) currento;
                            if(mouseButton == 1) {
                                modeSetting.value = modeSetting.value > modeSetting.minimum ? modeSetting.value - (1) * modeSetting.getAdd() : modeSetting.value;
                            }
                            if(mouseButton == 0) {
                                modeSetting.value = modeSetting.value < modeSetting.maximum ? modeSetting.value + (1) * modeSetting.getAdd() : modeSetting.value;
                            }
                        }
                    }
                }
            }
        }
    }
    public static void drawRoundedRect(int xCoord, int yCoord, int xSize, int ySize, int colour, String text) {
        int width = xCoord + xSize;
        int height = yCoord + ySize;

        // Top rounding
        Gui.drawRect(xCoord + 1, yCoord, width - 1, height, colour);

        // Middle rect
        Gui.drawRect(xCoord, yCoord + 1, width, height - 1, colour);
    }
}
