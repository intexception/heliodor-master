package space.heliodor.module.impl.movement;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Util;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import space.heliodor.Heliodor;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.*;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.settings.OptionNumber;
import space.heliodor.utils.SendMessage;
import space.heliodor.utils.Stopwatch;
import space.heliodor.utils.movement.Utils;

import java.awt.*;
import java.util.SplittableRandom;

public class Longjump extends Module {
    private static final OptionNumber speed = new OptionNumber("Speed", 2, 1f, 9, 1);
    public Longjump() {
        super("Longjump", Keyboard.KEY_G, Category.MOVEMENT);
        this.options.add(speed);
    }
    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
    }
    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            this.setDisplayName("Longjump " + EnumChatFormatting.GRAY.toString() + "AAC");
            if(mc.thePlayer.onGround) {
                mc.thePlayer.jump();
            }
            mc.thePlayer.isAirBorne = true;
            mc.thePlayer.motionY += 0.067f;
            if(!(mc.thePlayer.fallDistance > 0)) {
                mc.thePlayer.motionX *= 1.0965;
                mc.thePlayer.motionZ *= 1.0965;
            }
        }
    }
}
