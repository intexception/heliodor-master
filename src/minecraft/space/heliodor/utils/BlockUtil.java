package space.heliodor.utils;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class BlockUtil {
    public BlockPos blockPos;
    public EnumFacing facing;

    public BlockUtil(BlockPos blockPos, EnumFacing facing) {
        this.blockPos = blockPos;
        this.facing = facing;
    }

    public Vec3 getVector() {
        Vec3i directionVec = facing.getDirectionVec();
        double x = directionVec.getX() * 0.5;
        double y = directionVec.getY() * 0.5;
        double z = directionVec.getZ() * 0.5;
        return new Vec3(blockPos).addVector(0.5D, 0.5D, 0.5D).addVector(x, y, z);
    }
}
