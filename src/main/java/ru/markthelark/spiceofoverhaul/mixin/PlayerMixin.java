package ru.markthelark.spiceofoverhaul.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.markthelark.spiceofoverhaul.Config;


@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntityMixin {
    @Shadow public abstract FoodData getFoodData();


    @Inject(at = @At(value="HEAD"), method = "jumpFromGround()V", cancellable = true)
    public void injected(CallbackInfo info) {
        @Nullable MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("farmersdelight","nourishment"));
        if (effect != null && this.hasEffect(effect)){return;}
        if (Config.strikesJump) {
            if ((this.foodData.getFoodLevel() <= 1 && Config.lowHungerStrikes) || (this.getHealth() / this.getMaxHealth() <= 0.05f && Config.lowHealthStrikes) || (this.foodData.getSaturationLevel() >= 18f && Config.highSaturationStrikes)) {
                info.cancel();
            }
        }
    }
    @Inject(at = @At(value = "HEAD"), method = "causeFoodExhaustion(F)V", cancellable = true)
    public void injected(float p_36400_, CallbackInfo ci){
        ci.cancel();
        if (!this.abilities.invulnerable) {
            if (!this.level.isClientSide()) {
                int hunger = this.getFoodData().getFoodLevel();
                float saturation = this.getFoodData().getSaturationLevel();
                float modifier = (float)Math.min(hunger, 7)/7f*Math.max(saturation, 15f)/15f;
                this.foodData.addExhaustion(p_36400_*modifier);
            }

        }
    }
    @Shadow protected FoodData foodData;
    @Final @Shadow private Abilities abilities;
}

