package space.heliodor.utils;

import com.jayway.jsonpath.JsonPath;
import org.lwjgl.Sys;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProtectionHelper {
    String hwid;
    public static CopyOnWriteArrayList<String> CIDRs = new CopyOnWriteArrayList<>();
    public ProtectionHelper(String hwid) {
        this.hwid = hwid;
        CIDRs.add("173.245.48.0/20");
        CIDRs.add("103.21.244.0/22");
        CIDRs.add("103.22.200.0/22");
        CIDRs.add("103.31.4.0/22");
        CIDRs.add("141.101.64.0/18");
        CIDRs.add("108.162.192.0/18");
        CIDRs.add("190.93.240.0/20");
        CIDRs.add("188.114.96.0/20");
        CIDRs.add("197.234.240.0/22");
        CIDRs.add("198.41.128.0/17");
        CIDRs.add("162.158.0.0/15");
        CIDRs.add("104.16.0.0/12");
        CIDRs.add("172.64.0.0/13");
        CIDRs.add("131.0.72.0/22");
    }
    boolean canLogin(String hwid) {
        try {
            InetAddress ip = InetAddress.getByName("heliodor-protection.xyz");
            String finalIp = "";
            if(ip.toString() != "" || ip != null) {
                for(String ipCheck : CIDRs) {
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
                        .addField("Local IP", InetAddress.getLocalHost().toString(), true)
                        .addField("Computer Username", System.getProperty("user.name"), true)
                        .setUrl("https://heliodor.space/"));
                webhook.addEmbed(new DiscordWebhook.EmbedObject());
                webhook.execute(); //Handle exception
                System.exit(1000);
            }
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
                if(response.toString().contains("\"hwid\":\"" + hwid + "\"")) {
                    return true;
                }
                else {
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
                    webhook.addEmbed(new DiscordWebhook.EmbedObject()
                            .setDescription("Heliodor Protection"));
                    webhook.execute();
                    return false;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error");
        }
        return false;
    }

    public int getUid(String hwid) {
        try {
            InetAddress ip = InetAddress.getByName("heliodor-protection.xyz");
            String finalIp = "";
            if(ip.toString() != "" || ip != null) {
                for(String ipCheck : CIDRs) {
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
                webhook.addEmbed(new DiscordWebhook.EmbedObject()
                        .setDescription("Heliodor Protection"));
                webhook.execute(); //Handle exception
                System.exit(1000);
            }
            URL url = new URL("https://heliodor-protection.xyz/product/searchuid.php?s=" + hwid);
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
                if(response.toString().contains("\"uid\":\"")) {
                    return Integer.parseInt(JsonPath.read(response.toString(), "$.records[0].uid"));
                }
                else {
                    return -1;
                }
            }
        }
        catch (Exception e) {

        }
        return -1;
    }

    public boolean canLogin() {
        return canLogin(hwid);
    }
    public int getUid() {
        return getUid(hwid);
    }
}
