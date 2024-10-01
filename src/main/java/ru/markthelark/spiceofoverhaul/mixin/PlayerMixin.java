package ru.markthelark.spiceofoverhaul.mixin;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.markthelark.spiceofoverhaul.Config;
import ru.markthelark.spiceofoverhaul.util.FoodHashAccessor;
import net.minecraft.sounds.SoundSource;

import java.util.Optional;


@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntityMixin {
    @Shadow public abstract FoodData getFoodData();

    @Inject(at = @At(value = "HEAD"), method = "Lnet/minecraft/world/entity/player/Player;eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/ItemStack;", cancellable = true)
    public void injected(Level pLevel, ItemStack pFood, FoodProperties pFoodProperties, CallbackInfoReturnable info) {
        info.cancel();
        FoodData foodData1 = this.getFoodData();
        if (!(foodData1 instanceof FoodHashAccessor)) {return;
        }
        ((FoodHashAccessor)foodData1).eat(pFood, pFoodProperties, (Player)(Object)this);
        this.awardStat(Stats.ITEM_USED.get(pFood.getItem()));
        pLevel.playSound(
                null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, pLevel.random.nextFloat() * 0.1F + 0.9F
        );
        if ((Player)(Object)this instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)(Object)this, pFood);
        }

        ItemStack itemstack = super.eat(pLevel, pFood, pFoodProperties);
        Optional<ItemStack> optional = pFoodProperties.usingConvertsTo();
        if (optional.isPresent() && !this.hasInfiniteMaterials()) {
            if (itemstack.isEmpty()) {
                info.setReturnValue(optional.get().copy());
            }

            if (!this.level().isClientSide()) {
                ItemStack container = optional.get().copy();
                if (!getInventory().add(container)) {
                    drop(container, false);
                }
            }
        }

        info.setReturnValue(itemstack);
    }
    @Shadow public abstract ItemEntity drop(ItemStack pItemStack, boolean pIncludeThrowerName);

    @Shadow protected abstract Inventory getInventory();

    @Shadow protected abstract boolean hasInfiniteMaterials();

    @Shadow protected abstract void awardStat(Stat<Item> itemStat);


    @Inject(at = @At(value="HEAD"), method = "jumpFromGround()V", cancellable = true)
    public void injected(CallbackInfo info) {
//        @Nullable MobEffect effect = Registries.MOB_EFFECT.getValue(new ResourceLocation("farmersdelight","nourishment"));
//        if (effect != null && this.hasEffect(effect)){return;}
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
            if (!this.level().isClientSide()) {
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

