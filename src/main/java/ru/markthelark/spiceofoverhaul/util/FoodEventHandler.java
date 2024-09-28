package ru.markthelark.spiceofoverhaul.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
//import net.minecraftforge.client.gui.overlay.ForgeGui;
//import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import ru.markthelark.spiceofoverhaul.Config;

public class FoodEventHandler {

    static ResourceLocation PLAYER_HEALTH_ELEMENT = new ResourceLocation("minecraft", "player_health");

    @SubscribeEvent
    public static void tickPlayer(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        //Constant exhaustion
        if (!player.isCreative() && !player.isDeadOrDying() && Config.idleExhaustion) {
            player.causeFoodExhaustion(Config.idleExhaustionQuantity);
        }
        //Negatives
        int difficultyScale = Config.strikesDifficultyBase + player.level.getDifficulty().getId() * Config.StrikesDifficultyScale;
        FoodData foodData = player.getFoodData();
        int hunger = foodData.getFoodLevel();
        float saturation = foodData.getSaturationLevel();
        float healthPercent = player.getHealth() / player.getMaxHealth();
        @Nullable MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("farmersdelight", "nourishment"));
        if (effect != null && player.hasEffect(effect)) {
            return;
        }
        if ((hunger <= Config.hungerLevel - 4 && Config.lowHungerStrikes) || (healthPercent <= Config.healthLevel - 0.2f && Config.lowHealthStrikes) || (saturation >= Config.saturationLevel + 4f && Config.highSaturationStrikes)) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19, difficultyScale, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 19, difficultyScale, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 19, difficultyScale - 2, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 79, 0, false, false, false));
        } else if ((hunger <= Config.hungerLevel - 3 && Config.lowHungerStrikes) || (healthPercent <= Config.healthLevel - 0.15f && Config.lowHealthStrikes) || (saturation >= Config.saturationLevel + 3f && Config.highSaturationStrikes)) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19, difficultyScale - 1, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 19, difficultyScale - 1, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 19, difficultyScale - 3, false, false, false));
        } else if ((hunger <= Config.hungerLevel - 2 && Config.lowHungerStrikes) || (healthPercent <= Config.healthLevel - 0.1f && Config.lowHealthStrikes) || (saturation >= Config.saturationLevel + 2f && Config.highSaturationStrikes)) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19, difficultyScale - 2, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 19, difficultyScale - 2, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 19, difficultyScale - 4, false, false, false));
        } else if ((hunger <= Config.hungerLevel - 1 && Config.lowHungerStrikes) || (healthPercent <= Config.healthLevel - 0.05f && Config.lowHealthStrikes) || (saturation >= Config.saturationLevel + 1f && Config.highSaturationStrikes)) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19, difficultyScale - 3, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 19, difficultyScale - 3, false, false, false));
        } else if ((hunger <= Config.hungerLevel && Config.lowHungerStrikes) || (healthPercent <= Config.healthLevel && Config.lowHealthStrikes) || (saturation >= Config.saturationLevel && Config.highSaturationStrikes)) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19, difficultyScale - 4, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 19, difficultyScale - 4, false, false, false));
        }
    }

    @SubscribeEvent
    public static void onFoodStartEating(LivingEntityUseItemEvent.Start event) {
        if (Config.modifyFoodEatingSpeed && event.getItem().getItem().isEdible()) {
            int hunger = event.getItem().getFoodProperties((LivingEntity) event.getEntity()).getNutrition();
            event.setDuration(hunger * 6 + 8);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.PreLayer event)
    {
        if (Config.addGuiText && event.getOverlay() == ForgeIngameGui.PLAYER_HEALTH_ELEMENT)
        {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            ForgeIngameGui gui = (ForgeIngameGui) mc.gui;
            int foodIconsOffset = gui.right_height;
            int right = mc.getWindow().getGuiScaledWidth() / 2;
            int top = mc.getWindow().getGuiScaledHeight()-foodIconsOffset;

            if (!player.isDeadOrDying() && !player.isCreative()) {
                float healthPercent = player.getHealth() / player.getMaxHealth();

                if (healthPercent <= 0.15F) {
                    GuiComponent.drawString(event.getMatrixStack(), gui.getFont(), "dying", right-91-"dying".length()*6+1, top, -1);
                } else if (healthPercent <= 0.3F) {
                    GuiComponent.drawString(event.getMatrixStack(),gui.getFont(), "injured", right-91-"injured".length()*6+1, top, -1);
                } else if (healthPercent < 0.5F) {
                    GuiComponent.drawString(event.getMatrixStack(),gui.getFont(), "hurt", right-91-"hurt".length()*6+1, top, -1);
                }
                if (player.getFoodData().getFoodLevel() <= 6) {
                    GuiComponent.drawString(event.getMatrixStack(),gui.getFont(), "starving", right+93, top, -1);
                } else if (player.getFoodData().getFoodLevel() <= 10) {
                    GuiComponent.drawString(event.getMatrixStack(),gui.getFont(), "hungry", right+93, top, -1);
                } else if (player.getFoodData().getFoodLevel() <= 14) {
                    GuiComponent.drawString(event.getMatrixStack(),gui.getFont(), "peckish", right+93, top, -1);
                }
            }
        }
    }
}
