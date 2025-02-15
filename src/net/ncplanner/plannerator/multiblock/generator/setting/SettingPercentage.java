package net.ncplanner.plannerator.multiblock.generator.setting;
import net.ncplanner.plannerator.planner.menu.component.MenuComponentMinimaList;
import net.ncplanner.plannerator.planner.menu.component.MenuComponentMinimalistTextBox;
public class SettingPercentage extends SettingDouble{
    public SettingPercentage(String name, double defaultValue, String tooltip){
        super(name, 0d, 100d, defaultValue*100, tooltip);
    }
    @Override
    public void buildComponents(MenuComponentMinimaList generatorSettings){
        generatorSettings.add(new MenuComponentMinimalistTextBox(0, 0, 0, 48, value+"", true, name){
            @Override
            public void keyEvent(int key, int scancode, boolean isPress, boolean isRepeat, int modifiers){
                super.keyEvent(key, scancode, isPress, isRepeat, modifiers);
                value = Double.parseDouble(text);
            }
            @Override
            public void onCharTyped(char c){
                super.onCharTyped(c);
                value = Double.parseDouble(text);
            }
        }.setDoubleFilter(min, max).setTooltip(tooltip).setSuffix("%"));
    }
    @Override
    public Double getValue(){
        return super.getValue()/100;
    }
    @Override
    public void setValue(Double value){
        super.setValue(value*100);
    }
}