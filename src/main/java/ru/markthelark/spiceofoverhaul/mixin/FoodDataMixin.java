package ru.markthelark.spiceofoverhaul.mixin;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import ru.markthelark.spiceofoverhaul.Config;
import ru.markthelark.spiceofoverhaul.util.FoodHashAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(FoodData.class)
public abstract class FoodDataMixin implements FoodHashAccessor {
    @Shadow private int lastFoodLevel;
    @Shadow private int foodLevel;
    @Shadow private float exhaustionLevel;
    @Shadow private float saturationLevel;
    @Shadow private int tickTimer;

    @Unique public final int historyLength = Config.historyLength;
    @Unique private LinkedList<String> foodQueue = new LinkedList<>();
    @Unique public HashMap<String,Integer> foodHash = new HashMap<>();
    @Unique public HashMap<String,Integer> getFoodHash(){
        return this.foodHash;
    }
    @Unique public LinkedList<String> getFoodQueue(){
        return this.foodQueue;
    }
    @Unique public int getFoodHistory(){return this.historyLength;}
    @Unique public String getFoodQueueString(){
        String saveFoodQueue = this.foodQueue.toString().replace(" ", "");
        saveFoodQueue = saveFoodQueue.replace("[", "");
        saveFoodQueue = saveFoodQueue.replace("]", "");
        return saveFoodQueue;
    }
//Implement Special formula and fix cakes
    @Shadow public abstract void eat(int p_38708_, float p_38709_);
    public void eat(Item item, ItemStack itemStack, LivingEntity entity){
        if (Config.enableSOLModule) {
            if (item.isEdible()) {
                FoodProperties foodproperties = itemStack.getFoodProperties(entity);
                String itemString = (itemStack.getItem().getCreatorModId(itemStack) + ":" + itemStack.getItem().toString().replace(" ", ""));
                if (!this.foodQueue.contains(itemString)) {
                    this.foodHash.put(itemString, 0);
                }
                int eaten = this.foodHash.get(itemString);
                this.eat((int) (foodproperties.getNutrition() * Math.pow(0.7, eaten)), foodproperties.getSaturationModifier());
                if (this.foodQueue.size() >= this.historyLength) {
                    String elem = this.foodQueue.pollFirst();
                    this.foodHash.put(elem, this.foodHash.get(elem) - 1);
                }
                this.foodHash.put(itemString, this.foodHash.get(itemString) + 1);
                this.foodQueue.add(itemString);
            }
        }
        else {
            if (item.isEdible()) {
                FoodProperties foodproperties = itemStack.getFoodProperties(entity);
                this.eat(foodproperties.getNutrition(), foodproperties.getSaturationModifier());
            }
        }
    }
    @Inject(at = @At(value = "TAIL"), method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V")
    public void addAdditionalSaveData(CompoundTag p_38720_, CallbackInfo info) {
        String saveFoodQueue = this.foodQueue.toString().replace(" ", "");
        saveFoodQueue = saveFoodQueue.replace("[", "");
        saveFoodQueue = saveFoodQueue.replace("]", "");
        p_38720_.putString("spiceofoverhaul:foodQueue", saveFoodQueue);
        p_38720_.putString("debug", this.foodHash.toString());
    }
    @Inject(at = @At(value = "TAIL"), method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V")
    public void readAdditionalSaveData(CompoundTag p_38716_, CallbackInfo info) {
        if (p_38716_.contains("spiceofoverhaul:foodQueue")) {
            String saveFoodQueue = p_38716_.getString("spiceofoverhaul:foodQueue");
            LinkedList<String> foodList = new LinkedList<>(Arrays.asList(saveFoodQueue.split(",")));
            int length = foodList.size();
            for (int i = 0; i < length-this.historyLength+1; ++i){
                foodList.removeLast();
            }
            this.foodQueue= foodList;
            HashMap<String, Integer> hash = new HashMap<>();
            for (String element: foodList) {
                int frequency = hash.get(element)==null ? 1 : hash.get(element)+1;
                hash.put(element, frequency);
            }
            this.foodHash = hash;
        }
    }
    @Inject(at = @At(value = "HEAD"), method = "needsFood", cancellable = true)
    public void onNeedsFood(CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(true);
    }
    @Inject(at = @At(value = "HEAD"), method = "tick(Lnet/minecraft/world/entity/player/Player;)V", cancellable = true)
    private void injected(Player p_38711_, CallbackInfo info){
        if (Config.regenRework) {
            info.cancel();
            Difficulty difficulty = p_38711_.level().getDifficulty();
            this.lastFoodLevel = this.foodLevel;
            if (this.exhaustionLevel > 4.0F) {
                this.exhaustionLevel -= 4.0F;
                if (this.saturationLevel > 0.0F) {
                    this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
                } else if (difficulty != Difficulty.PEACEFUL) {
                    this.foodLevel = Math.max(this.foodLevel - 1, 0);
                }
            }


            boolean flag = p_38711_.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
            if (flag && this.foodLevel >= 7 && p_38711_.isHurt()) {
                ++this.tickTimer;
                if (this.tickTimer >= 40) {
                    p_38711_.heal(1.0F);
                    this.foodLevel = Math.max(this.foodLevel - 1, 0);
                    this.tickTimer = 0;
                }
            } else if (this.foodLevel <= 0) {
                ++this.tickTimer;
                if (this.tickTimer >= 20) {
                    p_38711_.hurt(p_38711_.damageSources().starve(), 1.0F);
                    this.tickTimer = 0;
                }
            } else {
                this.tickTimer = 0;
            }
        }
    }
    @Unique
    public void setFoodQueue(String saveFoodQueue){
        LinkedList<String> foodList = new LinkedList<>(Arrays.asList(saveFoodQueue.split(",")));
        int length = foodList.size();
        for (int i = 0; i < length-this.historyLength+1; ++i){
            foodList.removeLast();
        }
        this.foodQueue= foodList;
        HashMap<String, Integer> hash = new HashMap<>();
        for (String element: foodList) {
            int frequency = hash.get(element)==null ? 1 : hash.get(element)+1;
            hash.put(element, frequency);
        }
        this.foodHash = hash;
    }
}
