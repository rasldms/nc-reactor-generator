package net.ncplanner.plannerator.planner.menu.component;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.planner.Core;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentToggleBox extends MenuComponent{
    public String text;
    private final boolean darker;
    private double textInset = 4;
    public boolean isToggledOn = false;//because isSelected is taken
    private double boxInset = 0.15;
    public boolean enabled = true;
    public MenuComponentToggleBox(double x, double y, double width, double height, String label){
        this(x, y, width, height, label, false);
    }
    public MenuComponentToggleBox(double x, double y, double width, double height, String label, boolean isToggledOn){
        this(x, y, width, height, label, isToggledOn, false);
    }
    public MenuComponentToggleBox(double x, double y, double width, double height, String label, boolean isToggledOn, boolean darker){
        super(x, y, width, height);
        this.isToggledOn = isToggledOn;
        this.text = label;
        this.darker = darker;
    }
    @Override
    public void render(){
        Renderer renderer = new Renderer();
        renderer.setColor(darker?Core.theme.getSecondaryComponentColor(Core.getThemeIndex(this)):Core.theme.getComponentColor(Core.getThemeIndex(this)));
        drawRect(x, y, x+width, y+height, 0);//why is this here?
        renderer.setColor(darker?Core.theme.getSecondaryToggleBoxBorderColor(Core.getThemeIndex(this)):Core.theme.getToggleBoxBorderColor(Core.getThemeIndex(this)));
        drawRect(x, y, x+height, y+height, 0);
        renderer.setColor(isToggledOn?Core.theme.getToggleBoxMouseoverColor(Core.getThemeIndex(this)):Core.theme.getToggleBoxBackgroundColor(Core.getThemeIndex(this)));
        drawRect(x+boxInset*height, y+boxInset*height, x+height-boxInset*height, y+height-boxInset*height, 0);
        if(isMouseOver&&!enabled){
            renderer.setColor(Core.theme.getToggleBoxSelectedColor(Core.getThemeIndex(this)), .25f);
            drawRect(x+boxInset*height, y+boxInset*height, x+height-boxInset*height, y+height-boxInset*height, 0);
        }
        renderer.setColor(Core.theme.getComponentTextColor(Core.getThemeIndex(this)));
        drawText();
    }
    public void drawText(){
        double textLength = FontManager.getLengthForStringWithHeight(text, height);
        double scale = Math.min(1, (width-height-textInset*2)/textLength);
        double textHeight = (int)((height-textInset*2)*scale)-4;
        drawText(x+height+textInset, y+height/2-textHeight/2, x+width-textInset, y+height/2+textHeight/2, text);
    }
    @Override
    public MenuComponentToggleBox setTooltip(String tooltip){
        this.tooltip = tooltip;
        return this;
    }
    @Override
    public void onMouseButton(double x, double y, int button, boolean pressed, int mods){
        super.onMouseButton(x, y, button, pressed, mods);
        if(button==0&&pressed&&enabled)isToggledOn = !isToggledOn;
    }
}