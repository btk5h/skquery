package com.w00tmast3r.skquery.util.custom.menus.v2_;

import org.bukkit.Bukkit;

public class SlotRule {

    private final String callback;
    private final boolean willClose;

    public SlotRule(String callback, boolean willClose) {
        this.callback = callback;
        this.willClose = willClose;
    }

    public String getCallback() {
        return callback;
    }

    public boolean willClose() {
        return willClose;
    }

    public void run() {
        if (callback == null) return;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), callback);
    }

    public SlotRule getCopy() {
        return new SlotRule(callback, willClose);
    }
}
