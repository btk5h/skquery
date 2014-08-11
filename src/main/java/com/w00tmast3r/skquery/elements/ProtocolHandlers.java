package com.w00tmast3r.skquery.elements;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.w00tmast3r.skquery.SkQuery;
import com.w00tmast3r.skquery.api.AbstractTask;
import com.w00tmast3r.skquery.api.Dependency;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

@Dependency("ProtocolLib")
public class ProtocolHandlers extends AbstractTask {
    @Override
    public void run() {
        Bukkit.getScheduler().runTaskLater(SkQuery.getInstance(), new Runnable() {
            @Override
            public void run() {
                ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(new PacketAdapter.AdapterParameteters().plugin(SkQuery.getInstance()).serverSide().listenerPriority(ListenerPriority.HIGH).types(PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS)) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (event.getPacketType() == PacketType.Play.Server.SET_SLOT) {
                            addGlow(event.getPacket().getItemModifier().read(0));
                        } else {
                            addGlow(event.getPacket().getItemArrayModifier().read(0));
                        }
                    }
                });

            }
        }, 1);
    }

    private void addGlow(ItemStack...stacks) {
        for (ItemStack stack : stacks) {
            if (stack != null) {
                if (stack.getEnchantmentLevel(Enchantment.ARROW_INFINITE) == 69
                        || stack.getEnchantmentLevel(Enchantment.WATER_WORKER) == 69) {
                    NbtCompound compound = (NbtCompound) NbtFactory.fromItemTag(stack);
                    compound.put(NbtFactory.ofList("ench"));
                }
            }
        }
    }
}
