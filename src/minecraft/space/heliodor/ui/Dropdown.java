package space.heliodor.ui;

import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import space.heliodor.Heliodor;
import space.heliodor.module.Module;
import space.heliodor.settings.Option;
import space.heliodor.settings.OptionBool;
import space.heliodor.settings.OptionMode;
import space.heliodor.settings.OptionNumber;
import space.heliodor.utils.Slider;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

public class Dropdown extends GuiScreen {
    private static CopyOnWriteArrayList<Category> categories = new CopyOnWriteArrayList<>();
    int mouseX;
    int mouseY;
    public Dropdown() {
        double offset = 0;
        for(space.heliodor.module.Category category : space.heliodor.module.Category.values()) {
            Color colorCategory = null;
            switch(category) {
                case COMBAT: {
                    colorCategory = new Color(255,0,0,255);
                    break;
                }
                case MOVEMENT: {
                    colorCategory = new Color(0,120,255,255);
                    break;
                }
                case PLAYER: {
                    colorCategory = new Color(187, 150, 12,255);
                    break;
                }
                case RENDER: {
                    colorCategory = new Color(50,255,0,255);
                    break;
                }
            }
            Category category1 = new Category(120 + offset, 20, colorCategory, category);
            categories.add(category1);
            offset += 125;
        }
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        for(Category category : categories) {
            category.drawCategory(mouseX, mouseY);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Category category : categories) {
            category.mouse(mouseButton, mouseX, mouseY);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for(Category category : categories) {
            category.released();
        }
    }

    public class Category {
        // x axis positions
        private float xPos;
        private double finalX;
        private double xDrag;
        private double width;

        // y axis positions
        private double yPos;
        private double finalY;
        private double yDrag;
        private double height;

        //slider
        private boolean sliderDragged;
        //category
        private boolean isDragging;
        private space.heliodor.module.Category category;
        private Color color;
        private boolean hide;

        public Category(double width, double height, Color color, space.heliodor.module.Category category) {
            this.width = width;
            this.height = height;
            this.color = color;
            this.category = category;
        }

        public void drawCategory(int mouseX, int mouseY) {
            double count = 0;
            double count1 = 0;
            if(this.isDragging) {
                xPos = (float) (mouseX - xDrag);
                yPos = mouseY - yDrag;
            }
            finalX = xPos;
            finalY = yPos;
            CopyOnWriteArrayList<Module> modules = Heliodor.INSTANCE().moduleMgr.getModules();
            modules.sort(Comparator.comparingDouble(m -> Heliodor.INSTANCE().google.getWidth(((Module)m).getDisplayName().toUpperCase())).reversed());
            for(Module module : modules) {
                if (module.category == category) {
                    count += 15;
                }
            }
            if(!this.hide) {
                mc.currentScreen.drawGradientRect((int) ((int)finalX + width - 50), (int)(finalY + height + 20), (int)(finalX + width + 50), (int) ((int)finalY + height + count + 25), new Color(37,37,37,255).getRGB(), new Color(37,37,37,255).getRGB());
                for(Module module : modules) {
                    if(module.category == category) {
                        Heliodor.INSTANCE().google.drawCenteredString(module.name, (float)(finalX + width), (float) ((float) finalY + 45 + count1), module.isToggled ? new Color(100,100,100,255).getRGB() : -1);
                        module.x = (float) (finalX + width);
                        module.y = (float) (finalY + 45 + count1);
                        count1 += 15;
                    }
                    if(module.category == category && module.extended && module.options.size() > 0) {
                        double countset = 0;
                        double countrect = 0;
                        for(Option option : module.options) {
                            countrect += 15;
                        }
                        Gui.drawRect(module.x + 50, module.y - 5, module.x + 170, (float) (module.y + countrect), new Color(37,37,37,255).getRGB());
                        for(Option option : module.options) {
                            Gui.drawRect(0,0,0,0,0);
                            if (option instanceof OptionNumber) {
                                option.x = module.x + 60;
                                option.y = (float) (module.y + countset + 12);
                                OptionNumber optionNumber = (OptionNumber) option;
                                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                                String value = decimalFormat.format(optionNumber.value);
                                Heliodor.INSTANCE().google.drawStringWithShadow(optionNumber.name + " - " + value, module.x + 60, (float) (module.y + countset - 3), -1);
                                option.slider = new Slider((int)optionNumber.getMinimum(), (int)optionNumber.getMaximum(), (int)module.x + 60, (int) (module.y + countset + 12 - 3), 100, optionNumber);
                                option.slider.draw(mouseX, mouseY);
                            }
                            if (option instanceof OptionMode) {
                                option.x = module.x + 60;
                                option.y = (float) (module.y + countset);
                                OptionMode optionMode = (OptionMode) option;
                                Heliodor.INSTANCE().google.drawStringWithShadow(optionMode.name + " - " + optionMode.name(),module.x + 60, (float) (module.y + countset), -1);
                            }
                            if (option instanceof OptionBool) {
                                option.x = module.x + 60;
                                option.y = (float) (module.y + countset);
                                OptionBool modeSetting = (OptionBool) option;
                                Heliodor.INSTANCE().google.drawStringWithShadow(modeSetting.name + " - " + (modeSetting.isEnabled ? "Enabled" : "Disabled"), module.x + 60, (float) (module.y + countset), -1);
                            }
                            countset += 15;
                        }
                    }
                }
            }
            Gui.drawRect((int) ((int)finalX + width - 50), (int) ((int)finalY + height - 2), (int) ((int)finalX + width + 50), (int) ((int)finalY + height + 20), new Color(25, 25, 25,255).getRGB());
            Heliodor.INSTANCE().google.drawCenteredString(category.name().toUpperCase().charAt(0) + category.name().replaceFirst(String.valueOf(category.name().charAt(0)), "").toLowerCase(Locale.ROOT), (float) ((float) finalX + width), (float) ((float) finalY + height + 4), -1);
            Gui.drawRect(0,0,0,0,0);
        }
        public void mouse(int button, int mouseX, int mouseY) {
            boolean isHolding = mouseX >= finalX + width - 50 && mouseX < finalX +width + 50 && mouseY >= finalY + height - 2 && mouseY < finalY + height + 20;
            int dog = (int) (finalX + width);
            if(isHolding && button == 0){
                this.isDragging = true;
                xDrag = mouseX - finalX;
                yDrag = mouseY - finalY;
            }
            else if(isHolding && button == 1) {
                hide = !hide;
            }
            if(!hide) {
                for(Module module : Heliodor.INSTANCE().moduleMgr.getModulesByCategory(category)) {
                    if(mouseX > module.get(true, false) - Heliodor.INSTANCE().google.getWidth(module.name) && mouseX < module.get(true, false) + Heliodor.INSTANCE().google.getWidth(module.name) - 14) {
                        if(mouseY > module.get(false, true) && mouseY < module.get(false, true) +  Heliodor.INSTANCE().google.getHeight(module.name)) {
                            if(button == 0) {
                                module.toggle();
                            }
                            if(button == 1) {
                                module.extended = !module.extended;
                            }
                        }
                    }
                    if(module.extended) {
                        for(Option currento : module.options) {
                            if (mouseX > currento.x && mouseX < currento.x + 100) {
                                if (mouseY > currento.y - 5  & mouseY < currento.y + 5) {
                                    if (currento instanceof OptionBool) {
                                        OptionBool modeSetting = (OptionBool) currento;
                                        modeSetting.toggle();
                                    }
                                    if (currento instanceof OptionMode && button == 0) {
                                        OptionMode optionMode = (OptionMode) currento;
                                        optionMode.changeSetting();
                                    }
                                    if (currento instanceof OptionNumber) {
                                        OptionNumber modeSetting = (OptionNumber) currento;
                                        sliderDragged = true;
                                        currento.slider.mouseClicked(mouseX,mouseY, button);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        public void released() {
            isDragging = false;
            sliderDragged = false;

            for (Module module : Heliodor.INSTANCE().moduleMgr.getModulesByCategory(category)) {
                for(Option currento : module.options) {
                    if (mouseX > currento.x && mouseX < currento.x + 100) {
                        if (mouseY > currento.y & mouseY < currento.y +  Heliodor.INSTANCE().font1.getHeight(currento.name + ": " + "")) {
                            if(currento instanceof OptionNumber) {
                                currento.slider.mouseReleased();
                            }
                        }
                    }
                }
            }
        }
    }
}
