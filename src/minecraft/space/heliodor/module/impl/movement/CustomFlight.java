package space.heliodor.module.impl.movement;

import com.sun.org.apache.xpath.internal.operations.Mod;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventJump;
import space.heliodor.event.impl.EventSendPacket;
import space.heliodor.event.impl.EventUpdate;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.settings.OptionBool;
import space.heliodor.settings.OptionMode;
import space.heliodor.settings.OptionNumber;
import space.heliodor.utils.movement.Utils;

import java.util.ArrayList;

public class CustomFlight extends Module {
    private static final OptionNumber speed = new OptionNumber("Speed", 2f, 1f, 6f, 0.1f);
    private static final OptionBool blink = new OptionBool("Blink", false);
    private static final OptionNumber customY = new OptionNumber("Motion Y", 0, 0, 1, 0.1);
    private static final OptionMode customMotionMode = new OptionMode("Motion Mode", "Positive", "Positive", "Negative");
    private static final OptionNumber customTimer = new OptionNumber("Timer", 1, 0.1, 2, 0.1);
    private static final OptionBool customGround = new OptionBool("Ground", true);
    private static final OptionBool customVertical = new OptionBool("Vertical", true);
    private static final ArrayList<Packet> packets = new ArrayList<>();
    public CustomFlight() {
        super("CustomFlight", 0, Category.MOVEMENT);
        this.options.add(speed);
        this.options.add(customY);
        this.options.add(customTimer);
        this.options.add(customGround);
        this.options.add(customVertical);
        this.options.add(customMotionMode);
        this.options.add(blink);
    }

    @OnEvent
    public void onEvent(Event event) {
        this.setDisplayName("Custom Flight");
        if(event instanceof EventUpdate) {
            switch (customMotionMode.name()) {
                case "Positive": {
                    mc.thePlayer.motionY = customY.getVal();
                    break;
                }
                case "Negative": {
                    mc.thePlayer.motionY = -customY.getVal();
                    break;
                }
            }
            mc.thePlayer.onGround = customGround.isEnabled();
            mc.timer.timerSpeed = (float) customTimer.getVal();
            if(customVertical.isEnabled()) {
                if(mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.thePlayer.motionY += speed.getVal() / 2;
                }
                else if(mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.thePlayer.motionY -= speed.getVal() / 2;
                }
            }
            Utils.setSpeed(speed.getVal());
        }
        if(event instanceof EventJump) {
            event.setCancelled(true);
        }
        if(event instanceof EventSendPacket) {
            if(blink.isEnabled()) {
                if(((EventSendPacket) event).getPacket() instanceof C03PacketPlayer) {
                    packets.add(((EventSendPacket) event).getPacket());
                    event.setCancelled(true);
                }
            }
        }
    }

    @Override
    public void onEnable() {
        packets.clear();
        mc.timer.timerSpeed = 1f;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
        for(Packet packet : packets) {
            mc.thePlayer.sendQueue.sendQueueBypass(packet);
        }
        packets.clear();
    }
}
