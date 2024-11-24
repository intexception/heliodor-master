package space.heliodor.login;

import java.awt.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import space.heliodor.ui.MainMenu;
import space.heliodor.utils.HWIDHelper;
import space.heliodor.utils.ProtectionHelper;

public final class GuiClientLogin extends GuiScreen {
    private PasswordField password;
    private AltLoginThread thread;
    private GuiTextField username;

    public GuiClientLogin() {

    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                try {
                    byte[] hwid = HWIDHelper.generateHWID();
                    ProtectionHelper layer = new ProtectionHelper(HWIDHelper.bytesToHex(hwid));
                    if(username.getText().equalsIgnoreCase(layer.getUid() + "")) {
                        if(!layer.canLogin()) {
                            System.exit(55);
                        }
                        else {
                            mc.displayGuiScreen(new MainMenu());
                        }
                    }
                    else {
                        System.exit(55);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void drawScreen(int x2, int y2, float z2) {
        this.drawRect(0,0,1920,1080, new Color(36, 36, 36, 255).getRGB());
        this.username.drawTextBox();
        this.drawString(this.fontRendererObj, "Heliodor Protection", width / 2 - this.fontRendererObj.getStringWidth("Heliodor Protection") / 2, width / 2, -7829368);
        if (this.username.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "UID", width / 2 - this.fontRendererObj.getStringWidth("UID") / 2, height / 2 - 94, -7829368);
        }
        super.drawScreen(x2, y2, z2);
    }

    @Override
    public void initGui() {
        int var3 = height / 4 + 24;
        this.buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, "Login"));
        this.username = new GuiTextField(var3, this.mc.fontRendererObj, width / 2 - 100, height / 2 - 100, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\r') {
            mc.displayGuiScreen(new GuiClientLogin());
        }
        this.username.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x2, int y2, int button) {
        try {
            super.mouseClicked(x2, y2, button);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(x2, y2, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
    }
}

