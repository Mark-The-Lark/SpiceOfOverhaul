package ru.markthelark.spiceofoverhaul.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.markthelark.spiceofoverhaul.Config;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntityMixin {
    @Inject(at = @At(value="HEAD"), method = "jumpFromGround()V", cancellable = true)
    public void injected(CallbackInfo info) {
        if (Config.strikesJump) {
            if ((this.foodData.getFoodLevel() <= 1 && Config.lowHungerStrikes) || (this.getHealth() / this.getMaxHealth() <= 0.05f && Config.lowHealthStrikes) || (this.foodData.getSaturationLevel() >= 16f && Config.highSaturationStrikes)) {
                info.cancel();
            }
        }
    }
    @Shadow FoodData foodData;
}

