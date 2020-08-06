package com.janboerman.invsee.spigot.impl_1_16;

import com.janboerman.invsee.spigot.api.EnderSpectatorInventory;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftInventory;

import java.util.UUID;

public class EnderBukkitInventory extends CraftInventory implements EnderSpectatorInventory {

    protected EnderBukkitInventory(EnderNmsInventory inventory) {
        super(inventory);
    }

    @Override
    public EnderNmsInventory getInventory() {
        return (EnderNmsInventory) super.getInventory();
    }

    @Override
    public UUID getSpectatedPlayer() {
        return getInventory().spectatedPlayerUuid;
    }

    @Override
    public String getTitle() {
        return getInventory().title;
    }

}
