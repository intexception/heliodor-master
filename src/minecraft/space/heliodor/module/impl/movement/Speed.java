package space.heliodor.module.impl.movement;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import space.heliodor.Heliodor;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.*;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.module.impl.combat.KillAura;
import space.heliodor.module.impl.player.AntiVoid;
import space.heliodor.module.impl.player.Disabler;
import space.heliodor.settings.OptionMode;
import space.heliodor.utils.Stopwatch;
import space.heliodor.utils.movement.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Speed extends Module {
    private static final OptionMode mode = new OptionMode("Speed Mode", "Vanilla", "Vanilla", "Watchdog", "Cheatcraft", "AAC");
    private double speed = 0.03;
    private int targetDirection;
    private int speedMultiplier;
    private double speed2;
    private int animLength = 1;
    private boolean disable;
    private Stopwatch disableTimer = new Stopwatch();
    public Speed() {
        super("Speed", Keyboard.KEY_X, Category.MOVEMENT);
        this.options.add(mode);
    }

    @Override
    public void onEnable() {
        mc.timer.timerSpeed = 1f;
        disable = false;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
        speedMultiplier = 0;
        speed = 0.03;
        disable = false;
        disableTimer.reset();
        mc.thePlayer.noClip = false;
    }

    @OnEvent
    public void onEvent(Event event) {
        this.setDisplayName(this.name + " " + EnumChatFormatting.GRAY.toString() + mode.name());
        if(event instanceof Event2D) {
            if(disable) {
                if(disableTimer.elapsed(3000)) {
                    disable = false;
                }
                GL11.glPushMatrix();
                GL11.glTranslated(GuiScreen.width / 2 - 75,  GuiScreen.height / 2, GuiScreen.width / 2);
                Gui.drawRect(0,210 - 30, 150, 210, new Color(0,0,0,180).getRGB());
                mc.fontRendererObj.drawStringWithShadow("Speed will be enabled in...", 13, 210 - 25.5f, -1);
                animLength += ((disableTimer.getElapsedTime() - animLength) / 32) * 0.5;
                if (animLength < 0 || animLength > 3000) {
                    animLength = 3000;
                }
                else {
                    System.out.println(((animLength / 3000) * 140) + "");
                    mc.ingameGUI.drawGradientRect(5, 210 - 10, (int) ((animLength / 30) * 1.54f), 210 - 5, new Color(255, 255, 255, 255).getRGB(), new Color(160, 160, 159, 255).getRGB());
                }
                GL11.glPopMatrix();
            }
            else {
                disableTimer.reset();
                animLength = 1;
            }
        }
        if(event instanceof EventRecievePacket) {
            if(((EventRecievePacket) event).getPacket() instanceof S08PacketPlayerPosLook && mode.name().equalsIgnoreCase("Watchdog")) {
                disable = true;
            }
        }
        if(event instanceof EventUpdate) {
            if(Heliodor.INSTANCE().moduleMgr.getModuleByName("Flight").isToggled()) {
                this.toggle();
            }
            if(Heliodor.INSTANCE().moduleMgr.getModuleByName("Disabler").isToggled()) {
                disable = false;
            }
            if(mc.thePlayer.isInWater() || disable) {
                return;
            }
            switch(mode.name()) {
                case "Watchdog": {
                    if(Utils.isMoving()) {
                        mc.gameSettings.keyBindJump.pressed = false;
                        if(mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                            mc.thePlayer.motionY = -0.0032 - new Random().nextDouble() / 1000;
                            mc.thePlayer.jump();
                            speed = 0.00502238f + new Random().nextDouble() / 1000;
                        }
                        else {
                            speed2 = 0.2695 + speed;
                        }
                        speed -= speed / 42f;

                        if(speedMultiplier == 0) {
                            speed *= 1.02249f;
                        }
                        else if(speedMultiplier == 1) {
                            speed *= 0.98999124f;
                        }
                        else if(speedMultiplier == 2){
                            speed *= 0.96234872873f;
                        }
                        else if(speedMultiplier == 3) {
                            speed += 0.00062;
                        }
                        else if(speedMultiplier == 4) {
                            speed *= 0.98123f;
                        }
                        else if(speedMultiplier == 5) {
                            speed -= 0.000231f;
                        }
                        else if(speedMultiplier == 6) {
                            speedMultiplier = 0;
                        }
                        speedMultiplier++;

                        if(mc.thePlayer.isCollidedHorizontally) {
                            if (targetDirection == 1) {
                                targetDirection = -1;
                            } else {
                                targetDirection = 1;
                            }
                        }

                        if(new AntiVoid().canAntiVoid()) {
                            targetDirection = -targetDirection;
                        }

                        if (mc.gameSettings.keyBindLeft.isPressed()) {
                            targetDirection = 1;
                        }

                        if (mc.gameSettings.keyBindRight.isPressed()) {
                            targetDirection = -1;
                        }
                        if(mc.thePlayer.hurtTime > 0 && new Random().nextBoolean()) {
                            targetDirection = -targetDirection;
                        }
                        if(!mc.thePlayer.onGround) {
                            float rotations[] = new KillAura().getTarget() != null && Heliodor.INSTANCE().moduleMgr.getModuleByName("KillAura").isToggled() ? new KillAura().rotations(new KillAura().getTarget()) : new float[] {0,0} ;
                            if(Heliodor.INSTANCE().moduleMgr.getModuleByName("KillAura").isToggled() && new KillAura().getTarget() != null && Heliodor.INSTANCE().moduleMgr.getModuleByName("TargetStrafe").isToggled()) {
                                if ((double)mc.thePlayer.getDistanceToEntity(new KillAura().getTarget()) <= TargetStrafe.range.getVal()) {
                                    Utils.setSpeed(speed2, rotations[0], targetDirection, 0.0D);
                                } else {
                                    Utils.setSpeed(speed2, rotations[0], targetDirection, 1.0D);
                                }
                            }
                            else {
                                if(!mc.thePlayer.onGround) {
                                    Utils.setSpeed(speed2);
                                }
                            }
                        }
                    }
                    else {
                        Utils.setSpeed(0);
                    }
                    break;
                }
                case "Vanilla": {
                    if(mc.thePlayer.isCollidedHorizontally) {
                        if (targetDirection == 1) {
                            targetDirection = -1;
                        } else {
                            targetDirection = 1;
                        }
                    }
                    if(mc.thePlayer.hurtTime > 0) {
                        targetDirection = -targetDirection;
                    }

                    if(new AntiVoid().canAntiVoid()) {
                        targetDirection = -targetDirection;
                    }

                    if (mc.gameSettings.keyBindLeft.isPressed()) {
                        targetDirection = 1;
                    }

                    if (mc.gameSettings.keyBindRight.isPressed()) {
                        targetDirection = -1;
                    }
                    float rotations[] = new KillAura().getTarget() != null && Heliodor.INSTANCE().moduleMgr.getModuleByName("KillAura").isToggled() ? new KillAura().rotations(new KillAura().getTarget()) : new float[] {0,0} ;
                    if(Heliodor.INSTANCE().moduleMgr.getModuleByName("KillAura").isToggled() && new KillAura().getTarget() != null && Heliodor.INSTANCE().moduleMgr.getModuleByName("TargetStrafe").isToggled()) {
                        if ((double)mc.thePlayer.getDistanceToEntity(new KillAura().getTarget()) <= TargetStrafe.range.getVal()) {
                            Utils.setSpeed(1.2, rotations[0], targetDirection, 0.0D);
                        } else {
                            Utils.setSpeed(1.2, rotations[0], targetDirection, 1.0D);
                        }
                    }
                    else {
                        Utils.setSpeed(1.2);
                    }
                    if(mc.thePlayer.onGround && Utils.isMoving()) {
                        mc.thePlayer.jump();
                    }
                    break;
                }
                case "Cheatcraft": {
                    if(new AntiVoid().canAntiVoid()) {
                        targetDirection = -targetDirection;
                    }
                    if(mc.thePlayer.hurtTime > 0) {
                        targetDirection = -targetDirection;
                    }

                    if(mc.thePlayer.isCollidedHorizontally) {
                        if (targetDirection == 1) {
                            targetDirection = -1;
                        } else {
                            targetDirection = 1;
                        }
                    }

                    if (mc.gameSettings.keyBindLeft.isPressed()) {
                        targetDirection = 1;
                    }

                    if (mc.gameSettings.keyBindRight.isPressed()) {
                        targetDirection = -1;
                    }
                    float rotations[] = new KillAura().getTarget() != null && Heliodor.INSTANCE().moduleMgr.getModuleByName("KillAura").isToggled() ? new KillAura().rotations(new KillAura().getTarget()) : new float[] {0,0} ;
                    if(Heliodor.INSTANCE().moduleMgr.getModuleByName("KillAura").isToggled() && new KillAura().getTarget() != null && Heliodor.INSTANCE().moduleMgr.getModuleByName("TargetStrafe").isToggled()) {
                        if ((double)mc.thePlayer.getDistanceToEntity(new KillAura().getTarget()) <= TargetStrafe.range.getVal()) {
                            Utils.setSpeed(0.45, rotations[0], targetDirection, 0.0D);
                        } else {
                            Utils.setSpeed(0.45, rotations[0], targetDirection, 1.0D);
                        }
                    }
                    else {
                        Utils.setSpeed(0.45);
                    }
                    if(mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    }
                    break;

                }
                case "AAC": {
                    if(mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        mc.thePlayer.motionX *= 0.875;
                        mc.thePlayer.motionZ *= 0.875;
                    }
                    mc.thePlayer.motionX *= 1.005;
                    mc.thePlayer.motionZ *= 1.005;
                    break;
                }
            }
        }
    }
}
