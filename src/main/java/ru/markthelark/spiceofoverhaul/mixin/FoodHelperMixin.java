package ru.markthelark.spiceofoverhaul.mixin;//package ru.markthelark.spiceofoverhaul.mixin;
//
//import com.mojang.datafixers.util.Pair;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import ru.markthelark.spiceofoverhaul.util.FoodHashAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import squeek.appleskin.helpers.FoodHelper;

import java.util.HashMap;
import java.util.LinkedList;


@Mixin(FoodHelper.class)
public class FoodHelperMixin {
//    @Inject(method = "Lsqueek/appleskin/helpers/FoodHelper;getDefaultFoodValues(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/food/FoodProperties;", at = @At(value = "HEAD"), remap = false, cancellable = true)
//    private static void getDefaultFoodValues(ItemStack itemStack, Player player, CallbackInfoReturnable<FoodValues> info) {
//        FoodData foodData = player.getFoodData();
//        int eaten = 0;
//        if (foodData instanceof FoodHashAccessor) {
//            HashMap<String, Integer> foodHash = ((FoodHashAccessor) foodData).getFoodHash();
//            LinkedList<String> foodQueue = ((FoodHashAccessor) foodData).getFoodQueue();
//            String  itemString = (itemStack.getItem().getCreatorModId(itemStack) + ":" + itemStack.getItem().toString().replace(" ",""));
//            if (foodQueue.contains(itemString)) {
//                eaten = foodHash.get(itemString);
//            }
//        }
//        FoodProperties itemFood = itemStack.getItem().getFoodProperties(itemStack, player);
//        int hunger = itemFood != null ? itemFood.getNutrition() : 0;
//        float saturationModifier = itemFood != null ? itemFood.getSaturationModifier() : 0;
//
//        info.setReturnValue(new FoodValues((int)(hunger * Math.pow(0.7,eaten)), saturationModifier));
//    }
    @Inject(method = "Lsqueek/appleskin/helpers/FoodHelper;getEstimatedHealthIncrement(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/food/FoodProperties;)F", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void getEstimatedHealthIncrement(Player player, FoodProperties foodProperties, CallbackInfoReturnable<Float> cir)
    {
        if (!player.isHurt())
            return;

        FoodData stats = player.getFoodData();
        Level world = player.getCommandSenderWorld();

        int foodLevel = Math.min(stats.getFoodLevel() + foodProperties.nutrition(), 20);
        float healthIncrement = 0;

        // health for natural regen
        if (foodLevel >= 7.0F && world != null && world.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION))
        {
            healthIncrement = foodLevel-6;
        }

        for (FoodProperties.PossibleEffect effect : foodProperties.effects())
        {
            MobEffectInstance effectInstance = effect.effect();
            if (effectInstance.getEffect() == MobEffects.REGENERATION)
            {
                int amplifier = effectInstance.getAmplifier();
                int duration = effectInstance.getDuration();
                healthIncrement += (float) Math.floor(duration / Math.max(50 >> amplifier, 1));
                break;
            }
        }
        cir.setReturnValue(healthIncrement);
    }
}

