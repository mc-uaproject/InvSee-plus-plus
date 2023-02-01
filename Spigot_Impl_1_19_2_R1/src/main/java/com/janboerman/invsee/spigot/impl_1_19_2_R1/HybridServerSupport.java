package com.janboerman.invsee.spigot.impl_1_19_2_R1;

import com.janboerman.invsee.utils.FuzzyReflection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.storage.PlayerDataStorage;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HybridServerSupport {

    private HybridServerSupport() {}

    public static File getPlayerDir(PlayerDataStorage worldNBTStorage) {
        try {
            return worldNBTStorage.getPlayerDir();
        } catch (NoSuchMethodError craftbukkitMethodNotFound) {
            try {
                Method method = worldNBTStorage.getClass().getMethod("getPlayerDataFolder");
                return (File) method.invoke(worldNBTStorage);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException forgeMethodNotFound) {
                Field[] fields = FuzzyReflection.getFieldOfType(PlayerDataStorage.class, File.class);
                for (Field field : fields) {
                    try {
                        return (File) field.get(worldNBTStorage);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Could not access player data folder field from PlayerDataStorage", e);
                    }
                }
                RuntimeException ex = new RuntimeException("No method known of getting the player directory");
                ex.addSuppressed(craftbukkitMethodNotFound);
                ex.addSuppressed(forgeMethodNotFound);
                throw ex;
            }
        }
    }

    public static int nextContainerCounter(ServerPlayer nmsPlayer) {
        try {
            return nmsPlayer.nextContainerCounter();
        } catch (NoSuchMethodError craftbukkitMethodNotFound) {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            try {
                MethodHandle methodHandle = lookup.findVirtual(nmsPlayer.getClass(), "nextContainerCounterInt", MethodType.methodType(int.class));
                //this should work on Magma as well as Mohist.
                return (int) methodHandle.invoke(nmsPlayer);
            } catch (Throwable magmaMethodNotFound) {
                //look up the obfuscated field name and get it by reflection?
                RuntimeException ex = new RuntimeException("No method known of incrementing the player's container counter");
                ex.addSuppressed(craftbukkitMethodNotFound);
                ex.addSuppressed(magmaMethodNotFound);
                throw ex;
            }
        }
    }

    public static int slot(Slot slot) {
        try {
            return slot.slot;
        } catch (IllegalAccessError craftbukkitFieldIsActuallyPrivate) {
            try {
                return slot.getContainerSlot();
            } catch (Throwable vanillaMethodNotFound) {
                try {
                    MethodHandles.Lookup lookup = MethodHandles.lookup();
                    MethodHandle methodHandle = lookup.findVirtual(slot.getClass(), "getSlotIndex", MethodType.methodType(int.class));
                    //this should work on Magma as well as Mohist.
                    return (int) methodHandle.invoke(slot);
                } catch (Throwable forgeMethodNotFound) {
                    RuntimeException ex = new RuntimeException("No method known of getting the slot's inventory index");
                    ex.addSuppressed(craftbukkitFieldIsActuallyPrivate);
                    ex.addSuppressed(vanillaMethodNotFound);
                    ex.addSuppressed(forgeMethodNotFound);
                    throw ex;
                }
            }
        }
    }

}