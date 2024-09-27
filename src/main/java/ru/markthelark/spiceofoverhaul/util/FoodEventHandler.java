package ru.markthelark.spiceofoverhaul.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
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
            player.causeFoodExhaustion(Config.idleExhaustionQuantity);
        }
        //Negatives
        int difficultyScale = Config.strikesDifficultyBase + player.level().getDifficulty().getId()*Config.StrikesDifficultyScale;
        FoodData foodData = player.getFoodData();
        int hunger = foodData.getFoodLevel();
        float saturation = foodData.getSaturationLevel();
        float healthPercent = player.getHealth()/player.getMaxHealth();
        if ((hunger <= Config.hungerLevel-4 && Config.lowHungerStrikes) || (healthPercent <= Config.healthLevel-0.2f && Config.lowHealthStrikes) || (saturation >= Config.saturationLevel+4f && Config.highSaturationStrikes)){
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19, difficultyScale, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 19, difficultyScale, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 19, difficultyScale-2, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 79, 0, false, false, false));
        }
        else if ((hunger <= Config.hungerLevel-3 && Config.lowHungerStrikes) || (healthPercent <= Config.healthLevel-0.15f && Config.lowHealthStrikes) || (saturation >= Config.saturationLevel+3f && Config.highSaturationStrikes)){
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19, difficultyScale-1, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 19, difficultyScale-1, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 19, difficultyScale-3, false, false, false));
        }
        else if ((hunger <= Config.hungerLevel-2 && Config.lowHungerStrikes) || (healthPercent <= Config.healthLevel-0.1f && Config.lowHealthStrikes) || (saturation >= Config.saturationLevel+2f && Config.highSaturationStrikes)){
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19, difficultyScale-2, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 19, difficultyScale-2, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 19, difficultyScale-4, false, false, false));
        }
        else if ((hunger <= Config.hungerLevel-1 && Config.lowHungerStrikes) || (healthPercent <= Config.healthLevel-0.05f && Config.lowHealthStrikes) || (saturation >= Config.saturationLevel+1f && Config.highSaturationStrikes)){
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19, difficultyScale-3, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 19, difficultyScale-3, false, false, false));
        }
        else if ((hunger <= Config.hungerLevel && Config.lowHungerStrikes) || (healthPercent <= Config.healthLevel && Config.lowHealthStrikes) || (saturation >= Config.saturationLevel && Config.highSaturationStrikes)){
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19, difficultyScale-4, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 19, difficultyScale-4, false, false, false));
        }
    }
//    @OnlyIn(Dist.CLIENT)
//    @SubscribeEvent
//    public void onRenderGameOverlay(RenderGuiEvent event)
//    {
//        if (Config.addGuiText)
//        {
//            Minecraft mc = Minecraft.getInstance();
//            Player player = mc.player;
//            ForgeGui gui = (ForgeGui) mc.gui;
//
//            if (!player.isDeadOrDying() && !player.isCreative())
//            {
//                float healthPercent = player.getHealth() / player.getMaxHealth();
//
//                if (healthPercent <= 0.15F)
//                {
////                    gui.getLeft().add(TextFormatting.RED + I18n.translateToLocal("ui.health.dying") + TextFormatting.RESET);
//                    event.getGuiGraphics().drawString(gui.getFont(), "dying", 2, 2, -1, false);
//                }
//                else if (healthPercent <= 0.3F)
//                {
////                    event.getLeft().add(TextFormatting.YELLOW + I18n.translateToLocal("ui.health.injured") + TextFormatting.RESET);
//                    event.getGuiGraphics().drawString(gui.getFont(), "injured", 2, 2, -1, false);
//                }
//                else if (healthPercent < 0.5F)
//                {
////                    event.getLeft().add(TextFormatting.WHITE + I18n.translateToLocal("ui.health.hurt") + TextFormatting.RESET);
//                    event.getGuiGraphics().drawString(gui.getFont(), "hurt", 2, 2, -1, false);
//                }
//
//                if (player.getFoodData().getFoodLevel() <= 6)
//                {
////                    event.getRight().add(TextFormatting.RED + I18n.translateToLocal("ui.hunger.starving") + TextFormatting.RESET);
//                    event.getGuiGraphics().drawString(gui.getFont(), "starving", 2, 2, -1, false);
//                }
//                else if (player.getFoodData().getFoodLevel() <= 10)
//                {
////                    event.getRight().add(TextFormatting.YELLOW + I18n.translateToLocal("ui.hunger.hungry") + TextFormatting.RESET);
//                    event.getGuiGraphics().drawString(gui.getFont(), "hungry", 2, 2, -1, false);
//                }
//                else if (player.getFoodData().getFoodLevel() <= 14)
//                {
////                    event.getRight().add(TextFormatting.WHITE + I18n.translateToLocal("ui.hunger.peckish") + TextFormatting.RESET);
//                    event.getGuiGraphics().drawString(gui.getFont(), "peckish", 2, 2, -1, false);
//                }
//            }
//        }
//    }
}