package space.heliodor.module.impl.player;

import net.minecraft.network.play.server.S02PacketChat;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventRecievePacket;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.settings.OptionMode;
import space.heliodor.utils.SendMessage;

public class AutoJoin extends Module {
    private static final OptionMode mode = new OptionMode("Mode", "Solo Insane", "Solo Insane", "Solo Normal");
    public AutoJoin() {
        super("AutoJoin",0, Category.PLAYER);
        this.options.add(mode);
    }
    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof EventRecievePacket) {
            if(((EventRecievePacket) event).getPacket() instanceof S02PacketChat) {
                String message = ((S02PacketChat) ((EventRecievePacket) event).getPacket()).getChatComponent().getUnformattedText();
                if(message.contains("play again")) {
                    switch (mode.name()) {
                        case "Solo Insane": {
                            mc.thePlayer.sendChatMessage("/play solo_insane");
                            break;
                        }
                        case "Solo Normal" : {
                            mc.thePlayer.sendChatMessage("/play solo_normal");
                            break;
                        }
                    }
                    SendMessage.log("Teleported to new game!");
                }
            }
        }
    }
}
