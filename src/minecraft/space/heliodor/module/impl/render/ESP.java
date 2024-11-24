package space.heliodor.module.impl.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.Event3D;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.settings.OptionMode;

import java.awt.*;

public class ESP extends Module {
    private static final OptionMode mode = new OptionMode("Mode", "CSGO", "CSGO", "Rat");
    public ESP() {
        super("ESP",0, Category.RENDER);
        this.options.add(mode);
    }

    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof Event3D) {
            for(Object e : mc.theWorld.loadedEntityList) {
                if(e instanceof EntityPlayer) {
                    EntityPlayer player = mc.thePlayer;
                    if(!(e == player) && !player.isSpectator()) {
                        switch (mode.name()) {
                            case "CSGO": {
                                double x = (((EntityPlayer) e).lastTickPosX + (((EntityPlayer) e).posX - ((EntityPlayer) e).lastTickPosX) * ((Event3D) event).partialTicks);
                                double y = (((EntityPlayer) e).lastTickPosY + (((EntityPlayer) e).posY - ((EntityPlayer) e).lastTickPosY) * ((Event3D) event).partialTicks);
                                double z = (((EntityPlayer) e).lastTickPosZ + (((EntityPlayer) e).posZ - ((EntityPlayer) e).lastTickPosZ) * ((Event3D) event).partialTicks);
                                drawBox((EntityLivingBase) e,x - RenderManager.renderPosX, y - RenderManager.renderPosY, z - RenderManager.renderPosZ);
                                break;
                            }
                            case "Rat": {
                                double x = (((EntityPlayer) e).lastTickPosX + (((EntityPlayer) e).posX - ((EntityPlayer) e).lastTickPosX) * ((Event3D) event).partialTicks);
                                double y = (((EntityPlayer) e).lastTickPosY + (((EntityPlayer) e).posY - ((EntityPlayer) e).lastTickPosY) * ((Event3D) event).partialTicks);
                                double z = (((EntityPlayer) e).lastTickPosZ + (((EntityPlayer) e).posZ - ((EntityPlayer) e).lastTickPosZ) * ((Event3D) event).partialTicks);
                                drawRat((EntityLivingBase) e,x - RenderManager.renderPosX, y - RenderManager.renderPosY, z - RenderManager.renderPosZ);
                                break;
                            }
                        }
                    }
                }
            }
        }

    }

    public void drawRat(EntityLivingBase entity, double posX, double posY, double posZ) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-0.01, -0.01, 0.01);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        mc.getTextureManager().bindTexture(new ResourceLocation("rat.png"));
        Gui.drawModalRectWithCustomSizedTexture(-200,-275,0,0,300,300, 326, 300);
        GL11.glEnable(2929);
        GL11.glEnable(2896);
        GlStateManager.popMatrix();
    }

    public void drawBox(EntityLivingBase entity, double posX, double posY, double posZ) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-0.1, -0.1, 0.1);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        // obramowka na zewnatrz
        Gui.drawRect(-6.75f, -20f, -7.1f, 2.15f, new Color(0,0,0,255).getRGB());
        Gui.drawRect(6.75f, -20.f, 7.1f, 2.15f, new Color(0,0,0,255).getRGB());
        Gui.drawRect(-7.1f, 2.2f, 7.1f, 1.875f, new Color(0,0,0,255).getRGB());
        Gui.drawRect(-7.1f, -20f, 7.1f, -19.65f, new Color(0,0,0,255).getRGB());

        Gui.drawRect(-7, -19.9f, -6.85f, 2f, -1);
        Gui.drawRect(7, -19.9f, 6.85f, 2f, -1);
        Gui.drawRect(-7, 2.1f, 7f, 2f, -1);
        Gui.drawRect(-7, -19.9f, 6.9f, -19.75f, -1);

        Gui.drawRect(7.9f, -20.1f, 8.35f, 2.1f, new Color(0, 0,0,255).getRGB());
        Gui.drawRect(8, -entity.getHealth(), 8.25f, 2, new Color(0,255,0,255).getRGB());
        GlStateManager.scale(0.1, 0.1, 0.1);
        mc.fontRendererObj.drawStringWithShadow(Math.round(entity.getHealth()) + "❤", 80 - mc.fontRendererObj.getStringWidth(Math.round(entity.getHealth()) + "❤") / 2 + 15, (int) (-entity.getHealth() * 10), -1);
        mc.fontRendererObj.drawStringWithShadow(entity.getName(), 0 - mc.fontRendererObj.getStringWidth(entity.getName()) / 2, -210, -1);
        GL11.glEnable(2929);
        GL11.glEnable(2896);
        GlStateManager.popMatrix();
    }
}
