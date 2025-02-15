package net.ncplanner.plannerator.planner.s_tack.token.keyword;
import net.ncplanner.plannerator.planner.s_tack.Script;
import net.ncplanner.plannerator.planner.s_tack.object.StackMethod;
public class ExecKeyword extends Keyword{
    public ExecKeyword(){
        super("exec");
    }
    @Override
    public Keyword newInstance(){
        return new ExecKeyword();
    }
    @Override
    public void run(Script script){
        StackMethod method = script.stack.pop().asMethod();
        script.subscript(method.getValue());
    }
}