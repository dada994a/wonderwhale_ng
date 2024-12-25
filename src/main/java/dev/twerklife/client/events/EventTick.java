package dev.twerklife.client.events;

import dev.twerklife.api.manager.event.EventArgument;
import dev.twerklife.api.manager.event.EventListener;

public class EventTick extends EventArgument {
    @Override
    public void call(EventListener listener) {
        listener.onTick(this);
    }
}
