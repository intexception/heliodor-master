package optifine;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.HashMap;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.apache.commons.io.FilenameUtils;

public class CapeUtils
{
//    public static void downloadCape(final AbstractClientPlayer p_downloadCape_0_)
//    {
//        String s = p_downloadCape_0_.getNameClear();
//
//        if (s != null && !s.isEmpty())
//        {
//            String s1 = "http://s.optifine.net/capes/" + s + ".png";
//            String s2 = FilenameUtils.getBaseName(s1);
//            final ResourceLocation resourcelocation = new ResourceLocation("capeof/" + s2);
//            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
//            ITextureObject itextureobject = texturemanager.getTexture(resourcelocation);
//
//            if (itextureobject != null && itextureobject instanceof ThreadDownloadImageData)
//            {
//                ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)itextureobject;
//
//                if (threaddownloadimagedata.imageFound != null)
//                {
//                    if (threaddownloadimagedata.imageFound.booleanValue())
//                    {
//                        p_downloadCape_0_.setLocationOfCape(resourcelocation);
//                    }
//
//                    return;
//                }
//            }
//
//            IImageBuffer iimagebuffer = new IImageBuffer()
//            {
//                ImageBufferDownload ibd = new ImageBufferDownload();
//                public BufferedImage parseUserSkin(BufferedImage image)
//                {
//                    return CapeUtils.parseCape(image);
//                }
//                public void skinAvailable()
//                {
//                    p_downloadCape_0_.setLocationOfCape(resourcelocation);
//                }
//            };
//            ThreadDownloadImageData threaddownloadimagedata1 = new ThreadDownloadImageData((File)null, s1, (ResourceLocation)null, iimagebuffer);
//            threaddownloadimagedata1.pipeline = true;
//            texturemanager.loadTexture(resourcelocation, threaddownloadimagedata1);
//        }
//    }

    public static ResourceLocation ofLocationCape;

    public static void downloadCape(String username) {
        if (username != null && !username.isEmpty()) {
            username = StringUtils.stripControlCodes(username);
            String ofCapeUrl = "https://minecraftcapes.net/images/capes/special/NewMojang.png";
            MinecraftProfileTexture mpt = new MinecraftProfileTexture(ofCapeUrl, new HashMap());
            final ResourceLocation rl = new ResourceLocation("capeof/" + mpt.getHash());
            IImageBuffer iib = new IImageBuffer() {
                ImageBufferDownload ibd = new ImageBufferDownload();

                public BufferedImage parseUserSkin(BufferedImage var1) {
                    return parseCape(var1);
                }

                public void skinAvailable() {
                    CapeUtils.ofLocationCape = rl;
                }
            };
            ThreadDownloadImageData textureCape = new ThreadDownloadImageData((File) null, mpt.getUrl(), (ResourceLocation) null, iib);
            Minecraft.getMinecraft().getTextureManager().loadTexture(rl, textureCape);
        }
    }

    private static BufferedImage parseCape(BufferedImage img) {
        int imageWidth = 64;
        int imageHeight = 32;
        int srcWidth = img.getWidth();

        for (int srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageHeight *= 2) {
            imageWidth *= 2;
        }

        BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
        Graphics g = imgNew.getGraphics();
        g.drawImage(img, 0, 0, (ImageObserver) null);
        g.dispose();
        return imgNew;
    }
}
