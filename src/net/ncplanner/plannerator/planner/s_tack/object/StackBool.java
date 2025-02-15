package net.ncplanner.plannerator.planner.s_tack.object;
public class StackBool extends StackObject{
    private final Boolean value;
    public StackBool(boolean value){
        this.value = value;
    }
    @Override
    public Type getType(){
        return Type.BOOL;
    }
    @Override
    public Boolean getValue(){
        return value;
    }
    @Override
    public String toString(){
        return value.toString();
    }
    @Override
    public StackObject duplicate(){
        return new StackBool(value);
    }
}