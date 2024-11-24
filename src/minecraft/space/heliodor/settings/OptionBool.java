package space.heliodor.settings;

public class OptionBool extends Option {
    public boolean isEnabled;

    public OptionBool(String name, boolean isEnabled) {
        this.name = name;
        this.isEnabled = isEnabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public void toggle() {
        this.isEnabled = !this.isEnabled;
    }
}
