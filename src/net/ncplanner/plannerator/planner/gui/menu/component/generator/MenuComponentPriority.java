package net.ncplanner.plannerator.planner.gui.menu.component.generator;
import net.ncplanner.plannerator.graphics.Renderer;
import net.ncplanner.plannerator.multiblock.generator.Priority;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.gui.Component;
public class MenuComponentPriority extends Component{
    public final Priority priority;
    public MenuComponentPriority(Priority priority){
        super(0, 0, 0, 32);
        this.priority = priority;
    }
    @Override
    public void draw(double deltaTime){
        Renderer renderer = new Renderer();
        if(isFocused){
            if(isMouseFocused)renderer.setColor(Core.theme.getMouseoverSelectedComponentColor(Core.getThemeIndex(this)));
            else renderer.setColor(Core.theme.getSelectedComponentColor(Core.getThemeIndex(this)));
        }else{
            if(isMouseFocused)renderer.setColor(Core.theme.getMouseoverComponentColor(Core.getThemeIndex(this)));
            else renderer.setColor(Core.theme.getComponentColor(Core.getThemeIndex(this)));
        }
        renderer.fillRect(x, y, x+width, y+height);
        renderer.setColor(Core.theme.getComponentTextColor(Core.getThemeIndex(this)));
        drawText(renderer);
    }
    public void drawText(Renderer renderer){
        float textLength = renderer.getStringWidth(priority.name, height);
        float scale = Math.min(1, width/textLength);
        float textHeight = (int)(height*scale)-1;
        renderer.drawCenteredText(x, y+height/2-textHeight/2, x+width, y+height/2+textHeight/2, priority.name);
    }
}