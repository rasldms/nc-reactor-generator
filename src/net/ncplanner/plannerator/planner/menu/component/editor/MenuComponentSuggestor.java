package net.ncplanner.plannerator.planner.menu.component.editor;
import java.util.ArrayList;
import java.util.Arrays;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.Pinnable;
import net.ncplanner.plannerator.planner.editor.suggestion.Suggestor;
import net.ncplanner.plannerator.planner.menu.MenuEdit;
import org.lwjgl.glfw.GLFW;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentSuggestor extends MenuComponent implements Pinnable{
    private final MenuEdit editor;
    public final Suggestor suggestor;
    public boolean enabled = false;
    public MenuComponentSuggestor(MenuEdit editor, Suggestor suggestor){
        super(0, 0, 0, 64);
        this.editor = editor;
        this.suggestor = suggestor;
    }
    @Override
    public void render(){
        Renderer renderer = new Renderer();
        if(isSelected){
            if(isMouseOver)renderer.setColor(Core.theme.getMouseoverSelectedComponentColor(Core.getThemeIndex(this)));
            else renderer.setColor(Core.theme.getSelectedComponentColor(Core.getThemeIndex(this)));
        }else{
            if(isMouseOver)renderer.setColor(Core.theme.getMouseoverComponentColor(Core.getThemeIndex(this)));
            else renderer.setColor(Core.theme.getComponentColor(Core.getThemeIndex(this)));
        }
        drawRect(x, y, x+width, y+height, 0);
        renderer.setColor(Core.theme.getComponentTextColor(Core.getThemeIndex(this)));
        drawText(suggestor.name+" ("+(suggestor.isActive()?"On":"Off")+")");
    }
    public void drawText(String text){
        double textLength = FontManager.getLengthForStringWithHeight(text, height);
        double scale = Math.min(1, width/textLength);
        double textHeight = (int)(height*scale)-1;
        drawCenteredText(x, y+height/2-textHeight/2, x+width, y+height/2+textHeight/2, text);
    }
    @Override
    public String getTooltip(){
        return suggestor.getDescription();
    }
    @Override
    public void onMouseButton(double x, double y, int button, boolean pressed, int mods){
        super.onMouseButton(x, y, button, pressed, mods);
        if(button==GLFW.GLFW_MOUSE_BUTTON_LEFT&&pressed){
            enabled = !enabled;
            suggestor.setActive(enabled);
            editor.recalculateSuggestions();
        }
    }
    @Override
    public ArrayList<String> getSearchableNames(){
        ArrayList<String> lst = getSimpleSearchableNames();
        for(String s : getTooltip().split("\n"))lst.add(s.trim());
        return lst;
    }
    @Override
    public ArrayList<String> getSimpleSearchableNames(){
        ArrayList<String> lst = new ArrayList<>(Arrays.asList(suggestor.name));
        return lst;
    }
    @Override
    public String getPinnedName(){
        return suggestor.name;
    }
}