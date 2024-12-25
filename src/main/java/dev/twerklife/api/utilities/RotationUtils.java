package dev.twerklife.api.utilities;

import dev.twerklife.client.events.EventMotion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class RotationUtils implements IMinecraft {
    private static float c;
    private static float b;

    public static float[] getRotations(double posX, double posY, double posZ) {
        PlayerEntity player = mc.player;
        double x = posX - player.getX();
        double y = posY - (player.getY() + (double)player.getEyeHeight(player.getPose()));
        double z = posZ - player.getZ();
        double dist = MathHelper.sqrt((float) (x * x + z * z));
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationsEntity(Entity entity) {
        return RotationUtils.getRotations(entity.getX(), entity.getY() + (double)entity.getEyeHeight(entity.getPose()) - 0.4, entity.getZ());
    }

    public static void rotate(EventMotion event, float[] angle) {
        event.setRotationYaw(angle[0]);
        event.setRotationPitch(angle[1]);
        event.setRotated(true);
    }

    public static float[] getSmoothRotations(float[] angles, int smooth) {
        float var2 = MathHelper.clamp(1.0f - (float)smooth / 100.0f, 0.1f, 1.0f);
        c += (angles[0] - c) * var2;
        b += (angles[1] - b) * var2;
        return new float[]{MathHelper.wrapDegrees(c), b};
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle = Math.abs(angle1 - angle2) % 360.0f;
        if (angle > 180.0f) {
            angle = 360.0f - angle;
        }
        return angle;
    }
}
