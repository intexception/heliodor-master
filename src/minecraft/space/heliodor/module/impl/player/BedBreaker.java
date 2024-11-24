package space.heliodor.module.impl.player;

import net.minecraft.block.BlockBanner;
import net.minecraft.block.BlockBed;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventUpdate;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.utils.BlockUtil;

public class BedBreaker extends Module {
    public BedBreaker() {
        super("BedBreaker",0, Category.PLAYER);
    }

    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            for(double xRange = -5; xRange < 5; xRange++) {
                for(double yRange = -5; yRange < 5; yRange++) {
                    for(double zRange = -5; zRange < 5; zRange++) {
                        BlockUtil block = new BlockUtil(new BlockPos(mc.thePlayer.posX + xRange, mc.thePlayer.posY + yRange, mc.thePlayer.posZ + zRange), EnumFacing.UP);
                        if(mc.theWorld.getBlockState(block.blockPos).getBlock() instanceof BlockBed) {
                            mc.thePlayer.sendQueue.sendQueueBypass(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, block.blockPos, block.facing));
                            mc.thePlayer.sendQueue.sendQueueBypass(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, block.blockPos, block.facing));
                            mc.thePlayer.sendQueue.sendQueueBypass(new C0APacketAnimation());
                        }
                    }
                }
            }
        }
    }
}
