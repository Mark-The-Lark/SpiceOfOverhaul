package ru.markthelark.spiceofoverhaul.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import ru.markthelark.spiceofoverhaul.Config;
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

import static squeek.appleskin.helpers.FoodHelper.isFood;

@Mixin(FoodHelper.class)
public class FoodHelperMixin {

    @Inject(method = "getEstimatedHealthIncrement(Lnet/minecraft/world/item/ItemStack;Lsqueek/appleskin/api/food/FoodValues;Lnet/minecraft/world/entity/player/Player;)F", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void getEstimatedHealthIncrement(ItemStack itemStack, FoodValues modifiedFoodValues, Player player, CallbackInfoReturnable<Float> cir)
    {
        if (!isFood(itemStack, player) || !Config.regenHungerOnly)
            return;

        if (!player.isHurt())
            return;

        FoodData stats = player.getFoodData();
        Level world = player.getCommandSenderWorld();

        int foodLevel = Math.min(stats.getFoodLevel() + modifiedFoodValues.hunger, 20);
        float healthIncrement = 0;

        // health for natural regen
        if (foodLevel >= 7.0F && world != null && world.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION))
        {
            healthIncrement = foodLevel-6;
        }

        for (Pair<MobEffectInstance, Float> effect : itemStack.getItem().getFoodProperties(itemStack, player).getEffects())
        {
            MobEffectInstance effectInstance = effect.getFirst();
            if (effectInstance != null && effectInstance.getEffect() == MobEffects.REGENERATION)
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

