package net.ncplanner.plannerator.planner.menu;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.menu.component.MenuComponentMinimaList;
import net.ncplanner.plannerator.planner.menu.component.MenuComponentMinimalistButton;
import net.ncplanner.plannerator.planner.menu.component.MenuComponentModule;
import net.ncplanner.plannerator.planner.module.Module;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
public class MenuModules extends Menu{
    private final MenuComponentMinimaList list = add(new MenuComponentMinimaList(0, 0, 0, 0, 50));
    private final MenuComponentMinimalistButton done = add(new MenuComponentMinimalistButton(0, 0, 0, 0, "Done", true, true));
    public MenuModules(GUI gui, Menu parent){
        super(gui, parent);
        done.addActionListener((e) -> {
            gui.open(new MenuSettings(gui, parent.parent));
        });
    }
    @Override
    public void onGUIOpened(){
        list.components.clear();
        for(Module m : Core.modules){
            list.add(new MenuComponentModule(m));
        }
    }
    @Override
    public void render(int millisSinceLastTick){
        list.width = gui.helper.displayWidth();
        list.height = gui.helper.displayHeight()-done.height;
        for(simplelibrary.opengl.gui.components.MenuComponent component : list.components){
            component.width = list.width-(list.hasVertScrollbar()?list.vertScrollbarWidth:0);
        }
        done.width = gui.helper.displayWidth();
        done.height = gui.helper.displayHeight()/16;
        done.y = gui.helper.displayHeight()-done.height;
        super.render(millisSinceLastTick);
    }
}