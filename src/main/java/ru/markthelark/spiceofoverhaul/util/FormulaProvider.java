package ru.markthelark.spiceofoverhaul.util;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import ru.markthelark.spiceofoverhaul.Config;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
public class FormulaProvider {
    public static final int FormulaHunger(int hunger, float saturation, int eaten){
        ScriptEngineFactory mgr = new NashornScriptEngineFactory();
        ScriptEngine engine = mgr.getScriptEngine();
        engine.put("HUNGER", hunger);
        engine.put("EATEN", eaten);
        engine.put("SATURATION", saturation);
        try {
            return Math.round(((Number)(engine.eval(Config.hungerExpression))).floatValue());
        } catch (ScriptException e) {
            return hunger;
        }

    }
    public static final float FormulaSaturation(int hunger, float saturation, int eaten){
        ScriptEngineFactory mgr = new NashornScriptEngineFactory();
        ScriptEngine engine = mgr.getScriptEngine();
        engine.put("HUNGER", hunger);
        engine.put("EATEN", eaten);
        engine.put("SATURATION", saturation);
        try {
            return ((Number)(engine.eval(Config.saturationExpression))).floatValue();
        } catch (ScriptException e) {
            return saturation;
        }

    }
}