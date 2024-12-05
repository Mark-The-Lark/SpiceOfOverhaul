package ru.markthelark.spiceofoverhaul.util;

import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ru.markthelark.spiceofoverhaul.items.FoodBag;
import squeek.appleskin.api.event.FoodValuesEvent;
import squeek.appleskin.api.food.FoodValues;

public class AppleSkinEvent {
    @SubscribeEvent
    public static void onFoodStats(FoodValuesEvent event){
        FoodData foodData = event.player.getFoodData();
        ItemStack pStack;
        if (event.itemStack.getItem() instanceof FoodBag){
            pStack = FoodBag.nextToEat(event.itemStack, event.player);
        }
        else {pStack =event.itemStack;}
        String itemString = (pStack.getItem().getCreatorModId(pStack) + ":" + pStack.getItem().toString().replace(" ", ""));
        if (foodData instanceof FoodHashAccessor){
            int eaten = ((FoodHashAccessor)foodData).getFoodHash().get(itemString) != null ? ((FoodHashAccessor)foodData).getFoodHash().get(itemString) : 0;
            FoodValues foodproperties = event.defaultFoodValues;
            event.modifiedFoodValues = new FoodValues(FormulaProvider.FormulaHunger(foodproperties.hunger, foodproperties.saturationModifier, eaten),
                    FormulaProvider.FormulaSaturation(foodproperties.hunger, foodproperties.saturationModifier, eaten));
        }
    }
}
