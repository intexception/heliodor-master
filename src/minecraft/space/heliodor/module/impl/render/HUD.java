package space.heliodor.module.impl.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import space.heliodor.Heliodor;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.Event2D;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.settings.Option;
import space.heliodor.settings.OptionBool;
import space.heliodor.settings.OptionMode;
import space.heliodor.settings.OptionNumber;
import space.heliodor.utils.Stopwatch;
import space.heliodor.utils.movement.Utils;

import java.awt.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

public class HUD extends Module {
    private static final OptionMode mode = new OptionMode("HUD Color", "Pulse", "Pulse", "Color");
    private static final OptionMode font = new OptionMode("Font", "SF UI", "SF UI", "Google");
    private static final OptionMode watermark = new OptionMode("Watermark Mode", "Default", "Default", "Rect", "None");
    private static final OptionBool background = new OptionBool("Background" ,false);
    private static final OptionBool sidebar = new OptionBool("Sidebar" ,false);
    private static final OptionBool outline = new OptionBool("Outline" ,false);
    private static final OptionBool firstLetter = new OptionBool("First Letter", true);
    private static final OptionNumber red = new OptionNumber("Red",255,0,255,5);
    private static final OptionNumber green = new OptionNumber("Green",255,0,255,5);
    private static final OptionNumber blue = new OptionNumber("Blue",255,0,255,5);
    private static final OptionNumber opacity = new OptionNumber("Opacity",255,0,255,1);
    private static final int firstColor = new Color(48, 48, 48,255).getRGB();
    private static final int secondColor = new Color(43, 43, 43,255).getRGB();
    private static final int thirdColor = new Color(17, 17, 17,255).getRGB();
    private double animY = 1;
    private int colorInstance;
    public HUD() {
        super("HUD", 0, Category.RENDER);
        this.options.add(mode);
        this.options.add(watermark);
        this.options.add(font);
        this.options.add(background);
        this.options.add(sidebar);
        this.options.add(outline);
        this.options.add(firstLetter);
        this.options.add(red);
        this.options.add(green);
        this.options.add(blue);
        this.options.add(opacity);
    }
    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof Event2D) {
            GL11.glPushMatrix();
            int arrayColor1 = new Color((int)red.getVal(), (int)green.getVal(), (int)blue.getVal(),255).getRGB();
            int opacityBackground = (int) Math.round(opacity.getVal());
            Color backgroundColor = new Color(35, 35, 35, opacityBackground);
            ScaledResolution sr = new ScaledResolution(mc);
            if(font.name().equalsIgnoreCase("SF UI")) {
                Heliodor.INSTANCE().moduleMgr.getModules().sort(Comparator.comparingDouble(m -> (double) Heliodor.INSTANCE().font1.getWidth(((Module)m).getDisplayName())).reversed());
            }
            else {
                Heliodor.INSTANCE().moduleMgr.getModules().sort(Comparator.comparingDouble(m -> (double) Heliodor.INSTANCE().google.getWidth(((Module)m).getDisplayName())).reversed());
            }
            if (watermark.name().equalsIgnoreCase("Default")) {
                if(firstLetter.isEnabled()) {
                    Heliodor.INSTANCE().font1.drawStringWithShadow(Heliodor.INSTANCE().customname.charAt(0) + EnumChatFormatting.WHITE.toString() + Heliodor.INSTANCE().customname.replaceFirst(String.valueOf(Heliodor.INSTANCE().customname.charAt(0)), ""), 2, 4, colorInstance);
                }
                else {
                    Heliodor.INSTANCE().font1.drawStringWithShadow(EnumChatFormatting.WHITE.toString() + Heliodor.INSTANCE().customname.charAt(0) + EnumChatFormatting.WHITE.toString() + Heliodor.INSTANCE().customname.replaceFirst(String.valueOf(Heliodor.INSTANCE().customname.charAt(0)), ""), 2, 4, new Color(255, 140,0,255).getRGB());
                }
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                String bps = decimalFormat.format(Utils.getSpeed() * 20);
                Heliodor.INSTANCE().font1.drawCenteredString("BPS:" + bps, sr.getScaledWidth() - Heliodor.INSTANCE().font1.getWidth("BPS:" + bps) /2 , sr.getScaledHeight()- 10, -1);
            }
            if(watermark.name().equalsIgnoreCase("Rect")) {
                Gui.drawRect(60 - 41.5f, 75 - 61.5f, 100 + 11.5f - 25, -35 + 61.5f, firstColor);
                Gui.drawRect(60 - 41f, 75 - 61f, 100 + 11f  - 25, -35 + 61f, secondColor);
                Gui.drawRect(60 - 40.5f, 75 - 60.5f, 100 + 10.5f  - 25, -35 + 60.5f, firstColor);
                Gui.drawRect(60 - 40, 75 - 60, 100 + 10  - 25, -35 + 60, thirdColor);
                String rectText = EnumChatFormatting.GREEN.toString() + "H" + EnumChatFormatting.WHITE.toString() + "eliodor.space";
                Heliodor.INSTANCE().font1.drawString(rectText, 20, 15, -1);
            }
            int count = 0;
            for(Module m : Heliodor.INSTANCE().moduleMgr.getModules()) {
                int arrayListColor = -1;
                animY += ((count - animY) / 32) * 0.5;
                if(mode.name().equalsIgnoreCase("Pulse")) {
                    arrayListColor = fadeBetween(arrayColor1, darker(arrayColor1, 0.5f), ((System.currentTimeMillis() + (count * 100)) % 1000 / (1000 / 2.0F)));
                    colorInstance = fadeBetween(arrayColor1, darker(arrayColor1, 0.5f), ((System.currentTimeMillis() + (count * 100)) % 1000 / (1000 / 2.0F)));
                }
                if(mode.name().equalsIgnoreCase("Color")) {
                    arrayListColor = new Color((int)red.getVal(), (int)green.getVal(), (int)blue.getVal(), 255).getRGB();
                    colorInstance = new Color((int)red.getVal(), (int)green.getVal(), (int)blue.getVal(), 255).getRGB();
                }
                if(!m.isToggled() || m.name.equalsIgnoreCase("HUD")) {
                    if(m.animationX < sr.getScaledWidth() + 10){
                        m.animationX += 2;
                        m.inProcess = true;
                    }
                    else {
                        m.animationX = sr.getScaledWidth() + 11;
                        m.inProcess = false;
                    }
                }
                if(font.name().equalsIgnoreCase("SF UI")) {
                    if(m.animationX >= sr.getScaledWidth() -  Heliodor.INSTANCE().font1.getWidth(m.getDisplayName()) + 0.5f) {
                        m.animationX -= m.animationX / 1520;
                    }
                }
                else {
                    if(m.animationX >= sr.getScaledWidth() -  Heliodor.INSTANCE().google.getWidth(m.getDisplayName()) + 0.5f) {
                        m.animationX -= m.animationX / 1520;
                    }
                }
                if(background.isEnabled()) {
                    if(font.name().equalsIgnoreCase("SF UI")) {
                        Gui.drawRect(m.animationX - 1,  Heliodor.INSTANCE().font1.getHeight(m.getDisplayName()) + count + 2,1920, count, backgroundColor.getRGB());
                    }
                    else {
                        Gui.drawRect(m.animationX - 1,  Heliodor.INSTANCE().google.getHeight(m.getDisplayName()) + count + 2,1920, count, backgroundColor.getRGB());
                    }
                }
                if(sidebar.isEnabled()) {
                    Gui.drawRect(sr.getScaledWidth() - 1, 0, sr.getScaledWidth(), count,arrayListColor);
                }

                if(outline.isEnabled()) {
                    Gui.drawRect(m.animationX - 2f, Heliodor.INSTANCE().font1.getHeight(m.getDisplayName()) + count + 2, m.animationX - 2f + 1f, count, arrayListColor);

                }
                Gui.drawRect(0,0,0,0, 0);
                if(font.name().equalsIgnoreCase("SF UI")) {
                    Heliodor.INSTANCE().font1.drawStringWithShadow(m.getDisplayName(), m.animationX, count + 1, arrayListColor);
                }
                else {
                    Heliodor.INSTANCE().google.drawStringWithShadow(m.getDisplayName(), m.animationX, count + 1, arrayListColor);
                }
                if(m.animationX < sr.getScaledWidth()) {
                    count += 11 - m.animationY;
                }

                m.animationY += 0.3;
                if(m.isToggled()) {
                    m.animationY = 0;
                }
            }
            GL11.glPopMatrix();
        }
    }
    @Override
    public void onEnable() {
        ScaledResolution sr = new ScaledResolution(mc);
        for(Module m :  Heliodor.INSTANCE().moduleMgr.getModules()) {
            m.animationX = sr.getScaledWidth() + 11;
        }
    }

    /*
        Credit: Autumn Client
     */
    public int fadeBetween(int color1, int color2, float offset) {
        if (offset > 1)
            offset = 1 - offset % 1;

        double invert = 1 - offset;
        int r = (int) ((color1 >> 16 & 0xFF) * invert +
                (color2 >> 16 & 0xFF) * offset);
        int g = (int) ((color1 >> 8 & 0xFF) * invert +
                (color2 >> 8 & 0xFF) * offset);
        int b = (int) ((color1 & 0xFF) * invert +
                (color2 & 0xFF) * offset);
        int a = (int) ((color1 >> 24 & 0xFF) * invert +
                (color2 >> 24 & 0xFF) * offset);
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF);
    }

    private int darker(int color, float factor) {
        int r = (int) ((color >> 16 & 0xFF) * factor);
        int g = (int) ((color >> 8 & 0xFF) * factor);
        int b = (int) ((color & 0xFF) * factor);
        int a = color >> 24 & 0xFF;

        return ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF) |
                ((a & 0xFF) << 24);
    }
}
