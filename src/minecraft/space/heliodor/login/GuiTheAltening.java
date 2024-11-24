package space.heliodor.login;

import java.io.IOException;
import java.util.Objects;

import com.thealtening.api.TheAltening;
import com.thealtening.api.response.Account;
import com.thealtening.api.retriever.BasicDataRetriever;
import com.thealtening.auth.TheAlteningAuthentication;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;


public final class GuiTheAltening extends GuiScreen {
    private PasswordField password;
    private final GuiScreen previousScreen;
    private AltLoginThread thread;

    public GuiTheAltening(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.previousScreen);
                break;
            }
            case 0: {
                if (password.getText().isEmpty()) return;
                TheAltening theAltening = new TheAltening();
                BasicDataRetriever data = theAltening.newBasicRetriever(password.getText());
                Account account = data.getAccount();
                this.thread = new AltLoginThread(account.getToken(), "heliodor", TheAlteningAuthentication.theAltening());
                this.thread.start();
            }
        }
    }

    @Override
    public void drawScreen(int x2, int y2, float z2) {
        this.drawDefaultBackground();
        this.password.drawTextBox();
        this.drawCenteredString(this.mc.fontRendererObj, this.thread == null ? (Object)((Object)EnumChatFormatting.GRAY) + "Idle..." : this.thread.getStatus(), width / 2, 29, -1);
        if (this.password.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "API Key", width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(x2, y2, z2);
    }

    @Override
    public void initGui() {
        int var3 = height / 4 + 24;
        this.buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
        this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
        this.password.setFocused(true);
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
        if (character == '\t') {
            if (!this.password.isFocused()) {
                this.password.setFocused(false);
            } else {
                this.password.setFocused(true);
            }
        }
        if (character == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
        this.password.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x2, int y2, int button) {
        try {
            super.mouseClicked(x2, y2, button);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.password.mouseClicked(x2, y2, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        this.password.updateCursorCounter();
    }
}

