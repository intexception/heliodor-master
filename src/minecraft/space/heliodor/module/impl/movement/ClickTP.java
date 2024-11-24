package space.heliodor.module.impl.movement;

import com.sun.javafx.geom.Vec3d;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.Vector3d;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventUpdate;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.utils.SendMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClickTP extends Module {
    public ClickTP() {
        super("ClickTP", Keyboard.KEY_U, Category.MOVEMENT);
    }

    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            if(mc.objectMouseOver != null) {
                if(mc.objectMouseOver.getBlockPos() != null) {
                    float x = (float) (mc.objectMouseOver.getBlockPos().getX() + (new Random().nextDouble() - 0.5));
                    float y = mc.objectMouseOver.getBlockPos().getY();
                    float z = (float) (mc.objectMouseOver.getBlockPos().getZ() + (new Random().nextDouble() - 0.5));
                    if(Mouse.isButtonDown(0)) {
                        findPath(x, y + 5.0D, z, 8D).forEach(pos -> mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pos.x,pos.y,pos.z, false)));
                        this.toggle();
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
