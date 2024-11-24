package space.heliodor.module.configs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import space.heliodor.Heliodor;
import space.heliodor.module.Module;
import space.heliodor.settings.Option;
import space.heliodor.settings.OptionBool;
import space.heliodor.settings.OptionMode;
import space.heliodor.settings.OptionNumber;

public class ConfigSystem {
    private File dir, configFile;
    public ConfigSystem() {
        dir = new File(Minecraft.getMinecraft().mcDataDir, "Heliodor");
        if(!dir.exists())
            dir.mkdir();
        configFile = new File(dir, "cfg.txt");
        if(!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void save() throws IOException {
        PrintWriter pw = new PrintWriter(this.configFile);
        for(Module mod : Heliodor.INSTANCE().moduleMgr.getModules()) {
            pw.println("MOD:" + mod.name + ":" + mod.isToggled() + ":" + mod.getKey());
            for (Option set : Heliodor.INSTANCE().moduleMgr.getSettingsByModule(mod)) {
                if(set instanceof OptionBool)
                    pw.println("SET:" + set.name + ":" + mod.name + ":" + ((OptionBool) set).isEnabled());
                else if(set instanceof OptionMode)
                    pw.println("SET:" + set.name + ":" + mod.name + ":" + ((OptionMode) set).name());
                else if (set instanceof OptionNumber)
                    pw.println("SET:" + set.name + ":" + mod.name + ":" + ((OptionNumber) set).getVal());
            }
        }
        pw.close();
    }
    public void load() {
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.configFile));
            String line = reader.readLine();
            while(line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        for(String s : lines) {
            String[] args = s.split(":");
            switch (args[0].toLowerCase()) {
                case "mod": {
                    Module m = Heliodor.INSTANCE().moduleMgr.getModuleByName(args[1]);
                    if (m == null) break;
                    m.isToggled = Boolean.parseBoolean(args[2]);
                    m.key = Integer.parseInt(args[3]);
                    break;
                }
                case "set": {
                    Module m = Heliodor.INSTANCE().moduleMgr.getModuleByName(args[2]);
                    if (m == null) break;
                    Option set = Heliodor.INSTANCE().moduleMgr.getSettingByName(args[1]);
                    if (set == null) break;
                    if (set instanceof OptionBool)
                        ((OptionBool) set).isEnabled = Boolean.parseBoolean(args[3]);
                    else if (set instanceof OptionMode)
                        ((OptionMode) set).setName(args[3]);
                    else if (set instanceof OptionNumber)
                        ((OptionNumber) set).value = Double.parseDouble(args[3]);
                    break;
                }
                default:
                    continue;
            }
        }
    }
}
