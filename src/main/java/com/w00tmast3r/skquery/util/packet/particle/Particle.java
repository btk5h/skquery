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
                switch (fieldName) {
                    case "a":
                        field.set(packet, particle);
                        break;
                    case "b":
                        field.setFloat(packet, (float) loc.getX());
                        break;
                    case "c":
                        field.setFloat(packet, (float) loc.getY());
                        break;
                    case "d":
                        field.setFloat(packet, (float) loc.getZ());
                        break;
                    case "e":
                        field.setFloat(packet, xO);
                        break;
                    case "f":
                        field.setFloat(packet, yO);
                        break;
                    case "g":
                        field.setFloat(packet, zO);
                        break;
                    case "h":
                        field.setFloat(packet, data);
                        break;
                    case "i":
                        field.setInt(packet, amount);
                        break;
                }
            }
            Reflection.sendPacket(packet, players == null ? Bukkit.getOnlinePlayers() : players);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
