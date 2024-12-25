package dev.twerklife.client.events;

import dev.twerklife.api.manager.event.EventArgument;
import dev.twerklife.api.manager.event.EventListener;

public class EventLogout extends EventArgument {
    @Override
    public void call(EventListener listener) {
        listener.onLogout(this);
    }
}
