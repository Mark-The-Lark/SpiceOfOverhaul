package ru.markthelark.spiceofoverhaul.items;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.Nullable;
import ru.markthelark.spiceofoverhaul.util.FoodHashAccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoodBag extends BundleItem implements IForgeItem {
    public FoodBag(Properties pProperties) {
        super(pProperties);
    }
//    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        CompoundTag compoundtag = pStack.getOrCreateTag();
        if (!compoundtag.contains("Items")){
            return pStack;
        }
        ListTag listtag = compoundtag.getList("Items", 10);
        ItemStack itemToUse = nextToEat(pStack, pEntityLiving);
        if ((pEntityLiving instanceof Player)) {
            Player player = (Player)pEntityLiving;
            FoodData foodData = player.getFoodData();
            if (foodData instanceof FoodHashAccessor) {
                ((FoodHashAccessor)foodData).eat(itemToUse.getItem(), itemToUse, player);
            }
            else {
                foodData.eat(itemToUse.getItem(), itemToUse, player);
            }
            player.awardStat(Stats.ITEM_USED.get(itemToUse.getItem()));
            player.level().playSound(
                    null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, player.level().random.nextFloat() * 0.1F + 0.9F
            );
        }
        CompoundTag compoundtag1 = new CompoundTag();
        itemToUse.save(compoundtag1);
        int index = listtag.indexOf(compoundtag1);
        listtag.remove(index);
        if (itemToUse.getCount()>1) {
            itemToUse.shrink(1);
            CompoundTag compoundtag2 = new CompoundTag();
            itemToUse.save(compoundtag2);
            listtag.add(index, compoundtag2);
        }
        compoundtag.remove("Items");
        compoundtag.put("Items", listtag);
        pStack.setTag(compoundtag);
        return pStack;
    }
    @Override
    public int getUseDuration(ItemStack pStack) {
//        ItemStack itemStack = nextToEat(pStack, pEntity);
//        if (itemStack == null){return 1;}
        return 10;
    }
    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.EAT;
    }
    @Override
    public InteractionResultHolder<ItemStack> use (Level level, Player player, InteractionHand pUsedHand){
        ItemStack itemstack = player.getItemInHand(pUsedHand);
        ItemStack itemToUse = nextToEat(itemstack, player);
        if (itemToUse == null){
            return InteractionResultHolder.fail(itemstack);
        }
        FoodProperties foodproperties = itemToUse.getFoodProperties(player);
        if (foodproperties != null) {
            if (player.canEat(foodproperties.canAlwaysEat())) {
                player.startUsingItem(pUsedHand);
                return InteractionResultHolder.success(itemstack);
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        } else {
            return InteractionResultHolder.pass(player.getItemInHand(pUsedHand));
        }
    }
    public static ItemStack nextToEat(ItemStack itemStack, LivingEntity entity){
        if (!(entity instanceof Player)){
            return null;
        }
        Player player = (Player) entity;
        CompoundTag compoundtag = itemStack.getOrCreateTag();
        FoodData foodData = player.getFoodData();
        if (!(foodData instanceof FoodHashAccessor) || !compoundtag.contains("Items")){
            return null;
        }
        HashMap<String, Integer> foodHash = ((FoodHashAccessor)foodData).getFoodHash();
        List<ItemStack> items = new ArrayList<>();
        ListTag listtag = compoundtag.getList("Items", 10);
        for(int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag1 = listtag.getCompound(i);
            ItemStack itemstack = ItemStack.of(compoundtag1);
            items.add(itemstack);
        }
        ItemStack itemToUse = null;
        int eaten = 21;
        for (ItemStack item: items
        ) {
            String itemString = (item.getItem().getCreatorModId(item) + ":" + item.getItem().toString().replace(" ", ""));
            if (foodHash.get(itemString) == null && item.isEdible()){
                itemToUse = item;
                break;
            }
            else if (foodHash.get(itemString)<=eaten){
                itemToUse = item;
                eaten = foodHash.get(itemString);
            }
        }
        return itemToUse;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack pStack, Slot pSlot, ClickAction pAction, Player pPlayer) {
        ItemStack itemStack = pSlot.getItem();
        if (itemStack.isEmpty() || itemStack.isEdible()){
            return super.overrideStackedOnOther(pStack,pSlot,pAction,pPlayer);
        }
        return false;
    }

    @Override
    public boolean overrideOtherStackedOnMe(
            ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        if (pOther.isEmpty() || pOther.isEdible()){
            return super.overrideOtherStackedOnMe(pStack,pOther,pSlot,pAction,pPlayer,pAccess);
        }
        return false;
    }

    @Override // read javadoc to find a potential problem
    @Nullable
    public FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        ItemStack itemToUse = nextToEat(stack, entity);
        if (itemToUse == null) {return null;}
        else {
            return itemToUse.getFoodProperties(entity);
        }
    }
}
