package ru.markthelark.spiceofoverhaul;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

//implement configs
@EventBusSubscriber(modid = SpiceOfOverhaul.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.ConfigValue<Boolean> ENABLE_SOL_MODULE = BUILDER
            .comment("Spice of Life system of lowering food stats when it is being eaten too often")
            .define("enableSOLModule", true);

    private static final ModConfigSpec.ConfigValue<Integer> HISTORY_LENGTH = BUILDER
            .comment("Number of eaten foods that will affect food values of next item eaten")
            .defineInRange("historyLength", 20, 1, 1000);
    private static final ModConfigSpec.ConfigValue<Boolean> IDLE_EXHAUSTION = BUILDER
            .comment("Configured number of exhaustion will be applied constantly")
            .define("idleExhaustion", true);
    private static final ModConfigSpec.ConfigValue<Double> IDLE_EXHAUSTION_QUANTITY = BUILDER
            .comment("The number which will be added to exhaustion each tick")
            .defineInRange("idleExhaustionQuantity", 0.01f, 0.0f, 4.0f);

    private static final ModConfigSpec.ConfigValue<Boolean> MODIFY_FOOD_EATING_SPEED = BUILDER
            .comment("If eating speed affected by food values")
            .define("modifyFoodEatingSpeed", true);
    private static final ModConfigSpec.ConfigValue<Boolean> GUI_TEXT = BUILDER
            .comment("adds text to GUI describing health and hunger status")
            .define("addGuiText", true);
    private static final ModConfigSpec.ConfigValue<Boolean> LOW_HEALTH_STRIKES = BUILDER
            .comment("Strikes for having low hp")
            .define("lowHealthStrikes", true);
    private static final ModConfigSpec.ConfigValue<Double> HEALTH_LEVEL = BUILDER
            .comment("Strikes for having low hp start at this health level")
            .defineInRange("healthLevel", 0.25f, 0.05f, 1f);
    private static final ModConfigSpec.ConfigValue<Boolean> LOW_HUNGER_STRIKES = BUILDER
            .comment("Strikes for having low hunger")
            .define("lowHungerStrikes", true);
    private static final ModConfigSpec.ConfigValue<Integer> HUNGER_LEVEL = BUILDER
            .comment("Strikes for having low hunger start at this hunger level")
            .defineInRange("hungerLevel", 5, 1, 20);
    private static final ModConfigSpec.ConfigValue<Boolean> HIGH_SATURATION_STRIKES = BUILDER
            .comment("Strikes for having high saturation")
            .define("highSaturationStrikes", true);
    private static final ModConfigSpec.ConfigValue<Double> SATURATION_LEVEL = BUILDER
            .comment("Strikes for having high saturation start at this saturation level")
            .defineInRange("saturationLevel", 15f, 0f, 20f);
    private static final ModConfigSpec.ConfigValue<Boolean> STRIKES_JUMP = BUILDER
            .comment("You cannot jump when having extreme strikes")
            .define("strikesJump", true);
    private static final ModConfigSpec.ConfigValue<Integer> STRIKES_DIFFICULTY_BASE = BUILDER
            .comment("This value is equal to highest effect amplifier when you have 1 hp/hunger")
            .defineInRange("strikesDifficultyBase", 1, 0, 10);
    private static final ModConfigSpec.ConfigValue<Integer> STRIKES_DIFFICULTY_SCALE = BUILDER
            .comment("This value will be added to effect amplifier for each difficulty level past PEACEFUL")
            .defineInRange("strikesDifficultyScale", 1, 0, 10);

    private static final ModConfigSpec.ConfigValue<Boolean> REGEN_REWORK = BUILDER
            .comment("Regeneration only requires hunger, not saturation")
            .define("regenRework", true);
    private static final ModConfigSpec.ConfigValue<Boolean> ENABLE_WELLFED = BUILDER
            .comment("Enable effect which increases regeneration rate")
            .define("enableWellFed", true);
    private static final ModConfigSpec.ConfigValue<Boolean> CAKE_EATABLE_ANYWAY = BUILDER
            .comment("Makes cake eatable even if it does not give any hunger")
            .define("cakeEatableAnyway", false);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean enableSOLModule;
    public static boolean idleExhaustion;
    public static boolean modifyFoodEatingSpeed;
    public static float idleExhaustionQuantity;
    public static boolean addGuiText;
    public static boolean lowHealthStrikes;
    public static float healthLevel;
    public static boolean lowHungerStrikes;
    public static int hungerLevel;
    public static boolean highSaturationStrikes;
    public static float saturationLevel;
    public static boolean strikesJump;
    public static boolean regenRework;
    public static boolean enableWellFed;
    public static boolean cakeEatableAnyway;
    public static int historyLength;
    public static int strikesDifficultyBase;
    public static int StrikesDifficultyScale;
//    public static String magicExpression;

//    private static boolean validateItemName(final Object obj)
//    {
//        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
//    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        enableSOLModule = ENABLE_SOL_MODULE.get();
        historyLength = HISTORY_LENGTH.get();

        idleExhaustion = IDLE_EXHAUSTION.get();
        idleExhaustionQuantity = IDLE_EXHAUSTION_QUANTITY.get().floatValue();

        modifyFoodEatingSpeed = MODIFY_FOOD_EATING_SPEED.get();
        addGuiText =GUI_TEXT.get();

        lowHungerStrikes =LOW_HUNGER_STRIKES.get();
        hungerLevel = HUNGER_LEVEL.get();
        lowHealthStrikes = LOW_HEALTH_STRIKES.get();
        healthLevel = HEALTH_LEVEL.get().floatValue();
        highSaturationStrikes = HIGH_SATURATION_STRIKES.get();
        saturationLevel = SATURATION_LEVEL.get().floatValue();

        strikesJump = STRIKES_JUMP.get();
        strikesDifficultyBase = STRIKES_DIFFICULTY_BASE.get();
        StrikesDifficultyScale = STRIKES_DIFFICULTY_SCALE.get();

        regenRework = REGEN_REWORK.get();
        enableWellFed = ENABLE_WELLFED.get();
        cakeEatableAnyway = CAKE_EATABLE_ANYWAY.get();

    }
}
