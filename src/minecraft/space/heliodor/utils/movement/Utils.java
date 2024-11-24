package space.heliodor.utils.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.SplittableRandom;

public class Utils {
    static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isMoving() {
        return Minecraft.getMinecraft().thePlayer.movementInput.moveForward != 0F || Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe != 0F;
    }

    public static double getDirection() {
        float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;

        if(Minecraft.getMinecraft().thePlayer.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if(Minecraft.getMinecraft().thePlayer.moveForward < 0F)
            forward = -0.5F;
        else if(Minecraft.getMinecraft().thePlayer.moveForward > 0F)
            forward = 0.5F;

        if(Minecraft.getMinecraft().thePlayer.moveStrafing > 0F)
            rotationYaw -= 90F * forward;

        if(Minecraft.getMinecraft().thePlayer.moveStrafing < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }


    public static double baseMoveSpeed() {
        return 0.2875;
    }

    public static void setSpeed(final double moveSpeed) {
        setSpeed(moveSpeed, Minecraft.getMinecraft().thePlayer.rotationYaw, Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe, Minecraft.getMinecraft().thePlayer.movementInput.moveForward);
    }

    public static void setSpeed(final double moveSpeed, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += ((forward > 0.0) ? -45 : 45);
            }
            else if (strafe < 0.0) {
                yaw += ((forward > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            }
            else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = -1.0;
        }
        final double offsetX = Math.cos(Math.toRadians(yaw + 90.0f));
        final double offsetZ = Math.sin(Math.toRadians(yaw + 90.0f));
        mc.thePlayer.motionX = forward * moveSpeed * offsetX + strafe * moveSpeed * offsetZ;
        mc.thePlayer.motionZ = forward * moveSpeed * offsetZ - strafe * moveSpeed * offsetX;
    }

    public static void strafe(final float speed) {
        if(!isMoving())
            return;

        final double yaw = getDirection();
        Minecraft.getMinecraft().thePlayer.motionX = -Math.sin(yaw) * speed;
        Minecraft.getMinecraft().thePlayer.motionZ = Math.cos(yaw) * speed;
    }

    public static void strafe() {
        strafe(getSpeed());
    }
    public static float getSpeed() {
        return (float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ) * mc.timer.timerSpeed;
    }

    public static void hypixelDmg() {
        SplittableRandom random = new SplittableRandom();
        int dmgAmount = 57 + random.nextInt(4);
        double dmgBypassVal = random.nextDouble();
        for (int i = 0; i < dmgAmount; i++) {
            mc.thePlayer.sendQueue.sendQueueBypass(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.06076984763247362846783276 + dmgBypassVal / 999, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.sendQueueBypass(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.06076984763247362846783276 / 100 + dmgBypassVal / 99, mc.thePlayer.posZ,false));
            mc.thePlayer.sendQueue.sendQueueBypass(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.06076984763247362846783276 / 100 + dmgBypassVal / 99, mc.thePlayer.posZ,false));
        }
        mc.thePlayer.sendQueue.sendQueueBypass(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,true));
    }
}

