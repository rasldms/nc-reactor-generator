package planner.menu.component;
import multiblock.symmetry.Symmetry;
import planner.Core;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentSymmetry extends MenuComponent{
    public final Symmetry symmetry;
    public boolean enabled = false;
    public MenuComponentSymmetry(Symmetry symmetry){
        super(0, 0, 0, 32);
        this.symmetry = symmetry;
        enabled = symmetry.defaultEnabled();
    }
    @Override
    public void render(){
        if(isMouseOver&&!enabled)Core.applyAverageColor(Core.theme.getButtonColor(), Core.theme.getSelectedMultiblockColor());
        else Core.applyColor(enabled?Core.theme.getSelectedMultiblockColor():Core.theme.getButtonColor());
        drawRect(x, y, x+width, y+height, 0);
        Core.applyColor(Core.theme.getTextColor());
        drawText();
    }
    public void drawText(){
        double textLength = FontManager.getLengthForStringWithHeight(symmetry.name, height);
        double scale = Math.min(1, width/textLength);
        double textHeight = (int)(height*scale)-1;
        drawCenteredText(x, y+height/2-textHeight/2, x+width, y+height/2+textHeight/2, symmetry.name);
    }
    @Override
    public boolean mouseWheelChange(int wheelChange){
        return parent.mouseWheelChange(wheelChange);
    }
    @Override
    public void mouseEvent(int button, boolean pressed, float x, float y, float xChange, float yChange, int wheelChange){
        super.mouseEvent(button, pressed, x, y, xChange, yChange, wheelChange);
        if(button==0&&pressed){
            enabled = !enabled;
        }
    }
}