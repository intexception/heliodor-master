package space.heliodor.module.impl.player;

import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.EventUpdate;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.utils.SendMessage;

public class Speedmine extends Module {
    public Speedmine() {
        super("Speedmine", 0, Category.PLAYER);
    }

    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            if (mc.playerController.curBlockDamageMP == 0.4) mc.playerController.curBlockDamageMP = 0.5f;
            if (mc.playerController.curBlockDamageMP == 0.7) mc.playerController.curBlockDamageMP = 0.8f;
            if (mc.playerController.curBlockDamageMP > 0.825f) mc.playerController.curBlockDamageMP = 1.0f;
            mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 5, 1));
        }
    }
}
