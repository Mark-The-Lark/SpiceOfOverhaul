package ru.markthelark.spiceofoverhaul.util;

import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.bus.api.SubscribeEvent;
import squeek.appleskin.api.event.FoodValuesEvent;

public class AppleSkinEvent {
    @SubscribeEvent
    public static void onFoodStats(FoodValuesEvent event){
        FoodData foodData = event.player.getFoodData();
        String itemString = (event.itemStack.getItem().getCreatorModId(event.itemStack) + ":" + event.itemStack.getItem().toString().replace(" ", ""));
        if (foodData instanceof FoodHashAccessor){
            int eaten = ((FoodHashAccessor)foodData).getFoodHash().get(itemString) != null ? ((FoodHashAccessor)foodData).getFoodHash().get(itemString) : 0;
            FoodProperties foodProperies = event.defaultFoodProperties;
            event.modifiedFoodProperties = new FoodProperties((int) (foodProperies.nutrition() * Math.pow(0.7, eaten)),foodProperies.saturation(),foodProperies.canAlwaysEat(),foodProperies.eatSeconds(),foodProperies.usingConvertsTo(),foodProperies.effects());
        }
    }
}
