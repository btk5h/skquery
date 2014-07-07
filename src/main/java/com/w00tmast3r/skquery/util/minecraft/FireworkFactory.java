package com.w00tmast3r.skquery.util.minecraft;

import com.w00tmast3r.skquery.util.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkFactory {

    private Player[] players = Bukkit.getOnlinePlayers();
    private FireworkEffect[] effects = null;
    private Location loc = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);

    public FireworkFactory() {
    }

    public FireworkFactory players(Player... players) {
        this.players = players;
        return this;
    }

    public FireworkFactory effects(FireworkEffect... effects) {
        this.effects = effects;
        return this;
    }

    public FireworkFactory location(Location loc) {
        this.loc = loc;
        return this;
    }

    public void play() {
        Object packet = constructPacket(loc, effects);
        Reflection.sendPacket(packet, players);
    }

    private static Object constructPacket(Location loc, FireworkEffect... effects) {
        try {
            Firework firework = loc.getWorld().spawn(loc, Firework.class);
            FireworkMeta data = firework.getFireworkMeta();
            data.clearEffects();
            data.setPower(1);
            for (FireworkEffect f : effects) {
                data.addEffect(f);
            }
            firework.setFireworkMeta(data);
            Object nmsFirework = firework.getClass().getMethod("getHandle").invoke(firework);
            firework.remove();
            return Reflection.nmsClass("PacketPlayOutEntityStatus").getConstructor(Reflection.nmsClass("Entity"), byte.class).newInstance(nmsFirework, (byte) 17);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}