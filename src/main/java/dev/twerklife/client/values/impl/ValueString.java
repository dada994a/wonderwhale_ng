package dev.twerklife.client.values.impl;

import dev.twerklife.WonderWhale;
import dev.twerklife.client.events.EventClient;
import dev.twerklife.client.values.Value;

public class ValueString extends Value {
    private final String defaultValue;
    private String value;
    private final ValueCategory parent;

    public ValueString(String name, String tag, String description, String value) {
        super(name, tag, description);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
    }

    public ValueString(String name, String tag, String description, ValueCategory parent, String value) {
        super(name, tag, description);
        this.defaultValue = value;
        this.value = value;
        this.parent = parent;
    }

    public ValueCategory getParent() {
        return this.parent;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
        EventClient event = new EventClient(this);
        WonderWhale.EVENT_MANAGER.call(event);
    }
}
