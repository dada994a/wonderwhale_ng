package dev.twerklife.client.events;

import dev.twerklife.api.manager.event.EventArgument;
import dev.twerklife.api.manager.event.EventListener;

public class EventPush extends EventArgument {
    @Override
    public void call(EventListener listener) {
        listener.onPush(this);
    }
}
