package com.ksanur.betterenchantments;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * User: bobacadodl
 * Date: 1/16/14
 * Time: 8:48 PM
 */
public class BetterEnchantments extends JavaPlugin {
    PacketListener itemRenamer;

    public void onEnable() {
        itemRenamer = new ItemRenamer(this);
        ProtocolLibrary.getProtocolManager().addPacketListener(itemRenamer);
    }

    public void onDisable() {
        ProtocolLibrary.getProtocolManager().removePacketListener(itemRenamer);
    }
}
