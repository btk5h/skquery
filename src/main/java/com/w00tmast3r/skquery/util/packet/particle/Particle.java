package com.w00tmast3r.skquery.util.packet.particle;

import com.w00tmast3r.skquery.util.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class Particle {

    private float xO = 0;
    private float yO = 0;
    private float zO = 0;
    private float data = 0;
    private int amount = 1;
    private final String particle;

    public Particle() {
        particle = "";
    }

    public Particle(String particle) {
        this.particle = particle;
    }

    public Particle(ParticleType particle) {
        this.particle = particle.getId();
    }

    public Particle(ParticleTypes particle) {
        this.particle = particle.getId();
    }

    public void setXOffset(float xO) {
        this.xO = xO;
    }

    public void setYOffset(float yO) {
        this.yO = yO;
    }

    public void setZOffset(float zO) {
        this.zO = zO;
    }

    public void setData(float data) {
        this.data = data;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void play(Location loc, Player... players) {
        try {
            Object packet = Reflection.newFromNMS("PacketPlayOutWorldParticles");
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
            Reflection.sendPacket(packet, players == null ? Bukkit.getOnlinePlayers() : players);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
