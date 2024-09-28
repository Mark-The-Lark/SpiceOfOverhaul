package ru.markthelark.spiceofoverhaul.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;


public class WellFedEffect extends MobEffect {
    public WellFedEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return true;
    }
}
