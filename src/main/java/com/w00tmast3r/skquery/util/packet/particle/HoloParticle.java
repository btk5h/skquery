package com.w00tmast3r.skquery.util.packet.particle;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class HoloParticle extends Particle {

    private final String name;

    public HoloParticle(String name) {
        this.name = name;
    }

    @Override
    public void play(Location loc, Player... players) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
        StructureModifier<Integer> intData = packet.getIntegers();
        WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
        dataWatcher.setObject(10, name);
        dataWatcher.setObject(12, -1700000);
        packet.getDataWatcherModifier().write(0, dataWatcher);
        intData.write(0, new Random().nextInt(1000));
        intData.write(1, 100);
        intData.write(2, loc.getBlockX());
        intData.write(3, loc.getBlockY());
        intData.write(4, loc.getBlockZ());
        for (Player p : players) {
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
