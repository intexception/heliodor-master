package space.heliodor.module.impl.combat;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventPostMotionUpdate;
import space.heliodor.event.impl.EventPreMotionUpdate;
import space.heliodor.event.impl.EventUpdate;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.settings.OptionBool;
import space.heliodor.settings.OptionMode;
import space.heliodor.settings.OptionNumber;
import space.heliodor.utils.RaycastUtils;
import space.heliodor.utils.Stopwatch;

import java.util.Comparator;
import java.util.SplittableRandom;

public class KillAura extends Module {
    public static OptionMode mode = new OptionMode("KillAura Mode", "Single", "Single", "Multi", "AAC");
    public static OptionNumber reachSetting = new OptionNumber("Reach", 4.5f, 3, 5, 0.1f);
    public static OptionNumber apsSetting = new OptionNumber("APS", 15, 5, 20, 1);
    public static OptionBool autoblock = new OptionBool("Autoblock", true);
    public static OptionBool raycast = new OptionBool("Raycast", false);
    public static OptionBool players = new OptionBool("Players", true);
    public static OptionBool others = new OptionBool("Others", false);
    public static OptionBool swing = new OptionBool("Swing", true);
    public EntityLivingBase target;
    public boolean blocking;
    public Stopwatch timer = new Stopwatch();
    public float yaw, pitch;
    public float prevYaw, prevPitch;
    private static final Stopwatch switchTimer = new Stopwatch();
    private static final SplittableRandom random = new SplittableRandom();
    public KillAura() {
        super("KillAura", Keyboard.KEY_R, Category.COMBAT);
        this.options.add(mode);
        this.options.add(reachSetting);
        this.options.add(apsSetting);
        this.options.add(autoblock);
        this.options.add(raycast);
        this.options.add(swing);
        this.options.add(players);
        this.options.add(others);
    }

    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof EventPreMotionUpdate) {
            this.setDisplayName("KillAura " + EnumChatFormatting.GRAY.toString() + mode.name());
            boolean canAutoblock = mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && mc.thePlayer.getHeldItem().getItem() != null;
            yaw = mc.thePlayer.rotationYaw;
            pitch = mc.thePlayer.prevRotationPitch;
            prevYaw = mc.thePlayer.rotationYaw;
            prevPitch = mc.thePlayer.prevRotationPitch;
            Object[] possibleTargets = mc.theWorld.loadedEntityList.stream().filter(this::isValid).sorted(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer))).toArray();
            if(possibleTargets.length > 0) {
                if(!(possibleTargets[0] instanceof EntityLargeFireball)) {
                    if((target == null || !isValid(target))) {
                        target = (EntityLivingBase) possibleTargets[0];
                    }
                }
            }
            if (target == null)
                return;
            float[] facing = rotations(target);
            if(raycast.isEnabled())
                target = (EntityLivingBase) RaycastUtils.rayCast(reachSetting.getVal(), facing[0], facing[1]);
            if (target == null)
                return;
            if(mc.thePlayer.getDistanceToEntity(target) <= reachSetting.getVal()) {
                if(!(mode.name().equalsIgnoreCase("AAC"))) {
                    mc.thePlayer.rotationYaw = facing[0];
                    mc.thePlayer.rotationPitch = facing[1];
                    mc.thePlayer.rotationYawHead = facing[0];
                    mc.thePlayer.renderYawOffset = facing[0];
                    mc.thePlayer.prevRenderYawOffset = facing[0];
                }
                else {

                }
            }
            if(isValid(target)) {
                if(autoblock.isEnabled() && canAutoblock) {
                    mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 71626);
                    if(mc.getCurrentServerData() != null && !mc.getCurrentServerData().serverIP.contains("craftplay")) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-0.404757465367 + random.nextDouble() / 1000, -0.404757465367 + random.nextDouble() / 1000, -0.404757465367 + random.nextDouble() / 1000), 255, mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
                    }
                    blocking = true;
                }

                if(!timer.elapsed((long) (1000 / apsSetting.getVal()))) return;
                if(mode.name().equalsIgnoreCase("AAC")) {
                    if(random.nextInt(100) < 12) {
                        return;
                    }
                }
                if(mode.name().equalsIgnoreCase("Multi")) {
                    if(!switchTimer.elapsed(10)) return;
                }
                if(autoblock.isEnabled() && canAutoblock && !(mode.name().equalsIgnoreCase("AAC"))) {
                    if(mc.getCurrentServerData() != null && !mc.getCurrentServerData().serverIP.contains("craftplay")) {
                        mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(1, 1, 1), EnumFacing.DOWN));
                    }
                }
                if(swing.isEnabled()) {
                    mc.thePlayer.swingItem();
                }
                else {
                    mc.thePlayer.sendQueue.sendQueueBypass(new C0APacketAnimation());
                }
                mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                if(mode.name().equalsIgnoreCase("Multi")) {
                    target = null;
                    switchTimer.reset();
                }
                if(autoblock.isEnabled() && canAutoblock) {
                    mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 71626);
                    if(mc.getCurrentServerData() != null && !mc.getCurrentServerData().serverIP.contains("craftplay") && !(mode.name().equalsIgnoreCase("AAC"))) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-0.404757465367 + random.nextDouble() / 1000, -0.404757465367 + random.nextDouble() / 1000, -0.404757465367 + random.nextDouble() / 1000), 255, mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0));
                    }
                    blocking = true;
                }
            }
            timer.reset();
        }
        if(event instanceof EventPostMotionUpdate) {
            mc.thePlayer.rotationYaw = yaw;
            mc.thePlayer.rotationPitch = pitch;
            mc.thePlayer.prevRotationYaw = prevYaw;
            mc.thePlayer.prevRotationPitch = prevPitch;
            if (target == null)
                return;

            if(!timer.elapsed((long) (1000 / apsSetting.getVal()))) return;
            if(mode.name().equalsIgnoreCase("Multi")) {
                if(!switchTimer.elapsed(10)) return;
            }
            if(swing.isEnabled()) {
                mc.thePlayer.swingItem();
            }
            else {
                mc.thePlayer.sendQueue.sendQueueBypass(new C0APacketAnimation());
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
        }
    }

    public float[] rotations(Entity e) {
        double deltaX = e.boundingBox.minX + (e.boundingBox.maxX - e.boundingBox.minX) - mc.thePlayer.posX,
                deltaY = e.posY - 3.25 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
                deltaZ = e.boundingBox.minZ + (e.boundingBox.maxX - e.boundingBox.minX) - mc.thePlayer.posZ,
                distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

        float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ)),
                pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));

        if (deltaX < 0 && deltaZ < 0) {
            yaw = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        } else if (deltaX > 0 && deltaZ < 0) {
            yaw = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        }
        return new float[] {
                yaw, pitch
        };
    }
    public EntityLivingBase getTarget() {
        EntityLivingBase target = null;
        for(Entity entity : mc.theWorld.loadedEntityList) {
            if(entity instanceof EntityLivingBase) {
                EntityLivingBase entity2 = (EntityLivingBase) entity;
                if(mc.thePlayer.getDistanceToEntity(entity) <= reachSetting.getVal() && isValid(entity2)) {
                    target = entity2;
                }
            }
        }
        return target;
    }
    public boolean isValid(Entity entity) {
        if(entity != null) {
            if(entity.getDistanceToEntity(mc.thePlayer) <= reachSetting.getVal()) {
                if(entity instanceof EntityVillager) {
                    return false;
                }
                if(entity instanceof EntityLivingBase) {
                    if(entity != mc.thePlayer && !entity.isDead && ((EntityLivingBase)entity).getHealth() > 0) {
                        if(players.isEnabled() && (entity instanceof EntityOtherPlayerMP)) {
                            return true;
                        }
                        if(others.isEnabled() && entity instanceof EntityAnimal || entity instanceof EntityMob) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

}
