package net.ncplanner.plannerator.planner.s_tack.object;
public class StackChar extends StackObject{
    private final Character value;
    public StackChar(char value){
        this.value = value;
    }
    @Override
    public Type getType(){
        return Type.CHAR;
    }
    @Override
    public Character getValue(){
        return value;
    }
    @Override
    public String toString(){
        return "'"+value.toString()+"'";
    }
    @Override
    public StackObject duplicate(){
        return new StackChar(value);
    }
}