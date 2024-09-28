package ru.markthelark.spiceofoverhaul.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.LinkedList;

public interface FoodHashAccessor {

    HashMap<String, Integer> getFoodHash();
    LinkedList<String> getFoodQueue();
    String getFoodQueueString();
    int getFoodHistory();
    void setFoodQueue(String saveFoodQueue);
    void eat(ItemStack itemStack, FoodProperties foodProperties, Player player);
}
