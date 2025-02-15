package net.ncplanner.plannerator.planner.s_tack.token.operator;
import net.ncplanner.plannerator.planner.s_tack.Script;
import net.ncplanner.plannerator.planner.s_tack.object.StackObject;
import net.ncplanner.plannerator.planner.s_tack.token.Token;
public abstract class Operator extends Token{
    public Operator(String operator){
        super(escapify(operator));
    }
    @Override
    public abstract Operator newInstance();
    private static String escapify(String operator){
        String str = "";
        for(char c : operator.toCharArray()){
            str+="\\"+c;
        }
        return str;
    }
    @Override
    public final void load(){}
    @Override
    public final void run(Script script){
        StackObject v2 = script.stack.pop();
        StackObject v1 = script.stack.pop();
        StackObject ret = evaluate(v1, v2);
        if(ret!=null)script.stack.push(ret);
    }
    public abstract StackObject evaluate(StackObject v1, StackObject v2);
}