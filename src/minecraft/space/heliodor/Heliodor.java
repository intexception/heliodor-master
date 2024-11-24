package space.heliodor;

import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import space.heliodor.event.Event;
import space.heliodor.module.ModuleManager;
import space.heliodor.module.configs.ConfigSystem;
import space.heliodor.ui.ClickGUI;
import space.heliodor.ui.Dropdown;
import space.heliodor.utils.HWIDHelper;
import space.heliodor.utils.ProtectionHelper;
import space.heliodor.utils.font.TTFFontRenderer;


import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Heliodor {
    public static String name = "Heliodor", customname = "Heliodor", version = "", author = "Heliar";
    public static TTFFontRenderer font1;
    public static TTFFontRenderer font2;
    public static TTFFontRenderer google;
    public static TTFFontRenderer fontSmall1;
    public static TTFFontRenderer icons;
    public static TTFFontRenderer verdana;
    public static ModuleManager moduleMgr;
    public static Dropdown clickGui;
    public static ConfigSystem configSystem;

    public void onInitialize() {
        System.out.println("Initializing Heliodor...");
        try {
            font1 = new TTFFontRenderer(Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("user.dir") + "\\versions\\Heliodor\\heliodor_assets\\font.ttf")).deriveFont(Font.TRUETYPE_FONT, 9.0f));
            google = new TTFFontRenderer(Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("user.dir") + "\\versions\\Heliodor\\heliodor_assets\\fontCustom.ttf")).deriveFont(Font.TRUETYPE_FONT, 18.0f));
            fontSmall1 = new TTFFontRenderer(Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("user.dir") + "\\versions\\Heliodor\\heliodor_assets\\font.ttf")).deriveFont(Font.TRUETYPE_FONT, 7.0f));
            font2 = new TTFFontRenderer(Font.createFont(Font.TRUETYPE_FONT,new File(System.getProperty("user.dir") + "\\versions\\Heliodor\\heliodor_assets\\font2.ttf")).deriveFont(Font.TRUETYPE_FONT, 18.5f));
            icons = new TTFFontRenderer(Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("user.dir") + "\\versions\\Heliodor\\heliodor_assets\\icons.ttf")).deriveFont(Font.TRUETYPE_FONT, 18.0f));
            verdana = new TTFFontRenderer(Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("user.dir") + "\\versions\\Heliodor\\heliodor_assets\\Verdana.ttf")).deriveFont(Font.TRUETYPE_FONT, 9.0f));
        } catch (FontFormatException e) {
            e.printStackTrace();
            System.out.println("There was a problem with font renderer. Contact with client owner on discord for help.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was a problem with font renderer. Contact with client owner on discord for help.");
        }
        moduleMgr = new ModuleManager();
        byte[] hwid = HWIDHelper.generateHWID();
        ProtectionHelper layer = new ProtectionHelper(HWIDHelper.bytesToHex(hwid));
        if(!layer.canLogin()) {
            System.exit(55);
        }
        clickGui = new Dropdown();
        configSystem = new ConfigSystem();

        Display.setTitle("Heliodor");
    }

    public static Heliodor INSTANCE() {
        return new Heliodor();
    }
    public static boolean canPass;
    public void onEvent(Event event) {
        moduleMgr.getModules().forEach(module -> {
            canPass = module.isToggled ? true : false;
            if(canPass)module.onEvent(event);
        });
    }
}
