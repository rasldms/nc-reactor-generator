package planner.menu;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planner.Core;
import planner.menu.component.MenuComponentCoolantRecipe;
import planner.menu.component.MenuComponentEditorListBlock;
import planner.menu.component.MenuComponentEditorTool;
import planner.menu.component.MenuComponentSFRIrradiatorRecipe;
import planner.menu.component.MenuComponentMSRIrradiatorRecipe;
import planner.menu.component.MenuComponentMinimaList;
import planner.menu.component.MenuComponentMinimalistButton;
import planner.menu.component.MenuComponentMinimalistScrollable;
import planner.menu.component.MenuComponentMinimalistTextView;
import planner.menu.component.MenuComponentMulticolumnMinimaList;
import planner.menu.component.MenuComponentOverMSRFuel;
import planner.menu.component.MenuComponentOverSFRFuel;
import planner.menu.component.MenuComponentUnderFuel;
import planner.tool.EditorTool;
import multiblock.Block;
import multiblock.Multiblock;
import multiblock.action.SetCoolantRecipeAction;
import multiblock.action.SetFuelAction;
import multiblock.action.SetblockAction;
import multiblock.action.SetblocksAction;
import multiblock.overhaul.fissionsfr.OverhaulSFR;
import multiblock.overhaul.fissionmsr.OverhaulMSR;
import multiblock.underhaul.fissionsfr.UnderhaulSFR;
import planner.tool.LineTool;
import planner.tool.PencilTool;
import planner.tool.RectangleTool;
import planner.tool.SelectionTool;
import simplelibrary.game.Framebuffer;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuEdit extends Menu{
    private final ArrayList<EditorTool> editorTools = new ArrayList<>();
    {
        editorTools.add(new SelectionTool(this));
        editorTools.add(new PencilTool(this));
        editorTools.add(new LineTool(this));
        editorTools.add(new RectangleTool(this));
    }
    public final Multiblock multiblock;
    private final int partSize = 48;
    private final int partsWide = 7;
    private final MenuComponentMinimalistButton back = add(new MenuComponentMinimalistButton(0, 0, 0, 0, "Back", true, true));
    private final MenuComponentMulticolumnMinimaList parts = add(new MenuComponentMulticolumnMinimaList(0, 0, 0, 0, partSize, partSize, partSize/2));
    private final MenuComponentMinimalistScrollable multibwauk = add(new MenuComponentMinimalistScrollable(0, 0, 0, 0, 32, 32));
    private final MenuComponentMinimalistButton zoomOut = add(new MenuComponentMinimalistButton(0, 0, 0, 0, "Zoom out", true, true));
    private final MenuComponentMinimalistButton zoomIn = add(new MenuComponentMinimalistButton(0, 0, 0, 0, "Zoom in", true, true));
    private final MenuComponentMinimalistButton resize = add(new MenuComponentMinimalistButton(0, 0, 0, 0, "Resize", true, true));
    public final MenuComponentMinimaList underFuelOrCoolantRecipe = new MenuComponentMinimaList(0, 0, 0, 0, 24);
    private final MenuComponentMinimaList overFuel = new MenuComponentMinimaList(0, 0, 0, 0, 24);
    private final MenuComponentMinimaList irradiatorRecipe = new MenuComponentMinimaList(0, 0, 0, 0, 24);
    private final MenuComponentMinimalistTextView textBox = add(new MenuComponentMinimalistTextView(0, 0, 0, 0, 24, 24));
    private final MenuComponentMinimalistButton editMetadata = add(new MenuComponentMinimalistButton(0, 0, 0, 0, "", true, true));
    private final MenuComponentMinimaList tools = add(new MenuComponentMinimaList(0, 0, 0, 0, partSize/2));
    private final MenuComponentMinimalistButton generate = add(new MenuComponentMinimalistButton(0, 0, 0, 0, "Generate", false, true));
    public final ArrayList<int[]> selection = new ArrayList<>();
    private double scale = 4;
    private double minScale = 0.5;
    private double maxScale = 16;
    private int CELL_SIZE = (int) (16*scale);
    private int LAYER_GAP = CELL_SIZE/2;
    private int multisPerRow = 0;
    public static int sourceCircle = -1;
    public static int outlineSquare = -1;
    public static boolean delCircle = false;
    public MenuEdit(GUI gui, Menu parent, Multiblock multiblock){
        super(gui, parent);
        if(multiblock instanceof UnderhaulSFR){
            add(underFuelOrCoolantRecipe);
            for(planner.configuration.underhaul.fissionsfr.Fuel fuel : Core.configuration.underhaul.fissionSFR.fuels){
                underFuelOrCoolantRecipe.add(new MenuComponentUnderFuel(fuel));
            }
        }
        if(multiblock instanceof OverhaulSFR){
            add(underFuelOrCoolantRecipe);
            for(planner.configuration.overhaul.fissionsfr.CoolantRecipe recipe : Core.configuration.overhaul.fissionSFR.coolantRecipes){
                underFuelOrCoolantRecipe.add(new MenuComponentCoolantRecipe(recipe));
            }
            add(overFuel);
            for(planner.configuration.overhaul.fissionsfr.Fuel fuel : Core.configuration.overhaul.fissionSFR.fuels){
                overFuel.add(new MenuComponentOverSFRFuel(fuel));
            }
            overFuel.setSelectedIndex(0);
            add(irradiatorRecipe);
            for(planner.configuration.overhaul.fissionsfr.IrradiatorRecipe recipe : Core.configuration.overhaul.fissionSFR.irradiatorRecipes){
                irradiatorRecipe.add(new MenuComponentSFRIrradiatorRecipe(recipe));
            }
            irradiatorRecipe.setSelectedIndex(0);
        }
        if(multiblock instanceof OverhaulMSR){
            add(overFuel);
            for(planner.configuration.overhaul.fissionmsr.Fuel fuel : Core.configuration.overhaul.fissionMSR.fuels){
                overFuel.add(new MenuComponentOverMSRFuel(fuel));
            }
            overFuel.setSelectedIndex(0);
            add(irradiatorRecipe);
            for(planner.configuration.overhaul.fissionmsr.IrradiatorRecipe recipe : Core.configuration.overhaul.fissionMSR.irradiatorRecipes){
                irradiatorRecipe.add(new MenuComponentMSRIrradiatorRecipe(recipe));
            }
            irradiatorRecipe.setSelectedIndex(0);
        }
        this.multiblock = multiblock;
        multibwauk.setScrollMagnitude(CELL_SIZE/2);
        back.addActionListener((e) -> {
            gui.open(new MenuTransition(gui, this, parent, MenuTransition.SlideTransition.slideTo(1, 0), 5));
        });
        resize.addActionListener((e) -> {
            gui.open(new MenuResize(gui, this, multiblock));
        });
        zoomOut.addActionListener((e) -> {
            scale = Math.max(minScale, Math.min(maxScale, scale/1.5));
            CELL_SIZE = (int) (16*scale);
            LAYER_GAP = CELL_SIZE/2;
            onGUIOpened();
        });
        zoomIn.addActionListener((e) -> {
            scale = Math.max(minScale, Math.min(maxScale, scale*1.5));
            CELL_SIZE = (int) (16*scale);
            LAYER_GAP = CELL_SIZE/2;
            onGUIOpened();
        });
        editMetadata.addActionListener((e) -> {
            gui.open(new MenuTransition(gui, this, new MenuMultiblockMetadata(gui, this, multiblock), MenuTransition.SlideTransition.slideTo(0, 1), 4));
        });
        generate.addActionListener((e) -> {
//            generate();
        });
        for(Block availableBlock : ((Multiblock<Block>)multiblock).getAvailableBlocks()){
            parts.add(new MenuComponentEditorListBlock(this, availableBlock));
        }
        parts.setSelectedIndex(0);
        for(EditorTool tool : editorTools){
            tools.add(new MenuComponentEditorTool(tool));
        }
        tools.setSelectedIndex(1);
    }
    @Override
    public void onGUIOpened(){
        delCircle = true;
        editMetadata.label = multiblock.getName();
        generate.label = multiblock.isEmpty()?"Generate":"Generate Suggestions";
        if(multiblock instanceof UnderhaulSFR){
            underFuelOrCoolantRecipe.setSelectedIndex(Core.configuration.underhaul.fissionSFR.fuels.indexOf(((UnderhaulSFR)multiblock).fuel));
        }
        if(multiblock instanceof OverhaulSFR){
            underFuelOrCoolantRecipe.setSelectedIndex(Core.configuration.overhaul.fissionSFR.coolantRecipes.indexOf(((OverhaulSFR)multiblock).coolantRecipe));
        }
        multisPerRow = Math.max(1, (int)((multibwauk.width-multibwauk.horizScrollbarHeight)/(CELL_SIZE*multiblock.getX()+LAYER_GAP)));
        multibwauk.components.clear();
        for(int y = 0; y<multiblock.getY(); y++){
            int column = y%multisPerRow;
            int row = y/multisPerRow;
            int layerWidth = multiblock.getX()*CELL_SIZE+LAYER_GAP;
            int layerHeight = multiblock.getZ()*CELL_SIZE+LAYER_GAP;
            multibwauk.add(new planner.menu.component.MenuComponentEditorGrid(column*layerWidth+LAYER_GAP/2, row*layerHeight+LAYER_GAP/2, CELL_SIZE, this, multiblock, y));
        }
        multiblock.recalculate();
    }
    @Override
    public void render(int millisSinceLastTick){
        if(delCircle&&sourceCircle!=-1){
            ImageStash.instance.deleteTexture(sourceCircle);
            sourceCircle = -1;
        }
        if(sourceCircle==-1){
            BufferedImage image = makeImage(CELL_SIZE, (buff) -> {
                Core.drawCircle(buff.width/2, buff.height/2, buff.width*(4/16d), buff.width*(6/16d), Color.white);
            }, (img) -> {
                for(int x = 0; x<img.getWidth(); x++){
                    for(int y = 0; y<img.getHeight(); y++){
                        double xOff = x-img.getWidth()/2;
                        double yOff = y-img.getHeight()/2;
                        double dist = Math.sqrt(xOff*xOff+yOff*yOff);
                        if(dist>4/16d*img.getWidth()&&dist<6/16d*img.getWidth()){
                            img.setRGB(x, y, new Color(1f, 1f, 1f).getRGB());
                        }else{
                            img.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
                        }
                    }
                }
            });
            sourceCircle = ImageStash.instance.allocateAndSetupTexture(image);
        }
        if(outlineSquare==-1){
            BufferedImage image = makeImage(32, (buff) -> {
                Core.applyWhite();
                double inset = buff.width/32d;
                drawRect(inset, inset, buff.width-inset, inset+buff.width/16, 0);
                drawRect(inset, buff.width-inset-buff.width/16, buff.width-inset, buff.width-inset, 0);
                drawRect(inset, inset+buff.width/16, inset+buff.width/16, buff.width-inset-buff.width/16, 0);
                drawRect(buff.width-inset-buff.width/16, inset+buff.width/16, buff.width-inset, buff.width-inset-buff.width/16, 0);
            }, (img) -> {
                int size = img.getWidth();
                double inset = size/32;
                for(int x = 0; x<img.getWidth(); x++){
                    for(int y = 0; y<img.getHeight(); y++){
                        double X = x/(double)img.getWidth();
                        double Y = y/(double)img.getHeight();
                        boolean isWhite = true;
                        if(X<1/32d||Y<1/32d||X>31/32d||Y>31/32d)isWhite = false;
                        if(X>3/32d&&Y>3/32d&&X<29/32d&&Y<29/32d)isWhite = false;
                        if(isWhite){
                            img.setRGB(x, y, new Color(1f, 1f, 1f).getRGB());
                        }else{
                            img.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
                        }
                    }
                }
            });
            outlineSquare = ImageStash.instance.allocateAndSetupTexture(image);
        }
        setTooltip("");
        if(multisPerRow!=Math.max(1, (int)((multibwauk.width-multibwauk.horizScrollbarHeight)/(CELL_SIZE*multiblock.getX()+LAYER_GAP)))){
            onGUIOpened();
        }
        parts.width = partsWide*partSize+parts.vertScrollbarWidth*(parts.hasVertScrollbar()?1:0);
        tools.width = partSize;
        parts.x = tools.width+partSize/4;
        generate.x = editMetadata.x = textBox.width = multibwauk.x = back.width = parts.x+parts.width;
        generate.height = tools.y = multibwauk.y = parts.y = editMetadata.height = back.height = 48;
        generate.y = Display.getHeight()-generate.height;
        tools.height = parts.height = (parts.components.size()+5)/partsWide*partSize;
        resize.width = 320;
        generate.width = editMetadata.width = multibwauk.width = Display.getWidth()-parts.x-parts.width-resize.width;
        zoomIn.height = zoomOut.height = resize.height = back.height;
        zoomIn.width = zoomOut.width = resize.width/2;
        zoomIn.y = zoomOut.y = resize.height;
        resize.x = Display.getWidth()-resize.width;
        zoomIn.x = resize.x;
        zoomOut.x = zoomIn.x+zoomIn.width;
        irradiatorRecipe.x = overFuel.x = underFuelOrCoolantRecipe.x = resize.x;
        underFuelOrCoolantRecipe.y = resize.height*2;
        irradiatorRecipe.width = overFuel.width = underFuelOrCoolantRecipe.width = resize.width;
        underFuelOrCoolantRecipe.height = Display.getHeight()-resize.height*2;
        for(MenuComponent c : tools.components){
            c.width = c.height = partSize;
        }
        if(multiblock instanceof OverhaulSFR){
            underFuelOrCoolantRecipe.height = 96;
            irradiatorRecipe.height = 96;
            irradiatorRecipe.y = Display.getHeight()-irradiatorRecipe.height;
            overFuel.y = underFuelOrCoolantRecipe.y+underFuelOrCoolantRecipe.height;
            overFuel.height = irradiatorRecipe.y-overFuel.y;
        }
        if(multiblock instanceof OverhaulMSR){
            underFuelOrCoolantRecipe.height = 0;
            irradiatorRecipe.height = 96;
            irradiatorRecipe.y = Display.getHeight()-irradiatorRecipe.height;
            overFuel.y = underFuelOrCoolantRecipe.y+underFuelOrCoolantRecipe.height;
            overFuel.height = irradiatorRecipe.y-overFuel.y;
        }
        for(MenuComponent c : underFuelOrCoolantRecipe.components){
            c.width = underFuelOrCoolantRecipe.width-underFuelOrCoolantRecipe.vertScrollbarWidth;
            c.height = 32;
        }
        for(MenuComponent c : irradiatorRecipe.components){
            c.width = irradiatorRecipe.width-irradiatorRecipe.vertScrollbarWidth;
            c.height = 32;
        }
        for(MenuComponent c : overFuel.components){
            c.width = overFuel.width-overFuel.vertScrollbarWidth;
            c.height = 32;
        }
        if(multiblock instanceof UnderhaulSFR){
            if(underFuelOrCoolantRecipe.getSelectedIndex()>-1){
                planner.configuration.underhaul.fissionsfr.Fuel fuel = Core.configuration.underhaul.fissionSFR.fuels.get(underFuelOrCoolantRecipe.getSelectedIndex());
                if(((UnderhaulSFR)multiblock).fuel!=fuel){
                    multiblock.action(new SetFuelAction(this, fuel));
                }
            }
        }
        if(multiblock instanceof OverhaulSFR){
            if(underFuelOrCoolantRecipe.getSelectedIndex()>-1){
                planner.configuration.overhaul.fissionsfr.CoolantRecipe recipe = Core.configuration.overhaul.fissionSFR.coolantRecipes.get(underFuelOrCoolantRecipe.getSelectedIndex());
                if(((OverhaulSFR)multiblock).coolantRecipe!=recipe){
                    multiblock.action(new SetCoolantRecipeAction(this, recipe));
                }
            }
        }
        multibwauk.height = Display.getHeight()-multibwauk.y-generate.height;
        textBox.y = parts.y+parts.height;
        textBox.height = Display.getHeight()-textBox.y;
        super.render(millisSinceLastTick);
    }
    public void setTooltip(String tooltip){
        if(!tooltip.isEmpty())tooltip+="\n\n";
        textBox.setText(tooltip+multiblock.getTooltip());
    }
    public Block getSelectedBlock(){
        if(parts.getSelectedIndex()==-1)return null;
        return ((MenuComponentEditorListBlock) parts.components.get(parts.getSelectedIndex())).block;
    }
    public EditorTool getSelectedTool(){
        if(tools.getSelectedIndex()==-1)return null;
        return ((MenuComponentEditorTool) tools.components.get(tools.getSelectedIndex())).tool;
    }
    public planner.configuration.overhaul.fissionsfr.Fuel getSelectedOverSFRFuel(){
        return ((MenuComponentOverSFRFuel) overFuel.components.get(overFuel.getSelectedIndex())).fuel;
    }
    public planner.configuration.overhaul.fissionsfr.IrradiatorRecipe getSelectedSFRIrradiatorRecipe(){
        return ((MenuComponentSFRIrradiatorRecipe) irradiatorRecipe.components.get(irradiatorRecipe.getSelectedIndex())).recipe;
    }
    public planner.configuration.overhaul.fissionmsr.Fuel getSelectedOverMSRFuel(){
        return ((MenuComponentOverMSRFuel) overFuel.components.get(overFuel.getSelectedIndex())).fuel;
    }
    public planner.configuration.overhaul.fissionmsr.IrradiatorRecipe getSelectedMSRIrradiatorRecipe(){
        return ((MenuComponentMSRIrradiatorRecipe) irradiatorRecipe.components.get(irradiatorRecipe.getSelectedIndex())).recipe;
    }
    public void setblock(int x, int y, int z, Block template){
        if(hasSelection()&&!isSelected(x, y, z))return;
        if(template==null){
            if(Core.isControlPressed()){
                if(multiblock.getBlock(x, y, z)!=null&&!multiblock.getBlock(x, y, z).matches(getSelectedBlock()))return;
            }
            multiblock.action(new SetblockAction(x,y,z,null));
            return;
        }
        if(Core.isControlPressed()){
            if(multiblock.getBlock(x, y, z)!=null&&!Core.isShiftPressed())return;
            if(multiblock.getBlock(x, y, z)==null||multiblock.getBlock(x, y, z)!=null&&Core.isShiftPressed()){
                if(!isValid(template, x, y, z))return;
            }
        }
        Block blok = template.newInstance(x, y, z);
        if(multiblock instanceof OverhaulSFR){
            if(((multiblock.overhaul.fissionsfr.Block)blok).isFuelCell()){
                ((multiblock.overhaul.fissionsfr.Block)blok).fuel = getSelectedOverSFRFuel();
            }
            if(((multiblock.overhaul.fissionsfr.Block)blok).isIrradiator()){
                ((multiblock.overhaul.fissionsfr.Block)blok).recipe = getSelectedSFRIrradiatorRecipe();
            }
        }
        if(multiblock instanceof OverhaulMSR){
            if(((multiblock.overhaul.fissionmsr.Block)blok).isFuelVessel()){
                ((multiblock.overhaul.fissionmsr.Block)blok).fuel = getSelectedOverMSRFuel();
            }
            if(((multiblock.overhaul.fissionmsr.Block)blok).isIrradiator()){
                ((multiblock.overhaul.fissionmsr.Block)blok).recipe = getSelectedMSRIrradiatorRecipe();
            }
        }
        multiblock.action(new SetblockAction(x,y,z,blok));
    }
    @Override
    public void keyboardEvent(char character, int key, boolean pressed, boolean repeat){
        super.keyboardEvent(character, key, pressed, repeat);
        if(pressed&&key==Keyboard.KEY_ESCAPE)selection.clear();
        if(pressed&&Core.isControlPressed()){
            if(key==Keyboard.KEY_Z){
                multiblock.undo();
            }
            if(key==Keyboard.KEY_Y){
                multiblock.redo();
            }
        }
    }
    public void setblocks(SetblocksAction set){
        for(Iterator<int[]> it = set.locations.iterator(); it.hasNext();){
            int[] b = it.next();
            if(hasSelection()&&!isSelected(b[0], b[1], b[2]))it.remove();
            else if(Core.isControlPressed()){
                if(set.block==null){
                    if(multiblock.getBlock(b[0], b[1], b[2])!=null&&!multiblock.getBlock(b[0], b[1], b[2]).matches(getSelectedBlock()))it.remove();
                }else{
                    if(multiblock.getBlock(b[0], b[1], b[2])!=null&&!Core.isShiftPressed()){
                        it.remove();
                    }else if(multiblock.getBlock(b[0], b[1], b[2])==null||multiblock.getBlock(b[0], b[1], b[2])!=null&&Core.isShiftPressed()){
                        if(!isValid(set.block, b[0], b[1], b[2]))it.remove();
                    }
                }
            }
        }
        if(set.block!=null&&multiblock instanceof OverhaulSFR){
            if(((multiblock.overhaul.fissionsfr.Block)set.block).isFuelCell()){
                ((multiblock.overhaul.fissionsfr.Block)set.block).fuel = getSelectedOverSFRFuel();
            }
            if(((multiblock.overhaul.fissionsfr.Block)set.block).isIrradiator()){
                ((multiblock.overhaul.fissionsfr.Block)set.block).recipe = getSelectedSFRIrradiatorRecipe();
            }
        }
        if(set.block!=null&&multiblock instanceof OverhaulMSR){
            if(((multiblock.overhaul.fissionmsr.Block)set.block).isFuelVessel()){
                ((multiblock.overhaul.fissionmsr.Block)set.block).fuel = getSelectedOverMSRFuel();
            }
            if(((multiblock.overhaul.fissionmsr.Block)set.block).isIrradiator()){
                ((multiblock.overhaul.fissionmsr.Block)set.block).recipe = getSelectedMSRIrradiatorRecipe();
            }
        }
        multiblock.action(set);
    }
    public boolean isValid(Block selectedBlock, int x, int layer, int z){
        Block b = selectedBlock.newInstance(x, layer, z);
        return b.hasRules()&&b.calculateRules(multiblock);
    }
    public void select(int x1, int y1, int z1, int x2, int y2, int z2){
        if(!Core.isControlPressed()){
            selection.clear();
            if(x1==x2&&y1==y2&&z1==z2)return;
        }
        ArrayList<int[]> is = new ArrayList<>();
        for(int x = Math.min(x1,x2); x<=Math.max(x1,x2); x++){
            for(int y = Math.min(y1,y2); y<=Math.max(y1,y2); y++){
                for(int z = Math.min(z1,z2); z<=Math.max(z1,z2); z++){
                    is.add(new int[]{x,y,z});
                    if(isSelected(x, y, z))continue;
                    selection.add(new int[]{x,y,z});
                }
            }
        }
        select(is);
    }
    public void deselect(int x1, int y1, int z1, int x2, int y2, int z2){
        ArrayList<int[]> is = new ArrayList<>();
        for(int x = Math.min(x1,x2); x<=Math.max(x1,x2); x++){
            for(int y = Math.min(y1,y2); y<=Math.max(y1,y2); y++){
                for(int z = Math.min(z1,z2); z<=Math.max(z1,z2); z++){
                    is.add(new int[]{x,y,z});
                }
            }
        }
        deselect(is);
    }
    public void select(ArrayList<int[]> is){
        if(!Core.isControlPressed()){
            selection.clear();
        }
        for(int[] i : is){
            if(isSelected(i[0], i[1], i[2]))continue;
            selection.add(i);
        }
    }
    public void deselect(ArrayList<int[]> is){
        if(!Core.isControlPressed()){
            selection.clear();
            return;
        }
        for(int[] i : is){
            for(Iterator<int[]> it = selection.iterator(); it.hasNext();){
                int[] s = it.next();
                if(s[0]==i[0]&&s[1]==i[1]&&s[2]==i[2])it.remove();
            }
        }
    }
    public boolean isSelected(int x, int y, int z){
        for(int[] s : selection){
            if(s[0]==x&&s[1]==y&&s[2]==z)return true;
        }
        return false;
    }
    private boolean hasSelection(){
        return !selection.isEmpty();
    }
    public void selectCluster(int x, int y, int z){
        if(multiblock instanceof OverhaulSFR){
            OverhaulSFR osfr = (OverhaulSFR) multiblock;
            OverhaulSFR.Cluster c = osfr.getCluster(osfr.getBlock(x, y, z));
            ArrayList<int[]> is = new ArrayList<>();
            for(Block b : c.blocks){
                is.add(new int[]{b.x,b.y,b.z});
            }
            select(is);
        }
        if(multiblock instanceof OverhaulMSR){
            OverhaulMSR omsr = (OverhaulMSR) multiblock;
            OverhaulMSR.Cluster c = omsr.getCluster(omsr.getBlock(x, y, z));
            ArrayList<int[]> is = new ArrayList<>();
            for(Block b : c.blocks){
                is.add(new int[]{b.x,b.y,b.z});
            }
            select(is);
        }
    }
    public void deselectCluster(int x, int y, int z){
        if(multiblock instanceof OverhaulSFR){
            OverhaulSFR osfr = (OverhaulSFR) multiblock;
            OverhaulSFR.Cluster c = osfr.getCluster(osfr.getBlock(x, y, z));
            ArrayList<int[]> is = new ArrayList<>();
            for(Block b : c.blocks){
                is.add(new int[]{b.x,b.y,b.z});
            }
            deselect(is);
        }
        if(multiblock instanceof OverhaulMSR){
            OverhaulMSR omsr = (OverhaulMSR) multiblock;
            OverhaulMSR.Cluster c = omsr.getCluster(omsr.getBlock(x, y, z));
            ArrayList<int[]> is = new ArrayList<>();
            for(Block b : c.blocks){
                is.add(new int[]{b.x,b.y,b.z});
            }
            deselect(is);
        }
    }
    public void selectGroup(int x, int y, int z){
        ArrayList<Block> g = multiblock.getGroup(multiblock.getBlock(x, y, z));
        if(g==null){
            select(0, 0, 0, multiblock.getX()-1, multiblock.getY()-1, multiblock.getZ()-1);
            return;
        }
        ArrayList<int[]> is = new ArrayList<>();
        for(Block b : g){
            is.add(new int[]{b.x,b.y,b.z});
        }
        select(is);
    }
    public void deselectGroup(int x, int y, int z){
        ArrayList<Block> g = multiblock.getGroup(multiblock.getBlock(x, y, z));
        if(g==null){
            deselect(0, 0, 0, multiblock.getX()-1, multiblock.getY()-1, multiblock.getZ()-1);
            return;
        }
        ArrayList<int[]> is = new ArrayList<>();
        for(Block b : g){
            is.add(new int[]{b.x,b.y,b.z});
        }
        deselect(is);
    }
    private final int MAX_SIZE = (int) (maxScale*16);
    private ByteBuffer bufferer = ImageStash.createDirectByteBuffer(MAX_SIZE*MAX_SIZE*4);
    private final boolean basejava = true;
    private BufferedImage makeImage(BufferRenderer r, ImageRenderer r2){
        return makeImage(MAX_SIZE, r, r2);
    }
    private BufferedImage makeImage(int size, BufferRenderer r, ImageRenderer r2){
        if(basejava){
            BufferedImage img2 = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
            r2.render(img2);
            return img2;
        }
        Framebuffer buff = new Framebuffer(Core.helper, "Randobuffer", size, size);
        buff.bindRenderTarget2D();
        r.render(buff);
        buff.releaseRenderTarget();
        buff.bindTexture();
        bufferer.clear();
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bufferer);
//        GL11.glReadPixels(0, 0, MAX_SIZE, MAX_SIZE, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bufferer);
//        buff.releaseRenderTarget();
        ImageStash.instance.deleteBuffer(ImageStash.instance.getBuffer(buff.name));
        int[] imgRGBData = new int[size*size];
        byte[] imgData = new byte[size*size*4];
        bufferer.position(0);
        bufferer.get(imgData);
        for(int i=0;i<imgRGBData.length;i++){
            imgRGBData[i]=((0|imgData[i*4])<<16)+((0|imgData[i*4+1])<<8)+((0|imgData[i*4+2]))+((0|imgData[i*4+2])<<24);//Use RED, GREEN, or BLUE channel (here BLUE) for alpha data
        }
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
        img.setRGB(0, 0, size, size, imgRGBData, 0, size);
        return img;
    }
    private static interface BufferRenderer{
        void render(Framebuffer buff);
    }
    private static interface ImageRenderer{
        void render(BufferedImage buff);
    }
}