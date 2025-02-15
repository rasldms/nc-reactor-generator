package net.ncplanner.plannerator.planner.menu.dialog;
import java.util.ArrayList;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.menu.component.MenuComponentMinimalistButton;
import net.ncplanner.plannerator.planner.menu.component.MenuComponentMinimalistScrollable;
import net.ncplanner.plannerator.planner.menu.component.MenuComponentTextDisplay;
import simplelibrary.opengl.gui.ActionListener;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuDialog extends Menu{
    private MenuComponentMinimalistScrollable textPanel = add(new MenuComponentMinimalistScrollable(0, 0, 0, 0, 16, 16));
    public MenuComponentTextDisplay textBox = textPanel.add(new MenuComponentTextDisplay(""));
    public MenuComponent content = textBox;
    public double maxWidth = 0.5d;
    public double maxHeight = 0.5d;
    public double minWidth = 0.25d;
    public double minHeight = 0.1d;
    public int border = 8;
    public int buttonHeight = 64;
    private ArrayList<MenuComponentMinimalistButton> buttons = new ArrayList<>();
    public MenuDialog(GUI gui, Menu parent){
        super(gui, parent);
    }
    @Override
    public void render(int millisSinceLastTick){
        Renderer renderer = new Renderer();
        try{
            if(parent!=null)parent.render(millisSinceLastTick);
        }catch(Exception ignored){}
        renderer.setColor(Core.theme.getDialogBorderColor());
        double w = Math.max(gui.helper.displayWidth()*minWidth, Math.min(gui.helper.displayWidth()*maxWidth, content.width));
        double h = Math.max(gui.helper.displayHeight()*minHeight, Math.min(gui.helper.displayHeight()*maxHeight, content.height));
        drawRect(gui.helper.displayWidth()/2-w/2-border, gui.helper.displayHeight()/2-h/2-border, gui.helper.displayWidth()/2+w/2+border, gui.helper.displayHeight()/2+h/2+border+buttonHeight, 0);
        renderer.setColor(Core.theme.getDialogBackgroundColor());
        drawRect(gui.helper.displayWidth()/2-w/2, gui.helper.displayHeight()/2-h/2, gui.helper.displayWidth()/2+w/2, gui.helper.displayHeight()/2+h/2, 0);
        renderer.setWhite();
        textPanel.x = gui.helper.displayWidth()/2-w/2;
        textPanel.y = gui.helper.displayHeight()/2-h/2;
        textPanel.width = w;
        textPanel.height = h;
        for(int i = 0; i<buttons.size(); i++){
            buttons.get(i).width = w/buttons.size();
            buttons.get(i).x = gui.helper.displayWidth()/2-w/2+buttons.get(i).width*i;
            buttons.get(i).y = gui.helper.displayHeight()/2+h/2;
        }
        super.render(millisSinceLastTick);
    }
    public void close(){
        gui.menu = parent;
    }
    public void addButton(String text, ActionListener onClick){
        MenuComponentMinimalistButton b = new MenuComponentMinimalistButton(0, 0, 0, buttonHeight, text, true, true);
        b.addActionListener(onClick);
        buttons.add(add(b));
    }
    public <T extends MenuComponent> T setContent(T component){
        textPanel.components.remove(content);
        content = textPanel.add(component);
        return component;
    }
}