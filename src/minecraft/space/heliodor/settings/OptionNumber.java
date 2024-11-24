package space.heliodor.settings;

public class OptionNumber extends Option {
    public double value, minimum, maximum, add;

    public OptionNumber(String name, double value, double minimum, double maximum, double add) {
        this.name = name;
        this.value = value;
        this.maximum = maximum;
        this.minimum = minimum;
        this.add = add;
    }

    public double getVal() {
        return this.value;
    }

    public void add(boolean bool) {
        this.value = (double) (getVal() + (bool ? 1 : -1) * add);
    }

    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    public double getMinimum() {
        return this.minimum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    public double getMaximum() {
        return this.maximum;
    }

    public double getAdd() {
        return this.add;
    }

    public void setAdd(double inc) {
        this.add = inc;
    }
}

