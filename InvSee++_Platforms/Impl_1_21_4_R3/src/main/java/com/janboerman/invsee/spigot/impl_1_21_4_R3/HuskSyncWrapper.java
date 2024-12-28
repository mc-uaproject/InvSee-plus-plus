package com.janboerman.invsee.spigot.impl_1_21_4_R3;

import net.minecraft.world.Container;
import net.william278.husksync.api.BukkitHuskSyncAPI;
import net.william278.husksync.data.DataSnapshot;
import net.william278.husksync.user.BukkitUser;
import net.william278.husksync.user.OnlineUser;
import net.william278.husksync.user.User;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_21_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_21_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class HuskSyncWrapper {
    public static BukkitHuskSyncAPI getAPI() { return BukkitHuskSyncAPI.getInstance(); }

    public static CompletableFuture<Boolean> loadPlayerSnapshot(CraftHumanEntity player, boolean enderChest) {
        User user = new User(player.getUniqueId(), player.getName());
        CompletableFuture<Optional<ItemStack[]>> itemsFuture;
        org.bukkit.inventory.Inventory inventory;
        if (!enderChest) {
            itemsFuture = getAPI().getCurrentInventoryContents(user);
            inventory = player.getInventory();
        } else {
            itemsFuture = getAPI().getCurrentEnderChestContents(user);
            inventory = player.getEnderChest();
        }
        return itemsFuture.thenApply(itemsOptional -> {
            if (itemsOptional.isPresent()) {
                Container realInventory = ((CraftInventory) inventory).getInventory();
                var realItems = itemsOptional.get();
                for (int i = 0; i < Math.min(realInventory.getContainerSize(), realItems.length); i++) {
                    realInventory.setItem(i, CraftItemStack.asNMSCopy(realItems[i]));
                }
            }
            return true;
        });
    }

    public static void savePlayerSnapshot(FakeCraftPlayer player, boolean enderChest) {
        OnlineUser user = BukkitUser.adapt(player, getAPI().getPlugin());
        getAPI().getPlugin().getDatabase().ensureUser(user);
        var data = user.createSnapshot(enderChest ? DataSnapshot.SaveCause.ENDERCHEST_COMMAND : DataSnapshot.SaveCause.INVENTORY_COMMAND);
        getAPI().setCurrentData(user, data);
    }
}
