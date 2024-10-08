package ru.markthelark.spiceofoverhaul.mixin;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin{

    @Shadow abstract boolean hasEffect(MobEffect effect);
    @Shadow public abstract float getMaxHealth();
    @Shadow public abstract float getHealth();

}
