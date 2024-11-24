package space.heliodor.module.impl.player;

import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.util.Vector3d;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventRecievePacket;
import space.heliodor.event.impl.EventUpdate;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.settings.OptionBool;
import space.heliodor.utils.SendMessage;
import space.heliodor.utils.TrigUtil;
import space.heliodor.utils.nocheatplus.AttackLocation;
import space.heliodor.utils.nocheatplus.CollisionUtil;
import space.heliodor.utils.nocheatplus.TickTask;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

public class NoCheatPlus extends Module {
    private static final OptionBool angle = new OptionBool("Angle", true);
    public static long maxTimeDiff = 1000L;
    LinkedList<AttackLocation> angleHits = new LinkedList<AttackLocation>();
    double angleVL;
    public NoCheatPlus() {
        super("NoCheatPlus",0, Category.COMBAT);
        this.options.add(angle);
    }

    @OnEvent
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
           if(angle.isEnabled()) {
               boolean cancel = false;

               final long time = System.currentTimeMillis();
               AttackLocation lastLoc = angleHits.isEmpty() ? null : angleHits.getLast();
               if (lastLoc != null && time - lastLoc.time > maxTimeDiff) {
                   angleHits.clear();
                   lastLoc = null;
               }

               if(mc.thePlayer.getLastAttacker() != null) {
                   angleHits.add(new AttackLocation(mc.thePlayer.getLastAttacker().getUniqueID(), System.currentTimeMillis(), lastLoc));
               }

               double deltaMove = 0D;
               long deltaTime = 0L;
               float deltaYaw = 0f;
               int deltaSwitchTarget = 0;
               final Iterator<AttackLocation> it =angleHits.iterator();
               while (it.hasNext()) {
                   final AttackLocation refLoc = it.next();
                   if (time - refLoc.time > maxTimeDiff) {
                       it.remove();
                       continue;
                   }
                   deltaMove += refLoc.distSqLast;
                   final double yawDiff = Math.abs(refLoc.yawDiffLast);
                   deltaYaw += yawDiff;
                   deltaTime += refLoc.timeDiff;
                   if (refLoc.idDiffLast && yawDiff > 30.0) {
                       deltaSwitchTarget += 1;
                   }
               }
               if (angleHits.size() < 2) {
                   cancel = false;
               }

               final double n = (double) (angleHits.size() - 1);

               final double averageMove = deltaMove / n;

               final double averageTime = (double) deltaTime / n;

               final double averageYaw = (double) deltaYaw / n;

               final double averageSwitching = (double) deltaSwitchTarget / n;

               double violation = 0.0;

               if (averageMove >= 0.0 && averageMove < 0.2D) {
                   violation += 20.0 * (0.2 - averageMove) / 0.2;
               }

               if (averageTime >= 0.0 && averageTime < 150.0) {
                   violation += 30.0 * (150.0 - averageTime) / 150.0;
               }

               if (averageYaw > 50.0) {
                   violation += 30.0 * averageYaw / 180.0;
               }

               if (averageSwitching > 0.0) {
                   violation += 20.0 * averageSwitching;
               }

               if (violation > 50) {
                   if (TickTask.getLag(maxTimeDiff, true) < 1.5f){
                       angleVL += violation;
                   }
                   cancel = true;
               } else {
                   angleVL *= 0.98D;
               }
               if(cancel) {
                   SendMessage.log("Flag: FIGHT_ANGLE, VL:" + angleVL + "Violation:" + violation);
               }
           }
        }
        if(event instanceof EventRecievePacket) {
            if(((EventRecievePacket) event).getPacket() instanceof S00PacketDisconnect) {
                angleHits.clear();
            }

        }
    }
}
