package com.w00tmast3r.skquery.util.packet;

import com.w00tmast3r.skquery.util.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public enum Particle {
    HUGE_EXPLOSION("hugeexplosion"),
    LARGE_EXPLODE("largeexplode"),
    FIREWORKS_SPARK("fireworksSpark"),
    BUBBLE("bubble"),
    SUSPENDED("suspended"),
    DEPTH_SUSPEND("depthsuspend"),
    TOWN_AURA("townaura"),
    CRIT("crit"),
    MAGIC_CRIT("magicCrit"),
    SMOKE("smoke"),
    MOB_SPELL("mobSpell"),
    MOB_SPELL_AMBIENT("mobSpellAmbient"),
    SPELL("spell"),
    INSTANT_SPELL("instantSpell"),
    WITCH_MAGIC("witchMagic"),
    NOTE("note"),
    PORTAL("portal"),
    ENCHANTMENT_TABLE("enchantmenttable"),
    EXPLODE("explode"),
    FLAME("flame"),
    LAVA("lava"),
    FOOTSTEP("footstep"),
    SPLASH("splash"),
    LARGE_SMOKE("largesmoke"),
    CLOUD("cloud"),
    RED_DUST("reddust"),
    SNOWBALL_POOF("snowballpoof"),
    DRIP_WATER("dripWater"),
    DRIP_LAVA("dripLava"),
    SNOW_SHOVEL("snowshovel"),
    SLIME("slime"),
    HEART("heart"),
    ANGRY_VILLAGER("angryVillager"),
    HAPPY_VILLAGER("happyVillager");


    private final String id;

    Particle(String id) {
        this.id = id;
    }

    public static void play(String particle, Location loc, int amount, float xO, float yO, float zO, int data) {
        try {
            Object packet = Class.forName("net.minecraft.server." + Reflection.getServerVersion() + ".PacketPlayOutWorldParticles").newInstance();
            for (Field field : packet.getClass().getDeclaredFields()){
                field.setAccessible(true);
                String fieldName = field.getName();
                if (fieldName.equals("a")) {
                    field.set(packet, particle);

                } else if (fieldName.equals("b")) {
                    field.setFloat(packet, (float) loc.getX());

                } else if (fieldName.equals("c")) {
                    field.setFloat(packet, (float) loc.getY());

                } else if (fieldName.equals("d")) {
                    field.setFloat(packet, (float) loc.getZ());

                } else if (fieldName.equals("e")) {
                    field.setFloat(packet, xO);

                } else if (fieldName.equals("f")) {
                    field.setFloat(packet, yO);

                } else if (fieldName.equals("g")) {
                    field.setFloat(packet, zO);

                } else if (fieldName.equals("h")) {
                    field.setFloat(packet, data);

                } else if (fieldName.equals("i")) {
                    field.setInt(packet, amount);

                }
            }
            for(Player player : Bukkit.getServer().getOnlinePlayers()){
                Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
                Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
                Reflection.getMethod(playerConnection.getClass(), "sendPacket", Reflection.nmsClass("Packet")).invoke(playerConnection, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }
}
