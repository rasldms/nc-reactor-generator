package net.ncplanner.plannerator.planner.s_tack.token;
import java.util.regex.Pattern;
import net.ncplanner.plannerator.planner.s_tack.Script;
import static net.ncplanner.plannerator.planner.s_tack.token.Helpers.separator;
public abstract class Token{
    public final String regex;
    public String text;
    private Pattern pattern;
    public Token(String regex){
        this(regex, false);
    }
    public Token(String regex, boolean plain){
        this.regex = plain?regex:("(?<=^|"+separator+")"+regex+"(?=$|"+separator+")");
    }
    public abstract Token newInstance();
    public Token newInstance(String text){
        Token token = newInstance();
        token.text = text;
        token.load();
        return token;
    }
    public void load(){}
    public void run(Script script){
        throw new UnsupportedOperationException("Missing token handling for token "+getClass().getName());
    }
    public Pattern getStartPattern(){
        if(pattern!=null)return pattern;
        return pattern = Pattern.compile("^"+regex);
    }
}