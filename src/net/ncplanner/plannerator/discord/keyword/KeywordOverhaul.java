package net.ncplanner.plannerator.discord.keyword;
import net.ncplanner.plannerator.discord.Keyword;
import net.ncplanner.plannerator.planner.Core;
import simplelibrary.image.Color;
public class KeywordOverhaul extends Keyword{
    public KeywordOverhaul(){
        super("Overhaul");
    }
    @Override
    public boolean doRead(String input){
        return true;
    }
    @Override
    public Color getColor(){
        return Core.theme.getKeywordColorOverhaul();
    }
    @Override
    public String getRegex(){
        return "((?<!pre[ -])(?<!pre)overhaul)";
    }
    @Override
    public Keyword newInstance(){
        return new KeywordOverhaul();
    }
    @Override
    public boolean caseSensitive(){
        return false;
    }
}