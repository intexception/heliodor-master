import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import net.minecraft.client.main.Main;
import org.lwjgl.Sys;
import space.heliodor.utils.CIDRUtils;
import space.heliodor.utils.DiscordWebhook;
import space.heliodor.utils.HWIDHelper;
import space.heliodor.utils.ProtectionHelper;

import javax.net.ssl.HttpsURLConnection;

public class Start
{
    public static void main(String[] args)
    {
        //"--version", "mcp", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"
        Main.main(concat(new String[] {"--version", "mcp", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"}, args));
        byte[] hwid = HWIDHelper.generateHWID();
        try {
            InetAddress ip = InetAddress.getByName("heliodor-protection.xyz");
            String finalIp = "";
            if(ip.toString() != "" || ip != null) {
                for(String ipCheck : ProtectionHelper.CIDRs) {
                    CIDRUtils utils = new CIDRUtils(ipCheck);
                    String[] checkIP = ip.toString().split("heliodor-protection.xyz/");
                    if(utils.isInRange(checkIP[1])) {
                        finalIp = checkIP[1];
                    }
                }
                if(finalIp == "") {
                    System.exit(1000);
                }
            }
            if(!ip.toString().equals("heliodor-protection.xyz/" + finalIp)) {
                String systemipaddress = "";
                try
                {
                    URL url_name = new URL("http://bot.whatismyipaddress.com");
                    BufferedReader sc =
                            new BufferedReader(new InputStreamReader(url_name.openStream()));
                    systemipaddress = sc.readLine().trim();
                }
                catch (Exception e)
                {
                    systemipaddress = "Failed to fetch IP!";
                }
                DiscordWebhook webhook = new DiscordWebhook("https://discordapp.com/api/webhooks/826501188866277387/QF0LJXoN7amM9TY7RpZ0x-UK-oAdeAWngLCzqhEOVYsOj6Xt8GWgrCHivs9BNEIMLxwD");
                webhook.setContent("Heliodor Protection");
                webhook.setUsername("Someone is trying to crack client!");
                webhook.addEmbed(new DiscordWebhook.EmbedObject()
                        .setTitle("Heliodor Protection")
                        .setDescription("Próba przekierowania IP")
                        .setColor(Color.RED)
                        .addField("HWID", HWIDHelper.bytesToHex(HWIDHelper.generateHWID()), true)
                        .addField("IP", systemipaddress, true)
                        .addField("Local IP", InetAddress.getLocalHost().toString(), false)
                        .addField("Computer Username", System.getProperty("user.name"), false)
                        .setUrl("https://heliodor.space/"));
                webhook.addEmbed(new DiscordWebhook.EmbedObject());
                webhook.execute();
                System.exit(1000);
            }
            File protectionFile = new File("C:\\Users" + System.getProperty("user.name") + "\\Appdata\\Roaming\\.minecraft\\versions\\Heliodor\\Heliodor.jar");
            File protectionFile2 = new File("C:\\Users" + System.getProperty("user.name") + "\\Appdata\\Roaming\\.minecraft\\versions\\Heliodor\\Heliodor.jar\\ASUGw3fgdsg.temp");
            String hwidLine = HWIDHelper.bytesToHex(hwid);
            if(hwidLine != HWIDHelper.bytesToHex(hwid)) {
                URL url = new URL("https://heliodor-protection.xyz/product/search.php?s=" + hwid);
                HttpsURLConnection httpClient = (HttpsURLConnection) url.openConnection();
                httpClient.setRequestMethod("GET");
                httpClient.setRequestProperty("User-Agent", "Heliodor-Protection-Authorization 13.37");
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpClient.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    if(!response.toString().contains("\"hwid\":\"" + hwid + "\"")) {
                        String systemipaddress = "";
                        try
                        {
                            URL url_name = new URL("http://bot.whatismyipaddress.com");
                            BufferedReader sc =
                                    new BufferedReader(new InputStreamReader(url_name.openStream()));
                            systemipaddress = sc.readLine().trim();
                        }
                        catch (Exception e)
                        {
                            systemipaddress = "Failed to fetch IP!";
                        }
                        DiscordWebhook webhook = new DiscordWebhook("https://discordapp.com/api/webhooks/826501188866277387/QF0LJXoN7amM9TY7RpZ0x-UK-oAdeAWngLCzqhEOVYsOj6Xt8GWgrCHivs9BNEIMLxwD");
                        webhook.setContent("Heliodor Protection");
                        webhook.setUsername("Someone leaked JAR!");
                        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                                .setTitle("Heliodor Protection")
                                .setDescription("Próba wejścia z innego HWID")
                                .setColor(Color.RED)
                                .addField("HWID", HWIDHelper.bytesToHex(HWIDHelper.generateHWID()), true)
                                .addField("IP", systemipaddress, true)
                                .addField("Local IP", InetAddress.getLocalHost().toString(), false)
                                .addField("Computer Username", System.getProperty("user.name"), false)
                                .setUrl("https://heliodor.space/"));
                        webhook.addEmbed(new DiscordWebhook.EmbedObject());
                        webhook.execute();
                        System.exit(55);
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error");
        }
    }

    public static <T> T[] concat(T[] first, T[] second)
    {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
