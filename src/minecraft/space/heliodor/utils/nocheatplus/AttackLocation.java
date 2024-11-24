package space.heliodor.utils.nocheatplus;

import net.minecraft.client.Minecraft;
import space.heliodor.utils.TrigUtil;

import java.util.UUID;

public class AttackLocation {
    Minecraft mc = Minecraft.getMinecraft();
    public final double x, y, z;
    /** Yaw of the attacker. */
    public final float yaw;
    public long time;
    public final UUID damagedId;
    /** Squared distance to the last location (0 if none given). */
    public final double distSqLast;
    /** Difference in yaw to the last location (0 if none given). */
    public final double yawDiffLast;
    /** Time difference to the last location (0 if none given). */
    public final long timeDiff;
    /** If the id differs from the last damaged entity (true if no lastLoc is given). */
    public final boolean idDiffLast;
    public AttackLocation(final UUID damagedId, final long time, final AttackLocation lastLoc) {
        x = mc.thePlayer.posX;
        y = mc.thePlayer.posY;
        z = mc.thePlayer.posZ;
        yaw = mc.thePlayer.rotationYaw;
        this.time = time;
        this.damagedId = damagedId;

        if (lastLoc != null) {
            distSqLast = TrigUtil.distanceSquared(x, y, z, lastLoc.x, lastLoc.y, lastLoc.z);
            yawDiffLast = TrigUtil.yawDiff(yaw, lastLoc.yaw);
            timeDiff = Math.max(0L, time - lastLoc.time);
            idDiffLast = !damagedId.equals(lastLoc.damagedId);
        } else {
            distSqLast = 0.0;
            yawDiffLast = 0f;
            timeDiff = 0L;
            idDiffLast = true;
        }
    }
}
