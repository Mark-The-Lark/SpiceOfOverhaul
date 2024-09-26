package ru.markthelark.spiceofoverhaul.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.markthelark.spiceofoverhaul.Config;
import ru.markthelark.spiceofoverhaul.util.FoodHashAccessor;

import java.util.HashMap;
import java.util.LinkedList;


@Mixin(CakeBlock.class)
public class CakeBlockMixin {

    @Shadow public static IntegerProperty BITES;
    @Inject(method = "eat(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/InteractionResult;", at = @At(value = "HEAD"), cancellable = true)
    private static void injected(LevelAccessor p_51186_, BlockPos p_51187_, BlockState p_51188_, Player p_51189_, CallbackInfoReturnable<InteractionResult> cir) {
        if (!p_51189_.canEat(false)) {
            return;
        } else {

            String blockString = "minecraft:" + p_51188_.getBlock().toString();
            FoodData foodData = p_51189_.getFoodData();
            if (!(foodData instanceof FoodHashAccessor))
            { return; }
            cir.cancel();
            HashMap<String, Integer> foodHash = ((FoodHashAccessor) foodData).getFoodHash();
            LinkedList<String> foodQueue = ((FoodHashAccessor) foodData).getFoodQueue();
            if (!foodQueue.contains(blockString)){
                foodHash.put(blockString, 0);
            }
            int eaten = foodHash.get(blockString);
            if ((int)(2 * Math.pow(0.7,eaten))<=0 && !Config.cakeEatableAnyway){
                cir.setReturnValue(InteractionResult.PASS);
            }
            else {
                foodData.eat((int)(2 * Math.pow(0.7,eaten)), 0.1F);
                if (foodQueue.size() >= ((FoodHashAccessor)foodData).getFoodHistory()) {
                    String elem = foodQueue.pollFirst();
                    foodHash.put(elem, foodHash.get(elem) - 1);
                }
                foodHash.put(blockString, foodHash.get(blockString)+1);
                foodQueue.add(blockString);
                int i = p_51188_.getValue(BITES);
                p_51186_.gameEvent(p_51189_, GameEvent.EAT, p_51187_);
                if (i < 6) {
                    p_51186_.setBlock(p_51187_, p_51188_.setValue(BITES, Integer.valueOf(i + 1)), 3);
                } else {
                    p_51186_.removeBlock(p_51187_, false);
                    p_51186_.gameEvent(p_51189_, GameEvent.BLOCK_DESTROY, p_51187_);
                }

                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }
}
