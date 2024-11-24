package space.heliodor.event;

public class Event {
    public boolean isCancelled;

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }
}
