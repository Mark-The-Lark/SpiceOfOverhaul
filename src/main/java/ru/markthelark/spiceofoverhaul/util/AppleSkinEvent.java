package ru.markthelark.spiceofoverhaul.util;

import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import ru.markthelark.spiceofoverhaul.items.FoodBag;
import squeek.appleskin.api.event.FoodValuesEvent;

public class AppleSkinEvent {
    @SubscribeEvent
    public static void onFoodStats(FoodValuesEvent event){
        FoodData foodData = event.player.getFoodData();
        ItemStack pStack;
        if (event.itemStack.getItem() instanceof FoodBag){
            pStack = FoodBag.nextToEat(event.itemStack, event.player);
        }
        else {pStack =event.itemStack;}
        String itemString = pStack.getItem().toString().replace(" ", "");
        if (foodData instanceof FoodHashAccessor){
            int eaten = ((FoodHashAccessor)foodData).getFoodHash().get(itemString) != null ? ((FoodHashAccessor)foodData).getFoodHash().get(itemString) : 0;
            FoodProperties foodProperies = event.defaultFoodProperties;
            event.modifiedFoodProperties = new FoodProperties((int) (foodProperies.nutrition() * Math.pow(0.7, eaten)),foodProperies.saturation(),foodProperies.canAlwaysEat(),foodProperies.eatSeconds(),foodProperies.usingConvertsTo(),foodProperies.effects());
        }
    }
}
