package com.ksanur.betterenchantments;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: bobacadodl
 * Date: 1/16/14
 * Time: 8:56 PM
 */
public class ItemRenamer extends PacketAdapter {
    public static HashMap<Enchantment, String> enchantmentMap = new HashMap<Enchantment, String>() {{
        put(Enchantment.PROTECTION_ENVIRONMENTAL, "Protection");
        put(Enchantment.PROTECTION_FIRE, "Fire Protection");
        put(Enchantment.PROTECTION_FALL, "Feather Falling");
        put(Enchantment.PROTECTION_EXPLOSIONS, "Blast Protection");
        put(Enchantment.PROTECTION_PROJECTILE, "Projectile Protection");

        put(Enchantment.OXYGEN, "Respiration");
        put(Enchantment.WATER_WORKER, "Aqua Affinity");
        put(Enchantment.THORNS, "Thorns");

        put(Enchantment.DAMAGE_ALL, "Sharpness");
        put(Enchantment.DAMAGE_UNDEAD, "Smite");
        put(Enchantment.DAMAGE_ARTHROPODS, "Bane of Arthropods");
        put(Enchantment.KNOCKBACK, "Knockback");
        put(Enchantment.FIRE_ASPECT, "Fire Aspect");
        put(Enchantment.LOOT_BONUS_MOBS, "Looting");

        put(Enchantment.DIG_SPEED, "Efficiency");
        put(Enchantment.SILK_TOUCH, "Silk Touch");
        put(Enchantment.DURABILITY, "Unbreaking");
        put(Enchantment.LOOT_BONUS_BLOCKS, "Fortune");

        put(Enchantment.ARROW_DAMAGE, "Power");
        put(Enchantment.ARROW_FIRE, "Flame");
        put(Enchantment.ARROW_INFINITE, "Infinity");
        put(Enchantment.ARROW_KNOCKBACK, "Punch");

        put(Enchantment.LURE, "Lure");
        put(Enchantment.LUCK, "Luck of the Sea");
    }};


    public ItemRenamer(Plugin plugin) {
        super(plugin, ListenerPriority.HIGH, PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.getPacket().getType() == PacketType.Play.Server.SET_SLOT) {
            ItemStack[] stacks = new ItemStack[]{event.getPacket().getItemModifier().read(0)};
            for (ItemStack stack : stacks) {
                convertToRomanNumerals(stack);
            }
        } else {
            ItemStack[] stacks = event.getPacket().getItemArrayModifier().read(0);
            for (ItemStack stack : stacks) {
                convertToRomanNumerals(stack);
            }
        }
    }

    public void convertToRomanNumerals(ItemStack stack) {
        if (stack != null) {
            if (stack.getEnchantments().size() > 0) {
                for (Enchantment e : stack.getEnchantments().keySet()) {
                    int level = stack.getEnchantments().get(e);
                    ItemMeta im = stack.getItemMeta();
                    List<String> lore = (im.getLore() == null) ? new ArrayList<String>() : im.getLore();
                    String enchantmentName = (enchantmentMap.containsKey(e)) ? enchantmentMap.get(e) : e.getName();
                    lore.add(0, ChatColor.GRAY + enchantmentName + " " + new RomanNumeral(level).toString() + "hi");

                    NbtCompound compound = (NbtCompound) NbtFactory.fromItemTag(stack);
                    if (compound.containsKey("display")) {
                        NbtCompound displayCompound = compound.getCompound("display");
                        displayCompound.put(NbtFactory.ofList("Lore", lore));
                    } else {
                        NbtCompound displayCompound = NbtFactory.ofCompound("display");
                        displayCompound.put(NbtFactory.ofList("Lore", lore));
                        compound.put(displayCompound);
                    }
                }
                NbtCompound compound = (NbtCompound) NbtFactory.fromItemTag(stack);
                compound.put(NbtFactory.ofList("ench"));
            }
        }
    }
}
