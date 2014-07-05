package com.w00tmast3r.skquery.elements.virtualchests.v2;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.ManualDoc;
import com.w00tmast3r.skquery.api.Patterns;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;


@Patterns({
        "format slot %number% of %players% with %itemstack% to close then run %string%",
        "format slot %number% of %players% with %itemstack% to run %string%",
        "format slot %number% of %players% with %itemstack% to close",
        "format slot %number% of %players% with %itemstack% to (be|act) unstealable",
        "unformat slot %number% of %players%"
})
@ManualDoc(
        name = "Format Item Menu V2",
        description = "Format a single slot to act as an virtual menu. This only applies when the player has an open inventory."
)
public class EffAddFormatSlot extends Effect {

    private Expression<Number> slot;
    private Expression<Player> targets;
    private Expression<ItemStack> item;
    private Expression<String> callback;
    private int action;

    @Override
    protected void execute(Event event) {
        Number s = slot.getSingle(event);
        String c;
        ItemStack i = null;
        if (s == null) return;
        SlotRule toClone;
        switch (action) {
            case 0:
                c = callback.getSingle(event);
                i = item.getSingle(event);
                if (c == null) return;
                toClone = new SlotRule(c, true);
                break;
            case 1:
                c = callback.getSingle(event);
                i = item.getSingle(event);
                if (c == null) return;
                toClone = new SlotRule(c, false);
                break;
            case 2:
                i = item.getSingle(event);
                if (i == null) return;
                toClone = new SlotRule(null, true);
                break;
            case 3:
                i = item.getSingle(event);
                toClone = new SlotRule(null, false);
                break;
            case 4:
                for (Player p : targets.getAll(event)) {
                    FormattedSlotManager.removeRule(p, s.intValue());
                }
                return;
            default:
                assert false;
                return;
        }
        if (i != null) {
            for (Player p : targets.getAll(event)) {
                if (p.getOpenInventory().getType() != InventoryType.CRAFTING) p.getOpenInventory().setItem(s.intValue(), i);
            }
        }
        for (Player p : targets.getAll(event)) {
            if (p.getOpenInventory().getType() != InventoryType.CRAFTING) FormattedSlotManager.addRule(p, s.intValue(), toClone.getCopy());
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return "format";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        slot = (Expression<Number>) expressions[0];
        targets = (Expression<Player>) expressions[1];
        if (i <= 3) item = (Expression<ItemStack>) expressions[2];
        if (i <= 1) callback = (Expression<String>) expressions[3];
        action = i;
        return true;
    }
}
