package com.w00tmast3r.skquery.elements.virtualchests.v2;

import com.w00tmast3r.skriptaddon.skriptplus.SkriptPlus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FormattedSlotManager implements Listener {

    private static final HashMap<UUID, HashMap<Integer, SlotRule>> playerRules = new HashMap<UUID, HashMap<Integer, SlotRule>>();
    private static final List<UUID> exempt = new ArrayList<UUID>();

    public static HashMap<Integer, SlotRule> getRules(Player p) {
        return playerRules.containsKey(p.getUniqueId()) ? playerRules.get(p.getUniqueId()) : new HashMap<Integer, SlotRule>();
    }

    public static void setRules(Player p, HashMap<Integer, SlotRule> slotRules) {
        playerRules.put(p.getUniqueId(), slotRules);
    }

    public static void exemptNextClose(Player p) {
        exempt.add(p.getUniqueId());
    }

    public static void addRule(Player player, int slot, SlotRule rule) {
        if (!playerRules.containsKey(player.getUniqueId())) {
            playerRules.put(player.getUniqueId(), new HashMap<Integer, SlotRule>());
        }
        playerRules.get(player.getUniqueId()).put(slot, rule);
    }

    public static void removeRule(Player player, int slot) {
        if (!playerRules.containsKey(player.getUniqueId())) {
            playerRules.put(player.getUniqueId(), new HashMap<Integer, SlotRule>());
        }
        playerRules.get(player.getUniqueId()).remove(slot);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        playerRules.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        final Player p = (Player) event.getWhoClicked();
        if (event.isShiftClick() && playerRules.get(p.getUniqueId()) != null && playerRules.get(p.getUniqueId()).size() > 0) event.setCancelled(true);
        if (playerRules.containsKey(p.getUniqueId()) && event.getSlotType() == InventoryType.SlotType.CONTAINER && playerRules.get(p.getUniqueId()).get(event.getSlot()) != null) {
            event.setCancelled(true);
            SlotRule rule = playerRules.get(p.getUniqueId()).get(event.getSlot());
            rule.run();
            if (rule.willClose()) {
                Bukkit.getScheduler().runTaskLater(SkriptPlus.me.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        p.getOpenInventory().close();
                    }
                }, 1);
            }
        }
    }

    @EventHandler
    public void onClose(final InventoryCloseEvent event) {
        if (exempt.contains(event.getPlayer().getUniqueId())) {
            exempt.remove(event.getPlayer().getUniqueId());
            return;
        }
        Bukkit.getScheduler().runTaskLater(SkriptPlus.me.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (playerRules.get(event.getPlayer().getUniqueId()) != null) playerRules.get(event.getPlayer().getUniqueId()).clear();
            }
        }, 1);
    }
}
