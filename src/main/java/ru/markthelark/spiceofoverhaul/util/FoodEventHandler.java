package ru.markthelark.spiceofoverhaul.util;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ru.markthelark.spiceofoverhaul.Config;

public class FoodEventHandler {
    @SubscribeEvent
    public static void tickPlayer(LivingEvent.LivingTickEvent event) {
        if(!(event.getEntity() instanceof Player)){
            return;
        }
        Player player = (Player) event.getEntity();
        //Constant exhaustion
        if (!player.isCreative() && !player.isDeadOrDying() && Config.idleExhaustion)
        {
            player.causeFoodExhaustion(0.01F);
        }
        //Negatives
        int difficultyScale = player.level().getDifficulty().getId()-1;
        FoodData foodData = player.getFoodData();
        int hunger = foodData.getFoodLevel();
        float saturation = foodData.getSaturationLevel();
        float healthPercent = player.getHealth()/player.getMaxHealth();
        if ((hunger <= 1 && Config.lowHungerStrikes) || (healthPercent <= 0.05f && Config.lowHealthStrikes) || (saturation >= 18f && Config.highSaturationStrikes)){
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19, difficultyScale+1, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 19, difficultyScale+1, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 19, difficultyScale-1, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 79, 0, false, false, false));
        }
        else if ((hunger <= 2 && Config.lowHungerStrikes) || (healthPercent <= 0.1f && Config.lowHealthStrikes) || (saturation >= 16f && Config.highSaturationStrikes)){
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19, difficultyScale, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 19, difficultyScale, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 19, difficultyScale-2, false, false, false));
        }
        else if ((hunger <= 3 && Config.lowHungerStrikes) || (healthPercent <= 0.15f && Config.lowHealthStrikes) || (saturation >= 14f && Config.highSaturationStrikes)){
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19, difficultyScale-1, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 19, difficultyScale-1, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 19, difficultyScale-3, false, false, false));
        }
        else if ((hunger <= 4 && Config.lowHungerStrikes) || (healthPercent <= 0.2f && Config.lowHealthStrikes) || (saturation >= 12f && Config.highSaturationStrikes)){
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19, difficultyScale-2, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 19, difficultyScale-2, false, false, false));
        }
        else if ((hunger <= 5 && Config.lowHungerStrikes) || (healthPercent <= 0.25f && Config.lowHealthStrikes) || (saturation >= 10f && Config.highSaturationStrikes)){
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19, difficultyScale-3, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 19, difficultyScale-3, false, false, false));
        }
    }
}
