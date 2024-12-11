package ru.markthelark.spiceofoverhaul.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.LinkedList;

public interface FoodHashAccessor {
    boolean isUpdated();
    void setNotUpdated();
    HashMap<String, Integer> getFoodHash();
    LinkedList<String> getFoodQueue();
    String getFoodQueueString();
    int getFoodHistory();
    void setFoodQueue(String saveFoodQueue);
    void eat (Item item, ItemStack itemStack, LivingEntity player);
}
