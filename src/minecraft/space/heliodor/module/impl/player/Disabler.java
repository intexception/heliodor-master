package space.heliodor.module.impl.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.ClickEvent;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.Display;
import space.heliodor.Heliodor;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventJump;
import space.heliodor.event.impl.EventPreMotionUpdate;
import space.heliodor.event.impl.EventRecievePacket;
import space.heliodor.event.impl.EventSendPacket;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.settings.OptionMode;
import space.heliodor.utils.SendMessage;
import space.heliodor.utils.movement.Utils;

import java.util.Random;
import java.util.SplittableRandom;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Disabler extends Module {
    private static final OptionMode mode = new OptionMode("Disabler Mode", "Craftplay", "Ghostly", "Craftplay", "Watchdog", "Full", "Test");
    public Disabler() {
        super("Disabler",0, Category.PLAYER);
        this.options.add(mode);
    }

    private static final CopyOnWriteArrayList<C0FPacketConfirmTransaction> bypassList = new  CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<C0CPacketInput> bypassList2 = new  CopyOnWriteArrayList<>();
    private static final SplittableRandom random = new SplittableRandom();
    private int current;
    @Override
    public void onEnable() {

    }
    @OnEvent
    public void onEvent(Event event) {
        this.setDisplayName(this.name + " " + EnumChatFormatting.GRAY.toString() + mode.name());
        if(event instanceof EventPreMotionUpdate) {
            switch (mode.name()) {
                case "Craftplay": {
                    if(mc.thePlayer.ticksExisted < 5) {
                        current = 0;
                        bypassList.clear();
                    }
                    if(mc.thePlayer.ticksExisted % 100 == 0 && bypassList.size() > 0 && (bypassList.size() - 1) > current) {
                        mc.thePlayer.sendQueue.sendQueueBypass(bypassList.get(++current));
                    }
                    if(mc.thePlayer.ticksExisted % 1000 == 0) {
                        current = 0;
                        bypassList.clear();
                    }
                    int bypass = 20 + random.nextInt(40);
                    if(mc.thePlayer.ticksExisted % 20 == 0) {
                        double rand = random.nextInt(3500);
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + rand, mc.thePlayer.posZ,
                                mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                    }
                    break;
                }
                case "Ghostly": {
                    if(!bypassList.isEmpty()) {
                        if(mc.thePlayer.ticksExisted % 50 == 0) {
                            for(int i = 0; i < 2; i++) {
                                mc.thePlayer.sendQueue.sendQueueBypass(bypassList.get(random.nextInt(bypassList.size() - 1)));
                            }
                        }
                    }
                    break;
                }
                case "Watchdog": {
                    if(mc.thePlayer.ticksExisted % 50 == 0) {
                        if(bypassList2.size() > 0) {
                            for(C0CPacketInput packet : bypassList2) {
                                mc.thePlayer.sendQueue.sendQueueBypass(packet);
                            }
                        }
                    }
                    break;
                }
            }
        }
        if(event instanceof EventSendPacket) {
            switch (mode.name()) {
                case "Watchdog": {
                    if(((EventSendPacket) event).getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
                        C03PacketPlayer.C06PacketPlayerPosLook packetPlayerPosLook = (C03PacketPlayer.C06PacketPlayerPosLook) ((EventSendPacket) event).getPacket();
                        packetPlayerPosLook.y -= 0.3;
                    }
                    break;
                }
                case "Craftplay": {
                    if(((EventSendPacket) event).getPacket() instanceof C0BPacketEntityAction) event.setCancelled(true);
                    if(((EventSendPacket) event).getPacket() instanceof C0FPacketConfirmTransaction) {
                        bypassList.add((C0FPacketConfirmTransaction) ((EventSendPacket) event).getPacket());
                        event.setCancelled(true);
                    }

                    if(((EventSendPacket) event).getPacket() instanceof C00PacketKeepAlive) {
                        ((C00PacketKeepAlive) ((EventSendPacket) event).getPacket()).key = -random.nextInt(Short.MAX_VALUE / 23);
                    }

                    if(((EventSendPacket) event).getPacket() instanceof C03PacketPlayer) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C0CPacketInput());
                    }
                    if(((EventSendPacket) event).getPacket() instanceof C0CPacketInput) {
                        C13PacketPlayerAbilities packetPlayerAbilities = new C13PacketPlayerAbilities();
                        packetPlayerAbilities.setAllowFlying(true);
                        C18PacketSpectate packetSpectate = new C18PacketSpectate(mc.thePlayer.getUniqueID());
                        mc.thePlayer.sendQueue.sendQueueBypass(packetSpectate);
                        mc.thePlayer.sendQueue.sendQueueBypass(packetPlayerAbilities);
                    }
                    break;
                }
                case "Ghostly": {
                    if(((EventSendPacket) event).getPacket() instanceof C0FPacketConfirmTransaction) {
                        event.setCancelled(true);
                    }
                    if(((EventSendPacket) event).getPacket() instanceof C00PacketKeepAlive) {
                        ((C00PacketKeepAlive) ((EventSendPacket) event).getPacket()).key -= random.nextInt(Integer.MAX_VALUE / 23);
                    }
                    if(((EventSendPacket) event).getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook && mc.thePlayer.onGround) {
                        if(mc.thePlayer.ticksExisted % 30 == 0) {
                            C03PacketPlayer.C06PacketPlayerPosLook packetPlayerPosLook = (C03PacketPlayer.C06PacketPlayerPosLook) ((EventSendPacket) event).getPacket();
                            packetPlayerPosLook.y += 1400 + random.nextInt(1000);
                        }
                    }
                    if(((EventSendPacket) event).getPacket() instanceof C03PacketPlayer) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C0CPacketInput(0,1,true,true));
                    }
                    break;
                }
            }
        }
    }
}
