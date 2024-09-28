package ru.markthelark.spiceofoverhaul.util;
import ru.markthelark.spiceofoverhaul.Config;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
public class FormulaProvider {
    public String expression;
//    public final void FormulaProvider(){
//        this.expression = Config.magicExpression.replaceAll("(\\w+)(\\^)(\\w+)", "Math.pow($1,$2");;
//    }
    public final int formula(int hunger, float saturation, int eaten) throws ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        engine.put("H", hunger);
        engine.put("E", eaten);
        engine.put("S", saturation);
        return (int)engine.eval(this.expression);
    }
}