package com.w00tmast3r.skquery.util.custom.menus.v2_;

import com.w00tmast3r.skquery.skript.LambdaEffect;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public class SlotRule {

    private final Object callback;
    private final boolean willClose;

    public SlotRule(Object callback, boolean willClose) {
        this.callback = callback;
        this.willClose = willClose;
    }

    public Object getCallback() {
        return callback;
    }

    public boolean willClose() {
        return willClose;
    }

    public void run(Event e) {
        if (callback == null) return;
        if (callback instanceof String) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), (String) callback);
        } else if (callback instanceof LambdaEffect) {
            ((LambdaEffect) callback).walk(e);
        }
    }

    public SlotRule getCopy() {
        return new SlotRule(callback, willClose);
    }
}
