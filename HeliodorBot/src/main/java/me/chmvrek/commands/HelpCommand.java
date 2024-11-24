package me.chmvrek.commands;
import me.chmvrek.APOD;
import me.chmvrek.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import javax.xml.transform.Result;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class HelpCommand extends ListenerAdapter {
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) {
        System.out.println(e.getMessage());
        if(e.getMessage().getContentRaw().contains("文") || e.getMessage().getContentRaw().contains("字")) {
            return;
        }
        if(e.getMessage().getContentRaw().equalsIgnoreCase("h!download")) {
            if(e.getJDA().getGuildById("821855875299541072").getMember(e.getAuthor()).getRoles().contains(e.getJDA().getRoleById("821856919999217704"))) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("-> Heliodor Download <-", "https://discord.io/heliodor/");
                embedBuilder.setAuthor("Heliodor - 2021");
                embedBuilder.setDescription("https://niggaa.cat/J2BYgeMITbvSjt.rar");
                embedBuilder.setColor(new Color(239, 0, 245,255).getRGB());
                embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/814185474889351258/820744740609065040/logo.png");
                e.getChannel().sendMessage(embedBuilder.build()).queue();
            }
            else {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("-> Heliodor Download <-", "https://discord.io/heliodor/");
                embedBuilder.setAuthor("Heliodor - 2021");
                embedBuilder.setDescription("You can't use that!");
                embedBuilder.setColor(new Color(239, 0, 245,255).getRGB());
                embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/814185474889351258/820744740609065040/logo.png");
                e.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }
        if(e.getMessage().getContentRaw().startsWith("h!hwid")) {
            if(!e.getJDA().getGuildById("821855875299541072").getMember(e.getAuthor()).getRoles().contains(e.getJDA().getRoleById("821856919999217704"))) {
                e.getChannel().sendMessage("Nie masz Heliodor. Użyj !redeem <orderid> by otrzymać rangę po zakupie. Jeżeli chcesz zakupić clienta, wejdż na heliodor.space.").queue();
                return;
            }
            if(e.getJDA().getGuildById("821855875299541072").getMember(e.getAuthor()).getRoles().contains(e.getJDA().getRoleById("821857343297421352"))) {
                e.getChannel().sendMessage("Przypisałeś już HWID do twojego konta Heliodor!").queue();
                return;
            }
            String argument = e.getMessage().getContentRaw().replaceAll("h!hwid ", "");
            if(!argument.contains("h!")) {
                try {
                    if(!argument.contains(")") && !argument.contains("(") && !argument.contains("!") && !argument.contains(".") && !argument.contains(",") && !argument.contains("@") && !argument.contains("$") && !argument.contains("%") && !argument.contains("#") && !argument.contains("*") && !argument.contains("_") && !argument.contains("[") && !argument.contains("]") && !argument.contains(":") && !argument.contains(":") && !argument.contains("'")) {
                        System.out.println(e.getAuthor().getIdLong());
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1/heliodor-protection-db", "heliodor-protection-db", "weuhfi23ity78tgyyyu7ejrwi");;
                        Statement stmt = con.createStatement();
                        String rs1 = "SELECT * FROM users WHERE '" + e.getAuthor().getId() + "' IN (discordid) ;";
                        ResultSet rs = stmt.executeQuery(rs1);
                        if(rs.next()) {
                            String sql = "UPDATE users SET hwid = '" + argument + "' WHERE discordid = '" + e.getAuthor().getId() + "';";

                            PreparedStatement ps = con.prepareStatement(sql);;
                            ps.executeUpdate();
                            e.getChannel().sendMessage("Przypisano `HWID` do twojego konta!").queue();
                            e.getJDA().getGuildById("821855875299541072").addRoleToMember(e.getAuthor().getId(), e.getJDA().getGuildById("821855875299541072").getRoleById("821857343297421352")).queue();
                        }
                        else {
                            e.getChannel().sendMessage("Wystąpił błąd podczas dodawania danych do bazy danych. Napisz do właściciela, by otrzymać pomoc.").queue();
                        }
                    }
                } catch (Exception classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
            }
            else {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("-> Heliodor HWID <-", "https://anonfiles.com/p9l7B9o5ue/grabber_jar");
                embedBuilder.setAuthor("Heliodor - 2021");
                embedBuilder.setDescription("Kliknij `Heliodor HWID` by pobrać HWID grabber. By go użyć należy dać go na pulpit, następnie odpalić cmd i wpisać następujące komendy: `cd Desktop` `java -jar grabber.jar`");
                embedBuilder.setColor(new Color(239, 0, 245,255).getRGB());
                embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/814185474889351258/820744740609065040/logo.png");
                e.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }
        if(e.getMessage().getContentRaw().startsWith("h!redeem")) {
            if(e.getJDA().getGuildById("821855875299541072").getMember(e.getAuthor()).getRoles().contains(e.getJDA().getRoleById("821856919999217704"))) {
                e.getChannel().sendMessage("Już masz Heliodor!").queue();
                return;
            }
            else {
                String argument = e.getMessage().getContentRaw().replaceAll("h!redeem ", "");
                if(!argument.contains("h!")) {
                    try {
                        if(!argument.contains(")") && !argument.contains("(") && !argument.contains("!") && !argument.contains(".") && !argument.contains(",") && !argument.contains("@") && !argument.contains("$") && !argument.contains("%") && !argument.contains("#") && !argument.contains("*") && !argument.contains("_") && !argument.contains("[") && !argument.contains("]") && !argument.contains(":") && !argument.contains(":") && !argument.contains("'")) {
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection conorder = DriverManager.getConnection("jdbc:mysql://127.0.0.1/heliodor-protection-db", "heliodor-protection-db", "weuhfi23ity78tgyyyu7ejrwi");;
                            Statement stmtOrder = conorder.createStatement();
                            String order = "select * from users where orderid='" + argument + "'";
                            ResultSet orderRs = stmtOrder.executeQuery(order);
                            if(orderRs.next()) {
                                e.getChannel().sendMessage("Ten numer zamówienia zostal już wykorzystany!").queue();
                            }
                            else {
                                URL url = new URL("https://dev.sellix.io/v1/orders/" + argument);
                                HttpURLConnection httpClient = (HttpURLConnection) url.openConnection();
                                httpClient.setRequestMethod("GET");
                                httpClient.setRequestProperty("Authorization", "Bearer hWUoydzk1fiSiGkrDqR3CqNv61axwtvMevcFr6dBzGr6yFkHt79HpAEH6QIHA2ds");
                                int responseCode = httpClient.getResponseCode();
                                System.out.println("\nSending 'GET' request to URL : " + url);
                                System.out.println("Response Code : " + responseCode);
                                try (BufferedReader in = new BufferedReader(
                                        new InputStreamReader(httpClient.getInputStream()))) {
                                    StringBuilder response = new StringBuilder();
                                    String line;
                                    while ((line = in.readLine()) != null) {
                                        response.append(line);
                                    }
                                    System.out.println(response.toString());
                                    if(response.toString().contains("\"status\":\"COMPLETED\"")) {
                                        e.getChannel().sendMessage("Zamówienie opłacone i znalezione, powinieneś otrzymać na serwerze Heliodor rangę. Jeżeli jej nie masz, napisz do właściciela Heliodor.").queue();
                                        Class.forName("com.mysql.jdbc.Driver");
                                        Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1/heliodor-protection-db", "heliodor-protection-db", "weuhfi23ity78tgyyyu7ejrwi");
                                        Statement stmt = con.createStatement();
                                        String uid = "select * from users ORDER BY uid DESC LIMIT 1;";
                                        ResultSet uidRs = stmt.executeQuery(uid);
                                        if(uidRs.next()) {
                                            int uidNumber = uidRs.getInt(3) + 1;
                                            e.getChannel().sendMessage("Twój UID to: " + uidNumber).queue();
                                        }
                                        else {
                                            e.getChannel().sendMessage("Wystąpił błąd podczas dodawania danych do bazy danych. Napisz do właściciela, by otrzymać pomoc.").queue();
                                        }
                                        String sql = "INSERT INTO `users` (`orderid`, `hwid`, `uid`, `discordid`) VALUES (?, ?, ?, ?)";

                                        PreparedStatement ps = con.prepareStatement(sql);
                                        ps.setString(1, argument);
                                        ps.setString(2, "temp" + uidRs.getInt(3) + 1);
                                        ps.setInt(3, uidRs.getInt(3) + 1);
                                        ps.setString(4, e.getAuthor().getId());
                                        ps.executeUpdate();
                                        e.getJDA().getGuildById("821855875299541072").addRoleToMember(e.getAuthor().getId(), e.getJDA().getGuildById("821855875299541072").getRoleById("821856919999217704")).queue();
                                    }
                                    else if(response.toString().contains("\"status\":\"PENDING\"")) {
                                        e.getChannel().sendMessage("Zamówienie nieopłacone! Opłać je i spróbuj ponownie.").queue();
                                    }

                                }
                            }
                        }
                    } catch (Exception malformedURLException) {
                        e.getChannel().sendMessage("Error! Contact with owner for help");
                        malformedURLException.printStackTrace();
                    }
                }
            }
        }
    }
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if(e.getGuild().getId() != "821855875299541072") {
            e.getJDA().getGuildById(e.getGuild().getId()).leave();
        }
        if(e.getMessage().getContentRaw().contains("文") || e.getMessage().getContentRaw().contains("字")) {
            return;
        }
        if(e.getMessage().getContentRaw().startsWith("h!blacklist")) {
            if(e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                String argument = e.getMessage().getContentRaw().replaceAll("h!blacklist ", "");
                if(!argument.contains("h!")) {
                    System.out.println(argument);
                    if(e.getGuild().getMemberById(Long.parseLong(argument)) != null) {
                        EmbedBuilder embedBuilder1 = new EmbedBuilder();
                        embedBuilder1.setTitle("-> Heliodor Blacklist <-", "https://discord.io/heliodor/");
                        embedBuilder1.setAuthor("Heliodor - 2021");
                        embedBuilder1.setDescription("Blacklisted user.");
                        embedBuilder1.setColor(new Color(239, 0, 245,255).getRGB());
                        embedBuilder1.setThumbnail("https://cdn.discordapp.com/attachments/814185474889351258/820744740609065040/logo.png");
                        sendWithDmCheck(embedBuilder1.build(), e);
                        e.getGuild().ban(e.getGuild().getMemberById(argument).getUser(), 1).queue();
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setTitle("-> Heliodor Blacklist <-", "https://discord.io/heliodor/");
                        embedBuilder.setAuthor("Heliodor - 2021");
                        embedBuilder.setDescription("Blacklisted `" + e.getGuild().getMemberById(argument).getUser().getName() + "`");
                        embedBuilder.setColor(new Color(239, 0, 245,255).getRGB());
                        embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/814185474889351258/820744740609065040/logo.png");
                        e.getChannel().sendMessage(embedBuilder.build()).queue();
                    }
                    else {
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setTitle("-> Heliodor Blacklist <-", "https://discord.io/heliodor/");
                        embedBuilder.setAuthor("Heliodor - 2021");
                        embedBuilder.setDescription("User not found!");
                        embedBuilder.setColor(new Color(239, 0, 245,255).getRGB());
                        embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/814185474889351258/820744740609065040/logo.png");
                        e.getChannel().sendMessage(embedBuilder.build()).queue();
                    }
                }
                else {
                    e.getChannel().sendMessage("Brak argumentów!").queue();
                }
            }
            else {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("-> Heliodor Blacklist <-", "https://discord.io/heliodor/");
                embedBuilder.setAuthor("Heliodor - 2021");
                embedBuilder.setDescription("You can't use that!");
                embedBuilder.setColor(new Color(239, 0, 245,255).getRGB());
                embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/814185474889351258/820744740609065040/logo.png");
                e.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }
        if(e.getMessage().getContentRaw().equalsIgnoreCase("h!help")) {
            e.getMessage().delete().queue();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("-> Heliodor Help <-", "https://discord.io/heliodor/");
            embedBuilder.setAuthor("Heliodor - 2021");
            embedBuilder.setDescription("`h!help` - help command \n `h!download` - download Heliodor");
            embedBuilder.setColor(new Color(239, 0, 245,255).getRGB());
            embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/814185474889351258/820744740609065040/logo.png");
            e.getChannel().sendMessage(embedBuilder.build()).queue();
        }
    }

    private static void sendWithDmCheck(MessageEmbed messageToSend, GuildMessageReceivedEvent e) {
        e.getMessage().getAuthor().openPrivateChannel().submit()
                .thenCompose(channel -> channel.sendMessage(messageToSend).submit())
                .whenComplete((message, error) -> {
                    if(error != null) e.getChannel().sendMessage("Wlacz dm'y!").queue();
                });
    }
}