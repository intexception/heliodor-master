package space.heliodor.module.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import org.lwjgl.input.Mouse;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventUpdate;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.settings.OptionNumber;
import space.heliodor.utils.Stopwatch;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Spammer extends Module {
    private static final OptionNumber delay = new OptionNumber("Delay", 2000, 0, 5000, 1000);
    private static Stopwatch stopwatch = new Stopwatch();
    private static ArrayList<Entity> spamEntities = new ArrayList<>();
    private static ArrayList<Entity> blacklistedEntities = new ArrayList<>();
    public Spammer() {
        super("Spammer",0, Category.PLAYER);
        this.options.add(delay);
    }

    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            File file = new File(Minecraft.getMinecraft().mcDataDir + "\\Heliodor\\Spammer.txt");
            File dir = new File(Minecraft.getMinecraft().mcDataDir + "\\Heliodor");
            String line = "";
            try {
                if(!file.exists() || !dir.exists()) {
                    try {
                        if(dir.mkdir()) {
                            System.out.println("Created directory.");
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        file.createNewFile();
                        Writer output = new BufferedWriter(new FileWriter(file, true));
                        output.append("Heliodor.space | Najlepszy hacked client! | discord.io/heliodor");
                        System.out.println("Created file.");
                        output.close();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                BufferedReader reader = new BufferedReader(new FileReader(file));
                line = reader.readLine();
                reader.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if(stopwatch.elapsed((long) delay.getVal())) {
                System.out.println(line);
                if(line != null && line != "") {
                    if(line.contains("%random%")) {
                        int bypass = 1000 + new Random().nextInt(9000);
                        mc.thePlayer.sendChatMessage(line.replace("%random%", ">" + bypass + "<"));
                    }
                    else {
                        mc.thePlayer.sendChatMessage(line);
                    }
                }
                stopwatch.reset();
            }
        }
    }
}
