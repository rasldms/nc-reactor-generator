package planner.menu.component.editor;
import planner.Core;
import org.lwjgl.glfw.GLFW;
import planner.suggestion.Suggestor;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentSuggestor extends MenuComponent{
    public final Suggestor suggestor;
    public boolean enabled = false;
    public MenuComponentSuggestor(Suggestor suggestor){
        super(0, 0, 0, 64);
        this.suggestor = suggestor;
    }
    @Override
    public void render(){
        if(isMouseOver&&!enabled)Core.applyAverageColor(Core.theme.getButtonColor(), Core.theme.getSelectedMultiblockColor());
        else Core.applyColor(enabled?Core.theme.getSelectedMultiblockColor():Core.theme.getButtonColor());
        drawRect(x, y, x+width, y+height, 0);
        Core.applyColor(Core.theme.getTextColor());
        drawText(suggestor.getName()+" ("+(suggestor.isActive()?"On":"Off")+")");
    }
    public void drawText(String text){
        double textLength = FontManager.getLengthForStringWithHeight(text, height);
        double scale = Math.min(1, width/textLength);
        double textHeight = (int)(height*scale)-1;
        drawText(x, y+height/2-textHeight/2, x+width, y+height/2+textHeight/2, text);
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
        }
    }
}