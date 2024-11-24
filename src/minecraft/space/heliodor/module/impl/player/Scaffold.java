package space.heliodor.module.impl.player;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;
import space.heliodor.Heliodor;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventPostMotionUpdate;
import space.heliodor.event.impl.EventPreMotionUpdate;
import space.heliodor.event.impl.EventSendPacket;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.settings.OptionBool;
import space.heliodor.settings.OptionMode;
import space.heliodor.utils.BlockUtil;
import space.heliodor.utils.SendMessage;
import space.heliodor.utils.Stopwatch;
import space.heliodor.utils.movement.Utils;

import java.util.SplittableRandom;

public class Scaffold extends Module {
    private static final OptionBool expand = new OptionBool("Expand", true);
    public Scaffold() {
        super("Scaffold", Keyboard.KEY_B, Category.PLAYER);
        this.options.add(expand);
    }
    public float yaw, pitch;
    private double y;
    public Stopwatch timer = new Stopwatch();
    public BlockUtil block;
    BlockPos[] blockPosList = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1)};
    EnumFacing[] faceList = new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH};
    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof EventPostMotionUpdate) {
            if(Heliodor.INSTANCE().moduleMgr.getModuleByName("KillAura").isToggled()) {
                Heliodor.INSTANCE().moduleMgr.getModuleByName("KillAura").toggle();
            }
            mc.thePlayer.rotationYaw = yaw;
            mc.thePlayer.rotationPitch = pitch;
        }
        if(event instanceof EventSendPacket) {
            if(((EventSendPacket) event).getPacket() instanceof C0BPacketEntityAction) {
                mc.thePlayer.sendQueue.sendQueueBypass(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
        }
        if(event instanceof EventPreMotionUpdate) {
            yaw = mc.thePlayer.rotationYaw;
            pitch = mc.thePlayer.rotationPitch;
            float rotations[] = getRotations();
            mc.thePlayer.rotationYaw = rotations[0];
            mc.thePlayer.rotationPitch = rotations[1];
            block = getBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.boundingBox.minY, mc.thePlayer.posZ).offset(EnumFacing.DOWN));
            for(double i = 0; i < (expand.isEnabled() ? 1.3 : 0.1); i += 0.1) {
                block = getBlock(getExpandBlock(i).add(0, 0, 0).offset(EnumFacing.DOWN));
            }
            placeBlock();
        }
    }

    private boolean validBlockCheck(Block pos) {
        return (!(pos instanceof BlockLiquid)) && (pos.getMaterial() != Material.air);
    }
    ItemStack itemStack = null;
    private void placeBlock() {
        if(block != null) {
            for(int i = 0; i < 9; i++) {
                itemStack = mc.thePlayer.inventory.getStackInSlot(i);
                if(itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                    if(!(((ItemBlock) itemStack.getItem()).getBlock() instanceof BlockChest) && !(((ItemBlock) itemStack.getItem()).getBlock() instanceof BlockEnchantmentTable) && !(((ItemBlock) itemStack.getItem()).getBlock() instanceof BlockSnow) && !(((ItemBlock) itemStack.getItem()).getBlock() instanceof BlockSnowBlock)) {
                        mc.thePlayer.sendQueue.sendQueueBypass(new C09PacketHeldItemChange(i));
                        if(mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemStack, block.blockPos, block.facing, new Vec3(0, 0, 0))) {
                            mc.thePlayer.sendQueue.sendQueueBypass(new C0APacketAnimation());
                        }
                    }
                }
            }
        }
    }
    public BlockPos getExpandBlock(double expand) {
        BlockPos position = new BlockPos(mc.thePlayer.posX + (-Math.sin(Math.toRadians(mc.thePlayer.rotationYawHead)) * expand), mc.thePlayer.posY, mc.thePlayer.posZ + (Math.cos(Math.toRadians(mc.thePlayer.rotationYawHead)) * expand));
        return position;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
        mc.thePlayer.inventory.currentItem = mc.thePlayer.inventory.currentItem;
        mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
    }

    private float[] getRotations() {
        if(block != null && block.facing != null && block.blockPos != null) {
            float yaw = 0;
            float pitch = 78 + new SplittableRandom().nextInt(3);
            if(block.facing == EnumFacing.SOUTH) {
                yaw = -175 + new SplittableRandom().nextInt(7);
            }
            if(block.facing == EnumFacing.NORTH) {
                yaw = -5 + new SplittableRandom().nextInt(7);
            }
            if(block.facing == EnumFacing.EAST) {
                yaw = 85 + new SplittableRandom().nextInt(7);
            }
            if(block.facing == EnumFacing.WEST) {
                yaw = -85 + new SplittableRandom().nextInt(7);
            }
            return new float[]{yaw, pitch};
        }
        return  new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
    }
    private BlockUtil getBlock(BlockPos pos) {
        for (int i = 0; i < 4; i++) {
            BlockPos blockPos1 = pos.add(blockPosList[i]);
            if (validBlockCheck(mc.theWorld.getBlockState(blockPos1).getBlock())) {
                BlockUtil util = new BlockUtil(blockPos1, faceList[i]);
                return util;
            }
        }
        BlockPos blockPos2 = new BlockPos(0, -1, 0);
        if (validBlockCheck(mc.theWorld.getBlockState(pos.add(blockPos2)).getBlock())) {
            BlockUtil data = new BlockUtil(pos.add(blockPos2), EnumFacing.UP);
            return data;
        }
        for (int i = 0; i < 4; i++) {
            for (BlockPos blockPosition : blockPosList) {
                BlockPos blockPos3 = pos.add(blockPosition);
                BlockPos blockPos4 = blockPos3.add(blockPosList[i]);
                if (validBlockCheck(mc.theWorld.getBlockState(blockPos4).getBlock())) {
                    BlockUtil util = new BlockUtil(blockPos4, faceList[i]);
                    return util;
                }
            }
        }
        return null;
    }
}
