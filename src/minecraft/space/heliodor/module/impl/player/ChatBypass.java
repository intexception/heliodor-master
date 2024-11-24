package space.heliodor.module.impl.player;

import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.EnumChatFormatting;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventSendPacket;
import space.heliodor.module.Category;
import space.heliodor.module.Module;

public class ChatBypass extends Module {
    public ChatBypass() {
        super("Chat Bypass", 0, Category.PLAYER);
    }

    @OnEvent
    public void onEvent(Event event) {
        this.setDisplayName("Chat Bypass " + EnumChatFormatting.GRAY.toString() + "Emote");
        if(event instanceof EventSendPacket) {
            if(((EventSendPacket) event).getPacket() instanceof C01PacketChatMessage) {
                if(((C01PacketChatMessage) ((EventSendPacket) event).getPacket()).message.startsWith("/"))
                    return;

                StringBuilder message = new StringBuilder();
                char[] charMessage = ((C01PacketChatMessage) ((EventSendPacket) event).getPacket()).message.toCharArray();
                for(int i = 0; i < charMessage.length; i++) {
                    char messagePart = charMessage[i];
                    message.append(messagePart + "\uD83D");
                }
                mc.thePlayer.sendQueue.sendQueueBypass(new C01PacketChatMessage(message.toString().replaceFirst("%", "")));
                event.setCancelled(true);
            }
        }
    }
}
