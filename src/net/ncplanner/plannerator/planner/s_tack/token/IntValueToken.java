package net.ncplanner.plannerator.planner.s_tack.token;
import net.ncplanner.plannerator.planner.s_tack.Script;
import net.ncplanner.plannerator.planner.s_tack.object.StackInt;
public class IntValueToken extends Token{
    public int value;
    public IntValueToken(){
        super("(?>\\-[0-9][0-9]?[0-9]?[0-9]?[0-9]?[0-9]?[0-9]?[0-9]?[0-9]?[0-8]?|[+-]?[0-9][0-9]?[0-9]?[0-9]?[0-9]?[0-9]?[0-9]?[0-9]?[0-9]?[0-7]?)");//(sign+"?"+digit+"+")
    }
    @Override
    public Token newInstance(){
        return new IntValueToken();
    }
    @Override
    public void load(){
        value = Integer.parseInt(text);
    }
    @Override
    public void run(Script script){
        script.stack.push(new StackInt(value));
    }
}