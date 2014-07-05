package com.w00tmast3r.skquery.elements;

import com.w00tmast3r.skquery.api.AbstractTask;
import com.w00tmast3r.skriptaddon.skaddonlib.util.SimpleEnumClassInfo;
import com.w00tmast3r.skriptaddon.skriptplus.util.MoonPhase;
import com.w00tmast3r.skriptaddon.skriptplus.util.Particle;
import org.bukkit.Art;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.map.MapCursor;
import org.bukkit.scoreboard.DisplaySlot;


public class SimpleTypes extends AbstractTask {

    @Override
    public void run() {
        SimpleEnumClassInfo.create(DisplaySlot.class, "displayslot").register();
        SimpleEnumClassInfo.create(Particle.class, "particle").after("string").register();
        SimpleEnumClassInfo.create(Sound.class, "sound").register();
        SimpleEnumClassInfo.create(FireworkEffect.Type.class, "fireworktype").after("entitytype").register();
        SimpleEnumClassInfo.create(MapCursor.Type.class, "mapcursortype").register();
        SimpleEnumClassInfo.create(InventoryType.class, "inventorytype").after("gamemode", "entitytype").register();
        SimpleEnumClassInfo.create(Villager.Profession.class, "profession").register();
        SimpleEnumClassInfo.create(Art.class, "art").after("damagecause", "entitytype").register();
        SimpleEnumClassInfo.create(MoonPhase.class, "moonphase").register();
    }
}
