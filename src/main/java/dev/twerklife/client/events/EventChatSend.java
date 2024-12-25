package dev.twerklife.client.events;

import dev.twerklife.api.manager.event.EventArgument;
import dev.twerklife.api.manager.event.EventListener;

public class EventChatSend extends EventArgument {
    private final String message;

    public EventChatSend(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void call(EventListener listener) {
        listener.onChatSend(this);
    }
}
