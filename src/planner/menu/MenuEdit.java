package planner.menu;
import planner.multiblock.Multiblock;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
public class MenuEdit extends Menu{
    private final Multiblock multiblock;
    public MenuEdit(GUI gui, Menu parent, Multiblock multiblock){
        super(gui, parent);
        this.multiblock = multiblock;
    }
}