package net.ncplanner.plannerator.planner.menu.configuration.overhaul.fusion;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.multiblock.configuration.overhaul.fusion.Recipe;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.menu.component.MenuComponentMinimalistButton;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentRecipe extends MenuComponent{
    public final Recipe recipe;
    public final MenuComponentMinimalistButton edit = add(new MenuComponentMinimalistButton(0, 0, 0, 0, "", true, true, true){
        @Override
        public void renderForeground(){
            Renderer renderer = new Renderer();
            super.renderForeground();
            renderer.setColor(Core.theme.getComponentTextColor(Core.getThemeIndex(this)));
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex2d(x+width*.25, y+height*.75);
            GL11.glVertex2d(x+width*.375, y+height*.75);
            GL11.glVertex2d(x+width*.25, y+height*.625);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2d(x+width*.4, y+height*.725);
            GL11.glVertex2d(x+width*.275, y+height*.6);
            GL11.glVertex2d(x+width*.5, y+height*.375);
            GL11.glVertex2d(x+width*.625, y+height*.5);

            GL11.glVertex2d(x+width*.525, y+height*.35);
            GL11.glVertex2d(x+width*.65, y+height*.475);
            GL11.glVertex2d(x+width*.75, y+height*.375);
            GL11.glVertex2d(x+width*.625, y+height*.25);
            GL11.glEnd();
        }
        @Override
        public void action(){
            onEditPressed.run();
        }
    }.setTooltip("Modify recipe"));
    public final MenuComponentMinimalistButton delete = add(new MenuComponentMinimalistButton(0, 0, 0, 0, "", true, true, true){
        @Override
        public void renderForeground(){
            Renderer renderer = new Renderer();
            super.renderForeground();
            renderer.setColor(Core.theme.getComponentTextColor(Core.getThemeIndex(this)));
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2d(x+width*.1, y+height*.8);
            GL11.glVertex2d(x+width*.2, y+height*.9);
            GL11.glVertex2d(x+width*.9, y+height*.2);
            GL11.glVertex2d(x+width*.8, y+height*.1);
            
            GL11.glVertex2d(x+width*.1, y+height*.2);
            GL11.glVertex2d(x+width*.2, y+height*.1);
            GL11.glVertex2d(x+width*.9, y+height*.8);
            GL11.glVertex2d(x+width*.8, y+height*.9);
            GL11.glEnd();
        }
        @Override
        public void action(){
            onDeletePressed.run();
        }
    }.setTooltip("Delete recipe"));
    private final Runnable onEditPressed;
    private final Runnable onDeletePressed;
    public MenuComponentRecipe(Recipe recipe, Runnable onEditPressed, Runnable onDeletePressed){
        super(0, 0, 0, 100);
        this.recipe = recipe;
        this.onEditPressed = onEditPressed;
        this.onDeletePressed = onDeletePressed;
    }
    @Override
    public void renderBackground(){
        super.renderBackground();
        delete.x = width-height/2-height/4;
        edit.x = delete.x - height;
        delete.y = edit.y = height/4;
        delete.width = delete.height = edit.width = edit.height = height/2;
    }
    @Override
    public void render(){
        Renderer renderer = new Renderer();
        if(isMouseOver)renderer.setColor(Core.theme.getMouseoverUnselectableComponentColor(Core.getThemeIndex(this)));
        else renderer.setColor(Core.theme.getComponentColor(Core.getThemeIndex(this)));
        drawRect(x, y, x+width, y+height, 0);
    }
    @Override
    public void renderForeground(){
        Renderer renderer = new Renderer();
        renderer.setWhite();
        if(recipe.inputTexture!=null)drawRect(x, y, x+height, y+height, Core.getTexture(recipe.inputDisplayTexture));
        if(recipe.outputTexture!=null)drawRect(x+height, y, x+height*2, y+height, Core.getTexture(recipe.outputDisplayTexture));
        renderer.setColor(Core.theme.getComponentTextColor(Core.getThemeIndex(this)));
        drawText(x+height*2, y, x+width, y+height/6, recipe.getInputDisplayName());
        drawText(x+height*2, y+height/6, x+width, y+height/6*2, recipe.getOutputDisplayName());
        drawText(x+height*2, y+height/6*2, x+width, y+height/6*3, "Efficiency: "+recipe.efficiency);
        drawText(x+height*2, y+height/6*3, x+width, y+height/6*4, "Heat: "+recipe.heat);
        drawText(x+height*2, y+height/6*4, x+width, y+height/6*5, "Time: "+recipe.time);
        drawText(x+height*2, y+height/6*5, x+width, y+height/6*6, "Fluxiness: "+recipe.fluxiness);
    }
}
