package space.heliodor.settings;

import java.util.Arrays;
import java.util.List;

public class OptionMode extends Option {
    public int index;
    public List<String> list;

    public OptionMode(String name, String defaultMode, String... list) {
        this.name = name;
        this.list = Arrays.asList(list);
        this.index = this.list.indexOf(defaultMode);
    }

    public void changeSetting() {
        if(index < list.size() - 1) {
            index++;
        }
        else {
            index = 0;
        }
    }

    public String name() {
        return list.get(index);
    }
    public void setName(String name) {
        list.set(index, name);
    }
}
