package ru.markthelark.spiceofoverhaul;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;


//implement configs
@Mod.EventBusSubscriber(modid = SpiceOfOverhaul.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_SOL_MODULE = BUILDER
            .comment("Spice of Life system of lowering food stats when it is being eaten too often")
            .define("enableSOLModule", true);

    private static final ForgeConfigSpec.ConfigValue<Integer> HISTORY_LENGTH = BUILDER
            .comment("Number of eaten foods that will affect food values of next item eaten")
            .defineInRange("historyLength", 20, 1, 1000);
    private static final ForgeConfigSpec.ConfigValue<String> HUNGER_FORMULA = BUILDER
            .comment("You can change hunger formula dependence on circumstances")
            .comment("Use JavaScript Math module functions for mathematical functions")
            .comment("Possible inputs: HUNGER, EATEN, SATURATION")
            .define("hunger", "HUNGER*Math.pow(0.7,EATEN)");
    private static final ForgeConfigSpec.ConfigValue<String> SATURATION_FORMULA = BUILDER
            .comment("You can change saturation formula dependence on circumstances")
            .comment("Use JavaScript Math module functions for mathematical functions")
            .comment("Possible inputs: HUNGER, EATEN, SATURATION")
            .define("saturation", "SATURATION");
    private static final ForgeConfigSpec.ConfigValue<Boolean> IDLE_EXHAUSTION = BUILDER
            .comment("Configured number of exhaustion will be applied constantly")
            .define("idleExhaustion", true);
    private static final ForgeConfigSpec.ConfigValue<Double> IDLE_EXHAUSTION_QUANTITY = BUILDER
            .comment("The number which will be added to exhaustion each tick")
            .defineInRange("idleExhaustionQuantity", 0.01f, 0.0f, 4.0f);

    private static final ForgeConfigSpec.ConfigValue<Boolean> MODIFY_FOOD_EATING_SPEED = BUILDER
            .comment("If eating speed affected by food values")
            .define("modifyFoodEatingSpeed", true);
    private static final ForgeConfigSpec.ConfigValue<Boolean> GUI_TEXT = BUILDER
            .comment("adds text to GUI describing health and hunger status")
            .define("addGuiText", true);
    private static final ForgeConfigSpec.ConfigValue<Boolean> LOW_HEALTH_STRIKES = BUILDER
            .comment("Strikes for having low hp")
            .define("lowHealthStrikes", true);
    private static final ForgeConfigSpec.ConfigValue<Double> HEALTH_LEVEL = BUILDER
            .comment("Strikes for having low hp start at this health level")
            .defineInRange("healthLevel", 0.25f, 0.05f, 1f);
    private static final ForgeConfigSpec.ConfigValue<Boolean> LOW_HUNGER_STRIKES = BUILDER
            .comment("Strikes for having low hunger")
            .define("lowHungerStrikes", true);
    private static final ForgeConfigSpec.ConfigValue<Integer> HUNGER_LEVEL = BUILDER
            .comment("Strikes for having low hunger start at this hunger level")
            .defineInRange("hungerLevel", 5, 1, 20);
    private static final ForgeConfigSpec.ConfigValue<Boolean> HIGH_SATURATION_STRIKES = BUILDER
            .comment("Strikes for having high saturation")
            .define("highSaturationStrikes", true);
    private static final ForgeConfigSpec.ConfigValue<Double> SATURATION_LEVEL = BUILDER
            .comment("Strikes for having high saturation start at this saturation level")
            .defineInRange("saturationLevel", 15f, 0f, 20f);
    private static final ForgeConfigSpec.ConfigValue<Boolean> STRIKES_JUMP = BUILDER
            .comment("You cannot jump when having extreme strikes")
            .define("strikesJump", true);
    private static final ForgeConfigSpec.ConfigValue<Boolean> STRIKES_NAUSEA = BUILDER
            .comment("If Nausea effect should be applied")
            .define("strikesNausea", true);
    private static final ForgeConfigSpec.ConfigValue<Integer> STRIKES_DIFFICULTY_BASE = BUILDER
            .comment("This value is equal to highest effect amplifier when you have 1 hp/hunger")
            .defineInRange("strikesDifficultyBase", 1, 0, 10);
    private static final ForgeConfigSpec.ConfigValue<Integer> STRIKES_DIFFICULTY_SCALE = BUILDER
            .comment("This value will be added to effect amplifier for each difficulty level past PEACEFUL")
            .defineInRange("strikesDifficultyScale", 1, 0, 10);

    private static final ForgeConfigSpec.ConfigValue<Boolean> REGEN_REWORK = BUILDER
            .comment("Regeneration rate depends on certain circumstances")
            .define("regenRework", true);
    private static final ForgeConfigSpec.ConfigValue<Boolean> REGEN_HUNGER_ONLY = BUILDER
            .comment("Regeneration only requires hunger, not saturation")
            .define("regenHungerOnly", true);
    private static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_WELLFED = BUILDER
            .comment("Enable effect which increases regeneration rate")
            .define("enableWellFed", true);
    private static final ForgeConfigSpec.ConfigValue<Boolean> FOOD_EATABLE_ANYWAY = BUILDER
            .comment("Makes cake eatable even if it does not give any you benefit")
            .define("foodEatableAnyway", false);


    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean enableSOLModule;
    public static int historyLength;
    public static String hungerExpression;
    public static String saturationExpression;
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
    public static boolean strikesNausea;
    public static boolean regenRework;
    public static boolean regenHungerOnly;
    public static boolean enableWellFed;
    public static boolean foodEatableAnyway;

    public static int strikesDifficultyBase;
    public static int StrikesDifficultyScale;



    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        enableSOLModule = ENABLE_SOL_MODULE.get();
        historyLength = HISTORY_LENGTH.get();
        hungerExpression = HUNGER_FORMULA.get();
        saturationExpression = SATURATION_FORMULA.get();

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
        strikesNausea = STRIKES_NAUSEA.get();
        strikesDifficultyBase = STRIKES_DIFFICULTY_BASE.get();
        StrikesDifficultyScale = STRIKES_DIFFICULTY_SCALE.get();

        regenRework = REGEN_REWORK.get();
        regenHungerOnly = REGEN_HUNGER_ONLY.get();
        enableWellFed = ENABLE_WELLFED.get();
        foodEatableAnyway = FOOD_EATABLE_ANYWAY.get();

    }
}
