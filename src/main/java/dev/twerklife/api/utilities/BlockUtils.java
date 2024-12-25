package dev.twerklife.api.utilities;

import dev.twerklife.WonderWhale;
import dev.twerklife.client.events.EventMotion;
import dev.twerklife.client.modules.client.ModuleRotations;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockUtils implements IMinecraft {

    public static void placeBlock(EventMotion event, BlockPos position, Hand hand) {
        if (!mc.world.getBlockState(position).canReplace(new ItemPlacementContext(mc.player, Hand.MAIN_HAND, mc.player.getStackInHand(Hand.MAIN_HAND), new BlockHitResult(Vec3d.of(position), Direction.UP, position, false)))) {
            return;
        }
        if (getPlaceableSide(position) == null) {
            return;
        }
        if (WonderWhale.MODULE_MANAGER.isModuleEnabled("Rotations")) {
            float[] rot = RotationUtils.getSmoothRotations(RotationUtils.getRotations(position.getX(), position.getY(), position.getZ()), ModuleRotations.INSTANCE.smoothness.getValue().intValue());
            RotationUtils.rotate(event, rot);
        }
        mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(hand, new BlockHitResult(Vec3d.of(position.offset(Objects.requireNonNull(getPlaceableSide(position)))), Objects.requireNonNull(getPlaceableSide(position)).getOpposite(), position.offset(Objects.requireNonNull(getPlaceableSide(position))), false), 0));
        mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
    }

    public static boolean isPositionPlaceable(BlockPos position, boolean entityCheck, boolean sideCheck) {
        if (!mc.world.getBlockState(position).getBlock().canReplace(mc.world.getBlockState(position), new ItemPlacementContext(mc.player, Hand.MAIN_HAND, mc.player.getStackInHand(Hand.MAIN_HAND), new BlockHitResult(Vec3d.of(position), Direction.UP, position, false)))) {
            return false;
        }
        if (entityCheck) {
            for (Entity entity : mc.world.getEntitiesByClass(Entity.class, new Box(position), Entity::isAlive)) {
                if (entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity) continue;
                return false;
            }
        }
        if (sideCheck) {
            return getPlaceableSide(position) != null;
        }
        return true;
    }

    public static boolean isPositionPlaceable(BlockPos position, boolean entityCheck, boolean sideCheck, boolean ignoreCrystals) {
        if (!mc.world.getBlockState(position).getBlock().canReplace(mc.world.getBlockState(position), new ItemPlacementContext(mc.player, Hand.MAIN_HAND, mc.player.getStackInHand(Hand.MAIN_HAND), new BlockHitResult(Vec3d.of(position), Direction.UP, position, false)))) {
            return false;
        }
        if (entityCheck) {
            for (Entity entity : mc.world.getEntitiesByClass(Entity.class, new Box(position), Entity::isAlive)) {
                if (entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity || entity instanceof EndCrystalEntity && ignoreCrystals) continue;
                return false;
            }
        }
        if (sideCheck) {
            return getPlaceableSide(position) != null;
        }
        return true;
    }

    public static Direction getPlaceableSide(BlockPos position) {
        for (Direction side : Direction.values()) {
            if (!mc.world.getBlockState(position.offset(side)).blocksMovement() || mc.world.getBlockState(position.offset(side)).isLiquid()) continue;
            return side;
        }
        return null;
    }

    public static List<BlockPos> getNearbyBlocks(PlayerEntity player, double blockRange, boolean motion) {
        ArrayList<BlockPos> nearbyBlocks = new ArrayList<>();
        int range = (int)MathUtils.roundToPlaces(blockRange, 0);
        if (motion) {
            player.getPos().add(Vec3d.of(new Vec3i((int) player.getVelocity().x, (int) player.getVelocity().y, (int) player.getVelocity().z)));
        }
        for (int x = -range; x <= range; ++x) {
            for (int y = -range; y <= range - range / 2; ++y) {
                for (int z = -range; z <= range; ++z) {
                    nearbyBlocks.add(BlockPos.ofFloored(player.getPos().add(x, y, z)));
                }
            }
        }
        return nearbyBlocks;
    }

    public static BlockResistance getBlockResistance(BlockPos block) {
        if (mc.world.isAir(block)) {
            return BlockResistance.Blank;
        }
        if (!(mc.world.getBlockState(block).getBlock().getHardness() == -1.0f || mc.world.getBlockState(block).getBlock().equals(Blocks.OBSIDIAN) || mc.world.getBlockState(block).getBlock().equals(Blocks.ANVIL) || mc.world.getBlockState(block).getBlock().equals(Blocks.ENCHANTING_TABLE) || mc.world.getBlockState(block).getBlock().equals(Blocks.ENDER_CHEST))) {
            return BlockResistance.Breakable;
        }
        if (mc.world.getBlockState(block).getBlock().equals(Blocks.OBSIDIAN) || mc.world.getBlockState(block).getBlock().equals(Blocks.ANVIL) || mc.world.getBlockState(block).getBlock().equals(Blocks.ENCHANTING_TABLE) || mc.world.getBlockState(block).getBlock().equals(Blocks.ENDER_CHEST)) {
            return BlockResistance.Resistant;
        }
        if (mc.world.getBlockState(block).getBlock().equals(Blocks.BEDROCK)) {
            return BlockResistance.Unbreakable;
        }
        return null;
    }

    public enum BlockResistance {
        Blank,
        Breakable,
        Resistant,
        Unbreakable
    }
}
