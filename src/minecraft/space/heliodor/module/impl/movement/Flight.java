package space.heliodor.module.impl.movement;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import space.heliodor.Heliodor;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.*;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.module.impl.combat.KillAura;
import space.heliodor.settings.OptionBool;
import space.heliodor.settings.OptionMode;
import space.heliodor.settings.OptionNumber;
import space.heliodor.utils.SendMessage;
import space.heliodor.utils.Stopwatch;
import space.heliodor.utils.movement.Utils;

import java.util.ArrayList;
import java.util.Random;

public class Flight extends Module {
    private static final OptionMode mode = new OptionMode("Flight Mode", "Vanilla", "Vanilla", "Glide", "Watchdog Test", "Verus Disabler");
    private static final OptionNumber speed = new OptionNumber("Flight Speed", 2f, 1f, 6f, 0.1f);
    private static final OptionBool blink = new OptionBool("Blink", false);
    ArrayList<Packet> packets = new ArrayList<Packet>();
    private static final Stopwatch timer = new Stopwatch();
    private static final KillAura aura = new KillAura();
    private int stage;
    private float flightSpeed;
    private boolean canFly;
    private boolean hover;
    private boolean flag;
    private ItemStack itemStack;
    private double[] startpos;
    private double startPitch;
    public Flight() {
        super("Flight", Keyboard.KEY_F, Category.MOVEMENT);
        this.options.add(mode);
        this.options.add(speed);
        this.options.add(blink);
    }
    @OnEvent
    public void onEvent(Event event) {
        this.setDisplayName(this.name + " " + EnumChatFormatting.GRAY.toString() + mode.name());
        if(event instanceof EventUpdate) {
            switch (mode.name()) {
                case "Watchdog Test": {
                    if(!timer.elapsed(1000)) {
                        mc.timer.timerSpeed = 1f;
                    }
                    else {
                        mc.timer.timerSpeed = 1f;
                    }
                    if(stage == 0) {
                        for (int i = 0; i < 57; i++) {
                            mc.thePlayer.sendQueue.sendQueueBypass(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.06076984763247362846783276 + Math.random() / 999, mc.thePlayer.posZ, false));
                            mc.thePlayer.sendQueue.sendQueueBypass(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.06076984763247362846783276 / 100 + Math.random() / 99, mc.thePlayer.posZ,false));
                            mc.thePlayer.sendQueue.sendQueueBypass(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.06076984763247362846783276 / 100 + Math.random() / 99, mc.thePlayer.posZ,false));
                        }
                        mc.thePlayer.sendQueue.sendQueueBypass(new C03PacketPlayer(true));
                    }
                    if(stage == 1) {
                        mc.thePlayer.motionY = 0.42;
                        mc.thePlayer.isAirBorne = true;
                        flightSpeed = 0.55f;
                    }
                    if(stage == 2) {
                        flightSpeed *= 2;
                    }
                    else if(stage > 2) {
                        flightSpeed -= flightSpeed / 120;
                    }
                    Utils.setSpeed(flightSpeed);

                    stage++;
                    break;
                }
                case "Vanilla": {
                    if(speed.getVal() > 1.9) {
                        mc.thePlayer.motionY = 0;
                        mc.thePlayer.onGround = true;
                        float rotations[] = aura.getTarget() != null && Heliodor.INSTANCE().moduleMgr.getModuleByName("KillAura").isToggled() ? new KillAura().rotations(aura.getTarget()) : new float[] {0,0} ;
                        if(Heliodor.INSTANCE().moduleMgr.getModuleByName("KillAura").isToggled() && new KillAura().getTarget() != null && Heliodor.INSTANCE().moduleMgr.getModuleByName("TargetStrafe").isToggled()) {
                            if ((double)mc.thePlayer.getDistanceToEntity(aura.getTarget()) <= TargetStrafe.range.getVal()) {
                                Utils.setSpeed(speed.getVal(), rotations[0], 1, 0.0D);
                            } else {
                                Utils.setSpeed(speed.getVal(), rotations[0], 1, 1.0D);
                            }
                        }
                        else {
                            Utils.setSpeed(speed.getVal());
                        }
                        if (mc.gameSettings.keyBindJump.isKeyDown()) {
                            mc.thePlayer.motionY += speed.getVal() / 2;
                        }
                        if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                            mc.thePlayer.motionY -= speed.getVal() / 2;
                        }
                    }
                    else {
                        mc.thePlayer.capabilities.isFlying = true;
                    }
                    break;
                }
                case "Glide": {
                    mc.thePlayer.motionY = -0.03;
                    float rotations[] = aura.getTarget() != null && Heliodor.INSTANCE().moduleMgr.getModuleByName("KillAura").isToggled() ? new KillAura().rotations(aura.getTarget()) : new float[] {0,0} ;
                    if(Heliodor.INSTANCE().moduleMgr.getModuleByName("KillAura").isToggled() && new KillAura().getTarget() != null && Heliodor.INSTANCE().moduleMgr.getModuleByName("TargetStrafe").isToggled()) {
                        if ((double)mc.thePlayer.getDistanceToEntity(aura.getTarget()) <= TargetStrafe.range.getVal()) {
                            Utils.setSpeed(speed.getVal(), rotations[0], 1, 0.0D);
                        } else {
                            Utils.setSpeed(speed.getVal(), rotations[0], 1, 1.0D);
                        }
                    }
                    else {
                        Utils.setSpeed(speed.getVal());
                    }
                    if(mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.thePlayer.motionY += 1f;
                    }
                    if(mc.gameSettings.keyBindSneak.isKeyDown()) {
                        mc.thePlayer.motionY -= 1f;
                    }
                    break;
                }
            }
        }
        if(event instanceof EventPreMotionUpdate) {
            switch (mode.name()) {
                case "Watchdog Test":
                case "Cheatcraft": {
                    mc.thePlayer.motionY = -0.0;
                    if(mc.thePlayer.ticksExisted % 2 == 0 && !mc.thePlayer.isCollidedVertically) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0000000053721, mc.thePlayer.posZ);
                    }
                    break;
                }

                case "Verus Disabler": {
                    if (this.isToggled() && !mc.thePlayer.isCollidedVertically) {
                        mc.timer.timerSpeed = 0.941f;
                        if(timer.elapsed(450)) {
                            mc.thePlayer.jump();
                            timer.reset();
                        }
                        else if(!timer.elapsed(300)){
                            mc.thePlayer.onGround = true;
                            Utils.setSpeed(speed.getVal() / 2 * timer.getElapsedTime() / 100);
                            mc.thePlayer.motionY = 0;
                            mc.thePlayer.setPosition(mc.thePlayer.posX, Math.round(mc.thePlayer.posY), mc.thePlayer.posZ);
                        }
                    }
                    break;
                }
            }
        }
        if(event instanceof Event2D) {
            if(mode.name().equalsIgnoreCase("Watchdog") && flag) {
                Heliodor.INSTANCE().font2.drawCenteredString(timer.getElapsedTime() / 1000 + " seconds elapsed", GuiScreen.width / 2, GuiScreen.height / 2, -1);
            }
        }
//        if(event instanceof EventJump) {
//            event.setCancelled(true);
//        }
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
        startpos = new double[]{mc.thePlayer.posX, mc.thePlayer.posZ};
        mc.timer.timerSpeed = 1f;
        mc.thePlayer.stepHeight = 0;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;
        startPitch = mc.thePlayer.rotationPitch;
        if(mode.name().equalsIgnoreCase("Verus Disabler")) mc.thePlayer.motionY = 0.42;
        timer.reset();
    }
    @Override
    public void onDisable() {
        mc.thePlayer.capabilities.isFlying = false;
        mc.timer.timerSpeed = 1;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;
        mc.thePlayer.stepHeight = 0.625f;
        canFly = false;
        timer.reset();
        flag = false;
        hover = false;
        if(!packets.isEmpty()) {
            for(Packet packet : packets)
                mc.thePlayer.sendQueue.sendQueueBypass(packet);
            packets.clear();
        }
        stage = 0;
    }
}
