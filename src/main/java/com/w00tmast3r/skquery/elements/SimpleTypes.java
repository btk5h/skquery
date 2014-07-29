package com.w00tmast3r.skquery.elements;

import com.w00tmast3r.skquery.api.AbstractTask;
import com.w00tmast3r.skquery.skript.EnumClassInfo;
import com.w00tmast3r.skquery.util.minecraft.MoonPhase;
import com.w00tmast3r.skquery.util.packet.particle.ParticleTypes;
import org.bukkit.Art;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.map.MapCursor;
import org.bukkit.scoreboard.DisplaySlot;


public class SimpleTypes extends AbstractTask {

    @Override
    public void run() {
        EnumClassInfo.create(DisplaySlot.class, "displayslot").register();
        EnumClassInfo.create(ParticleTypes.class, "particletypes").after("string").register();
        EnumClassInfo.create(Sound.class, "sound").register();
        EnumClassInfo.create(FireworkEffect.Type.class, "fireworktype").after("entitytype").register();
        EnumClassInfo.create(MapCursor.Type.class, "mapcursortype").register();
        EnumClassInfo.create(InventoryType.class, "inventorytype").after("gamemode", "entitytype").register();
        EnumClassInfo.create(Villager.Profession.class, "profession").register();
        EnumClassInfo.create(Art.class, "art").after("damagecause", "entitytype").register();
        EnumClassInfo.create(MoonPhase.class, "moonphase").register();
        EnumClassInfo.create(EntityRegainHealthEvent.RegainReason.class, "regainreason").after("potioneffecttype").register();
    }
}
