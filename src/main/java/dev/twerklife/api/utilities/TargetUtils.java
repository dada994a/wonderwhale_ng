package dev.twerklife.api.utilities;

import dev.twerklife.WonderWhale;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;

public class TargetUtils implements IMinecraft {
    public static LivingEntity getTarget(float range, float wallRange, boolean visible, TargetMode targetMode) {
        LivingEntity targetEntity = null;
        for (Entity e : mc.world.getEntities()) {
            LivingEntity entity;
            if (!(e instanceof LivingEntity) || (!mc.player.canSee(entity = (LivingEntity)e) ? !(mc.player.squaredDistanceTo(entity.getX(), entity.getY(), entity.getZ()) <= (double)wallRange) : !(mc.player.squaredDistanceTo(entity.getX(), entity.getY(), entity.getZ()) <= (double)range))) continue;
            if (WonderWhale.FRIEND_MANAGER.isFriend(e.getName().getString()) || entity == mc.player && entity.getName().equals(mc.player.getName()) || !(entity instanceof PlayerEntity) || (entity.isDead() || entity.getHealth() <= 0.0f) && !mc.player.canSee(entity) && visible || ((PlayerEntity)entity).isCreative()) continue;
            if (targetEntity == null) {
                targetEntity = entity;
                continue;
            }
            if (targetMode == TargetMode.Range) {
                if (!(mc.player.squaredDistanceTo(entity.getX(), entity.getY(), entity.getZ()) < mc.player.squaredDistanceTo(targetEntity.getX(), targetEntity.getY(), targetEntity.getZ()))) continue;
                targetEntity = entity;
                continue;
            }
            if (!(entity.getHealth() + entity.getAbsorptionAmount() < targetEntity.getHealth() + targetEntity.getAbsorptionAmount())) continue;
            targetEntity = entity;
        }
        return targetEntity;
    }

    public static PlayerEntity getTarget(float range) {
        PlayerEntity targetPlayer = null;
        for (PlayerEntity player : new ArrayList<>(mc.world.getPlayers())) {
            if (mc.player.squaredDistanceTo(player) > (double)MathUtils.square(range) || player == mc.player || WonderWhale.FRIEND_MANAGER.isFriend(player.getName().getString()) || player.isDead() || player.getHealth() <= 0.0f) continue;
            if (targetPlayer == null) {
                targetPlayer = player;
                continue;
            }
            if (!(mc.player.squaredDistanceTo(player) < mc.player.squaredDistanceTo(targetPlayer))) continue;
            targetPlayer = player;
        }
        return targetPlayer;
    }

    public enum TargetMode {
        Range,
        Health
    }
}
