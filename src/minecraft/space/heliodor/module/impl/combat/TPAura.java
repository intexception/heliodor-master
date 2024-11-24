package space.heliodor.module.impl.combat;

import com.google.common.collect.Lists;
import com.sun.javafx.geom.Vec3d;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventUpdate;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.utils.Stopwatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TPAura extends Module {
    private static final Stopwatch stopwatch = new Stopwatch();
    double x, y, z;
    public TPAura() {
        super("TPAura",0, Category.COMBAT);
    }

    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            for(Object entity : mc.theWorld.loadedEntityList) {
                if(entity instanceof EntityOtherPlayerMP) {
                    EntityOtherPlayerMP player = (EntityOtherPlayerMP) entity;
                    if(mc.thePlayer.getDistanceToEntity(player) < 100) {
                        mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 71626);
                        if(!stopwatch.elapsed(500)) return;
                        x = player.posX;
                        y = player.posY;
                        z = player.posZ;
                        List<Vec3d> positions = findPath(x, y + 1, z, 4D);
                        List<Vec3d> positionsBackwards = Lists.reverse(positions);
                        for(Vec3d position : positions) {
                            mc.thePlayer.sendQueue.sendQueueBypass(new C03PacketPlayer.C04PacketPlayerPosition(position.x, position.y, position.z, true));
                        }
                        mc.thePlayer.swingItem();
                        for(int i = 0; i < 10; i++) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(player, C02PacketUseEntity.Action.ATTACK));
                        }
                        for(Vec3d position : positionsBackwards) {
                            mc.thePlayer.sendQueue.sendQueueBypass(new C03PacketPlayer.C04PacketPlayerPosition(position.x, position.y, position.z, true));
                        }
                        stopwatch.reset();
                    }
                }
            }
        }
    }

    public List<Vec3d> findPath(final double tpX, final double tpY, final double tpZ, final double offset) {
        final List<Vec3d> positions = new ArrayList<>();
        final double steps = Math.ceil(mc.thePlayer.getDistance(tpX, tpY, tpZ) / offset);

        final double dX = tpX - mc.thePlayer.posX;
        final double dY = tpY - mc.thePlayer.posY;
        final double dZ = tpZ - mc.thePlayer.posZ;

        for(double d = 1D; d <= steps; ++d) {
            positions.add(new Vec3d(mc.thePlayer.posX + (dX * d) / steps, mc.thePlayer.posY + (dY * d) / steps, mc.thePlayer.posZ + (dZ * d) / steps));
        }

        return positions;
    }
}
