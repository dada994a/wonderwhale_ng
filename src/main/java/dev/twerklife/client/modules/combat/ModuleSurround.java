package dev.twerklife.client.modules.combat;

import dev.twerklife.WonderWhale;
import dev.twerklife.api.manager.module.Module;
import dev.twerklife.api.manager.module.RegisterModule;
import dev.twerklife.api.utilities.BlockUtils;
import dev.twerklife.api.utilities.ChatUtils;
import dev.twerklife.api.utilities.InventoryUtils;
import dev.twerklife.api.utilities.MathUtils;
import dev.twerklife.client.events.EventMotion;
import dev.twerklife.client.values.impl.ValueBoolean;
import dev.twerklife.client.values.impl.ValueEnum;
import dev.twerklife.client.values.impl.ValueNumber;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

@RegisterModule(name="Surround", description="Places blocks around your feet to protect you from crystals.", category=Module.Category.COMBAT)
public class ModuleSurround extends Module {
    private final ValueEnum mode = new ValueEnum("Mode", "Mode", "The mode for the Surround.", Modes.Normal);
    private final ValueEnum autoSwitch = new ValueEnum("Switch", "Switch", "The mode for Switching.", InventoryUtils.SwitchModes.Normal);
    private final ValueEnum itemSwitch = new ValueEnum("Item", "Item", "The item to place the blocks with.", InventoryUtils.ItemModes.Obsidian);
    private final ValueNumber blocks = new ValueNumber("Blocks", "Blocks", "The amount of blocks that can be placed per tick.", 8, 1, 40);
    private final ValueEnum supports = new ValueEnum("Supports", "Supports", "The support blocks for the Surround.", Supports.Dynamic);
    private final ValueBoolean dynamic = new ValueBoolean("Dynamic", "Dynamic", "Makes the surround place dynamically.", true);
    private final ValueBoolean ignoreCrystals = new ValueBoolean("IgnoreCrystals", "Ignore Crystals", "Ignores crystals when checking if there are any entities in the block that needs to be placed.", false);
    private final ValueBoolean stepDisable = new ValueBoolean("StepDisable", "Step Disable", "Disable if step enabled.", true);
    private final ValueBoolean jumpDisable = new ValueBoolean("JumpDisable", "Jump Disable", "Disable if player jumps.", true);
    private int placements;
    private BlockPos startPosition;

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.player == null || mc.world == null) {
            this.disable(false);
            return;
        }
        this.startPosition = new BlockPos((int) Math.round(mc.player.getX()), (int) Math.round(mc.player.getY()), (int) Math.round(mc.player.getZ()));
    }

    @Override
    public void onMotion(EventMotion event) {
        super.onMotion(event);
        if ((double)this.startPosition.getY() != MathUtils.roundToPlaces(mc.player.getY(), 0) && this.mode.getValue().equals(Modes.Normal)) {
            this.disable(true);
            return;
        }
        if (this.jumpDisable.getValue() && mc.options.jumpKey.isPressed() || this.stepDisable.getValue() && WonderWhale.MODULE_MANAGER.isModuleEnabled("Step")) {
            this.disable(true);
            return;
        }
        int slot = InventoryUtils.getTargetSlot(this.itemSwitch.getValue().toString());
        int lastSlot = mc.player.getInventory().selectedSlot;
        if (slot == -1) {
            ChatUtils.sendMessage("No blocks could be found.", "Surround");
            this.disable(true);
            return;
        }
        if (!this.getUnsafeBlocks().isEmpty()) {
            InventoryUtils.switchSlot(slot, this.autoSwitch.getValue().equals(InventoryUtils.SwitchModes.Silent));
            for (BlockPos position : this.getUnsafeBlocks()) {
                if (!this.supports.getValue().equals(Supports.None) && (BlockUtils.getPlaceableSide(position) == null || this.supports.getValue().equals(Supports.Static))) {
                    this.placeBlock(event, position.down());
                }
                this.placeBlock(event, position);
            }
            if (!this.autoSwitch.getValue().equals(InventoryUtils.SwitchModes.Strict)) {
                InventoryUtils.switchSlot(lastSlot, this.autoSwitch.getValue().equals(InventoryUtils.SwitchModes.Silent));
            }
        }
        this.placements = 0;
        if (this.getUnsafeBlocks().isEmpty() && this.mode.getValue().equals(Modes.Toggle)) {
            this.disable(true);
        }
    }

    public void placeBlock(EventMotion event, BlockPos position) {
        if (BlockUtils.isPositionPlaceable(position, true, true, this.ignoreCrystals.getValue()) && this.placements < this.blocks.getValue().intValue()) {
            BlockUtils.placeBlock(event, position, Hand.MAIN_HAND);
            ++this.placements;
        }
    }

    public List<BlockPos> getUnsafeBlocks() {
        ArrayList<BlockPos> positions = new ArrayList<>();
        for (BlockPos position : this.getOffsets()) {
            if (!mc.world.getBlockState(position).canReplace(new ItemPlacementContext(mc.player, Hand.MAIN_HAND, mc.player.getStackInHand(Hand.MAIN_HAND), new BlockHitResult(Vec3d.of(position), Direction.UP, position, false)))) continue;
            positions.add(position);
        }
        return positions;
    }

    private List<BlockPos> getOffsets() {
        ArrayList<BlockPos> offsets = new ArrayList<>();
        if (this.dynamic.getValue()) {
            int z;
            int x;
            double decimalX = Math.abs(mc.player.getX()) - Math.floor(Math.abs(mc.player.getX()));
            double decimalZ = Math.abs(mc.player.getZ()) - Math.floor(Math.abs(mc.player.getZ()));
            int lengthX = this.calculateLength(decimalX, false);
            int negativeLengthX = this.calculateLength(decimalX, true);
            int lengthZ = this.calculateLength(decimalZ, false);
            int negativeLengthZ = this.calculateLength(decimalZ, true);
            ArrayList<BlockPos> tempOffsets = new ArrayList<>();
            offsets.addAll(this.getOverlapPositions());
            for (x = 1; x < lengthX + 1; ++x) {
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), x, 1 + lengthZ));
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), x, -(1 + negativeLengthZ)));
            }
            for (x = 0; x <= negativeLengthX; ++x) {
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), -x, 1 + lengthZ));
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), -x, -(1 + negativeLengthZ)));
            }
            for (z = 1; z < lengthZ + 1; ++z) {
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), 1 + lengthX, z));
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), -(1 + negativeLengthX), z));
            }
            for (z = 0; z <= negativeLengthZ; ++z) {
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), 1 + lengthX, -z));
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), -(1 + negativeLengthX), -z));
            }
            offsets.addAll(tempOffsets);
        } else {
            for (Direction side : Direction.Type.HORIZONTAL) {
                offsets.add(this.getPlayerPosition().add(side.getOffsetX(), 0, side.getOffsetZ()));
            }
        }
        return offsets;
    }

    private BlockPos getPlayerPosition() {
        return new BlockPos(mc.player.getBlockX(), (int) (mc.player.getBlockY() - Math.floor(mc.player.getY()) > 0.8 ? Math.floor(mc.player.getY()) + 1.0 : Math.floor(mc.player.getY())), mc.player.getBlockZ());
    }

    private List<BlockPos> getOverlapPositions() {
        ArrayList<BlockPos> positions = new ArrayList<>();
        int offsetX = this.calculateOffset(mc.player.getX() - Math.floor(mc.player.getX()));
        int offsetZ = this.calculateOffset(mc.player.getZ() - Math.floor(mc.player.getZ()));
        positions.add(this.getPlayerPosition());
        for (int x = 0; x <= Math.abs(offsetX); ++x) {
            for (int z = 0; z <= Math.abs(offsetZ); ++z) {
                int properX = x * offsetX;
                int properZ = z * offsetZ;
                positions.add(this.getPlayerPosition().add(properX, -1, properZ));
            }
        }
        return positions;
    }

    private BlockPos addToPosition(BlockPos position, double x, double z) {
        if (position.getX() < 0) {
            x = -x;
        }
        if (position.getZ() < 0) {
            z = -z;
        }
        return position.add((int) x, 0, (int) z);
    }

    private int calculateOffset(double dec) {
        return dec >= 0.7 ? 1 : (dec <= 0.3 ? -1 : 0);
    }

    private int calculateLength(double decimal, boolean negative) {
        if (negative) {
            return decimal <= 0.3 ? 1 : 0;
        }
        return decimal >= 0.7 ? 1 : 0;
    }

    public enum Supports {
        None,
        Dynamic,
        Static
    }

    public enum Modes {
        Normal,
        Persistent,
        Toggle,
        Shift
    }
}
