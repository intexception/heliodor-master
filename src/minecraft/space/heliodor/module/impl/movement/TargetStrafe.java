package space.heliodor.module.impl.movement;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import space.heliodor.Heliodor;
import space.heliodor.event.Event;
import space.heliodor.event.OnEvent;
import space.heliodor.event.impl.Event3D;
import space.heliodor.module.Category;
import space.heliodor.module.Module;
import space.heliodor.module.impl.combat.KillAura;
import space.heliodor.settings.OptionNumber;

import static org.lwjgl.opengl.GL11.*;

public class TargetStrafe extends Module {
    public static final OptionNumber range = new OptionNumber("Range", 2.5f, 0.1f, 5, 0.1f);
    public static KillAura killAura = new KillAura();
    public TargetStrafe() {
        super("TargetStrafe",0, Category.MOVEMENT);
        this.options.add(range);
        this.setDisplayName("TargetStrafe" + EnumChatFormatting.GRAY.toString() + " Adaptive");
    }

    @OnEvent
    public void onEvent(Event event) {
        if(event instanceof Event3D) {
            KillAura killAura = (KillAura) Heliodor.INSTANCE().moduleMgr.getModuleByName("KillAura");
            if(Heliodor.INSTANCE().moduleMgr.getModuleByName("KillAura").isToggled() && killAura.getTarget() != null) {
                EntityLivingBase entity = killAura.target;
                if(entity != null) {
                    GL11.glPushMatrix();
                    glDisable(GL_TEXTURE_2D);
                    glEnable(GL_LINE_SMOOTH);
                    glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
                    glDisable(GL_DEPTH_TEST);
                    glLineWidth(1.5f);
                    glEnable(GL_BLEND);
                    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                    glDisable(GL_DEPTH_TEST);
                    glBegin(GL_LINE_STRIP);
                    final double x = entity.prevPosX + (entity.posX - entity.prevPosX) * ((Event3D) event).partialTicks - RenderManager.viewerPosX;
                    final double y = entity.prevPosY + (entity.posY - entity.prevPosY) * ((Event3D) event).partialTicks - RenderManager.viewerPosY;
                    final double z = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * ((Event3D) event).partialTicks - RenderManager.viewerPosZ;
                    glColor3d(1f,1f,1f);
                    for (int i = 0; i < 36; i++)
                        glVertex3d(x + range.getVal() * Math.cos(i * Math.PI * 2 / 34), y, z + range.getVal() * Math.sin(i * Math.PI * 2 / 34));
                    glEnd();
                    glDepthMask(true);
                    glDisable(GL_BLEND);
                    glEnable(GL_DEPTH_TEST);
                    glDisable(GL_LINE_SMOOTH);
                    glEnable(GL_DEPTH_TEST);
                    glEnable(GL_TEXTURE_2D);
                    glPopMatrix();
                }
            }
        }
    }
}
