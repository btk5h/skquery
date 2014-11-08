package com.w00tmast3r.skquery.util.minecraft;

import com.w00tmast3r.skquery.util.Reflection;
import org.bukkit.Achievement;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.com.google.gson.stream.JsonWriter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class JSONMessage {

    private final List<MessagePart> messageParts;
    private String jsonString;
    private boolean dirty;

    private Class<?> nmsChatSerializer = Reflection.nmsClass("ChatSerializer");
    private Class<?> nmsTagCompound = Reflection.nmsClass("NBTTagCompound");
    private Class<?> nmsPacketPlayOutChat = Reflection.nmsClass("PacketPlayOutChat");
    private Class<?> nmsAchievement = Reflection.nmsClass("Achievement");
    private Class<?> nmsStatistic = Reflection.nmsClass("Statistic");
    private Class<?> nmsItemStack = Reflection.nmsClass("ItemStack");

    private Class<?> obcStatistic = Reflection.obcClass("CraftStatistic");
    private Class<?> obcItemStack = Reflection.obcClass("inventory.CraftItemStack");

    public JSONMessage(final String firstPartText) {
        messageParts = new ArrayList<>();
        messageParts.add(new MessagePart(firstPartText));
        jsonString = null;
        dirty = false;
    }

    public JSONMessage color(final ChatColor color) {
        if (!color.isColor()) {
            throw new IllegalArgumentException(color.name() + " is not a color");
        }
        latest().color = color;
        dirty = true;
        return this;
    }

    public JSONMessage style(final ChatColor... styles) {
        for (final ChatColor style : styles) {
            if (!style.isFormat()) {
                throw new IllegalArgumentException(style.name() + " is not a style");
            }
        }
        latest().styles = styles;
        dirty = true;
        return this;
    }

    public JSONMessage file(final String path) {
        onClick("open_file", path);
        return this;
    }

    public JSONMessage link(final String url) {
        onClick("open_url", url);
        return this;
    }

    public JSONMessage suggest(final String command) {
        onClick("suggest_command", command);
        return this;
    }

    public JSONMessage command(final String command) {
        onClick("run_command", command);
        return this;
    }

    public JSONMessage achievementTooltip(final String name) {
        onHover("show_achievement", "achievement." + name);
        return this;
    }

    public JSONMessage achievementTooltip(final Achievement which) {
        try {
            Object achievement = Reflection.getMethod(obcStatistic, "getNMSAchievement").invoke(null, which);
            return achievementTooltip((String) Reflection.getField(nmsAchievement, "name").get(achievement));
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    public JSONMessage itemTooltip(final String itemJSON) {
        onHover("show_item", itemJSON);
        return this;
    }

    public JSONMessage itemTooltip(final ItemStack itemStack) {
        try {
            Object nmsItem = Reflection.getMethod(obcItemStack, "asNMSCopy", ItemStack.class).invoke(null, itemStack);
            return itemTooltip(Reflection.getMethod(nmsItemStack, "save").invoke(nmsItem, nmsTagCompound.newInstance()).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }
    /*
    public JSONMessage tooltip(final String text) {
        return tooltip(text.split("\\n"));
    }

    public JSONMessage tooltip(final List<String> lines) {
        return tooltip((String[])lines.toArray());
    }
    */

    public JSONMessage tooltip(final String... lines) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < lines.length; i++){
            builder.append(lines[i]);
            if(i != lines.length - 1){
                builder.append('\n');
            }
        }
        onHover("show_text", builder.toString());
        return this;
    }

    public JSONMessage then(final Object obj) {
        messageParts.add(new MessagePart(obj.toString()));
        dirty = true;
        return this;
    }

    public String toJSONString() {
        if (!dirty && jsonString != null) {
            return jsonString;
        }
        StringWriter string = new StringWriter();
        JsonWriter json = new JsonWriter(string);
        try {
            if (messageParts.size() == 1) {
                latest().writeJson(json);
            } else {
                json.beginObject().name("text").value("").name("extra").beginArray();
                for (final MessagePart part : messageParts) {
                    part.writeJson(json);
                }
                json.endArray().endObject();
                json.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("invalid message");
        }
        jsonString = string.toString();
        dirty = false;
        return jsonString;
    }

    public void send(Player... players) {
        try {
            Object serialized = Reflection.getMethod(nmsChatSerializer, "a", String.class).invoke(null, toJSONString());
            Object packet = nmsPacketPlayOutChat.getConstructor(Reflection.nmsClass("IChatBaseComponent")).newInstance(serialized);
            Reflection.sendPacket(packet, players);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toOldMessageFormat() {
        StringBuilder result = new StringBuilder();
        for (MessagePart part : messageParts) {
            result.append(part.color).append(part.text);
        }
        return result.toString();
    }

    private MessagePart latest() {
        return messageParts.get(messageParts.size() - 1);
    }
    /*
    private String makeMultilineTooltip(final String[] lines) {
        StringWriter string = new StringWriter();
        JsonWriter json = new JsonWriter(string);
        try {
            json.beginObject().name("id").value(1);
            json.name("tag").beginObject().name("display").beginObject();
            json.name("Name").value("\\u00A7f" + lines[0].replace("\"", "\\\""));
            json.name("Lore").beginArray();
            for (int i = 1; i < lines.length; i++) {
                final String line = lines[i];
                json.value(line.isEmpty() ? " " : line.replace("\"", "\\\""));
            }
            json.endArray().endObject().endObject().endObject();
            json.close();
        } catch (Exception e) {
            throw new RuntimeException("invalid tooltip");
        }
        return string.toString();
    }
    */

    private void onClick(final String name, final String data) {
        final MessagePart latest = latest();
        latest.clickActionName = name;
        latest.clickActionData = data;
        dirty = true;
    }

    private void onHover(final String name, final String data) {
        final MessagePart latest = latest();
        latest.hoverActionName = name;
        latest.hoverActionData = data;
        dirty = true;
    }

}