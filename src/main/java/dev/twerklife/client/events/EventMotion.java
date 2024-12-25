package dev.twerklife.client.events;

import dev.twerklife.api.manager.event.EventArgument;
import dev.twerklife.api.manager.event.EventListener;

public class EventMotion extends EventArgument {
    private float rotationYaw;
    private float rotationPitch;
    private float oldRotationYaw;
    private float oldRotationPitch;
    private boolean rotated;

    public EventMotion() {
        this.rotated = false;
    }

    public EventMotion(float rotationYaw, float rotationPitch) {
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
        this.oldRotationYaw = rotationYaw;
        this.oldRotationPitch = rotationPitch;
        this.rotated = false;
    }

    public final float getRotationYaw() {
        return this.rotationYaw;
    }

    public final float getRotationPitch() {
        return this.rotationPitch;
    }

    public final void setRotationYaw(float i) {
        this.rotationYaw = i;
        this.rotated = true;
    }

    public final void setRotationPitch(float i) {
        this.rotationPitch = i;
        this.rotated = true;
    }

    public final float getOldRotationYaw() {
        return this.oldRotationYaw;
    }

    public final float getOldRotationPitch() {
        return this.oldRotationPitch;
    }

    public final boolean isRotated() {
        return this.rotated;
    }

    public final void setRotated(boolean rotated) {
        this.rotated = rotated;
    }

    @Override
    public void call(EventListener listener) {
        listener.onMotion(this);
    }
}