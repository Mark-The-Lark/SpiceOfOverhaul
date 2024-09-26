package ru.markthelark.spiceofoverhaul.mixin;

import ru.markthelark.spiceofoverhaul.util.FoodHashAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import squeek.appleskin.api.food.FoodValues;
import squeek.appleskin.helpers.FoodHelper;

import java.util.HashMap;
import java.util.LinkedList;

@Mixin(FoodHelper.class)
public class FoodHelperMixin {
    @Inject(method = "getDefaultFoodValues(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;)Lsqueek/appleskin/api/food/FoodValues;", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void getDefaultFoodValues(ItemStack itemStack, Player player, CallbackInfoReturnable<FoodValues> info) {
        FoodData foodData = player.getFoodData();
        int eaten = 0;
        if (foodData instanceof FoodHashAccessor) {
            HashMap<String, Integer> foodHash = ((FoodHashAccessor) foodData).getFoodHash();
            LinkedList<String> foodQueue = ((FoodHashAccessor) foodData).getFoodQueue();
            String  itemString = (itemStack.getItem().getCreatorModId(itemStack) + ":" + itemStack.getItem().toString().replace(" ",""));
            if (foodQueue.contains(itemString)) {
                eaten = foodHash.get(itemString);
            }
        }
        FoodProperties itemFood = itemStack.getItem().getFoodProperties(itemStack, player);
        int hunger = itemFood != null ? itemFood.getNutrition() : 0;
        float saturationModifier = itemFood != null ? itemFood.getSaturationModifier() : 0;

        info.setReturnValue(new FoodValues((int)(hunger * Math.pow(0.7,eaten)), saturationModifier));
    }
}

