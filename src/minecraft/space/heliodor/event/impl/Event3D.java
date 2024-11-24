package space.heliodor.event.impl;

import space.heliodor.event.Event;

public class Event3D extends Event {
    public float partialTicks;
    public Event3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
