package dev.twerklife.client.values.impl;

import dev.twerklife.WonderWhale;
import dev.twerklife.client.events.EventClient;
import dev.twerklife.client.values.Value;

import java.util.ArrayList;
import java.util.Arrays;

public class ValueEnum extends Value {
    private final Enum defaultValue;
    private Enum value;
    private final ValueCategory parent;

    public ValueEnum(String name, String tag, String description, Enum value) {
        super(name, tag, description);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
    }

    public ValueEnum(String name, String tag, String description, ValueCategory parent, Enum value) {
        super(name, tag, description);
        this.defaultValue = value;
        this.value = value;
        this.parent = parent;
    }

    public ValueCategory getParent() {
        return this.parent;
    }

    public Enum getDefaultValue() {
        return this.defaultValue;
    }

    public Enum getValue() {
        return this.value;
    }

    public void setValue(Enum value) {
        this.value = value;
        EventClient event = new EventClient(this);
        WonderWhale.EVENT_MANAGER.call(event);
    }

    public Enum getEnum(String name) {
        for (Enum value : this.getEnums()) {
            if (!value.name().equals(name)) continue;
            return value;
        }
        return null;
    }

    public ArrayList<Enum> getEnums() {
        return new ArrayList<>(Arrays.asList(this.value.getClass().getEnumConstants()));
    }
}
