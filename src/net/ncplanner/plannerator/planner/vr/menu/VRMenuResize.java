package net.ncplanner.plannerator.planner.vr.menu;
import java.util.function.Consumer;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.multiblock.CuboidalMultiblock;
import net.ncplanner.plannerator.planner.Core;
import net.ncplanner.plannerator.planner.FormattedText;
import net.ncplanner.plannerator.planner.vr.VRGUI;
import net.ncplanner.plannerator.planner.vr.VRMenu;
import net.ncplanner.plannerator.planner.vr.menu.component.VRMenuComponentButton;
import net.ncplanner.plannerator.planner.vr.menu.component.VRMenuComponentTextPanel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.openvr.TrackedDevicePose;
import simplelibrary.image.Color;
public class VRMenuResize extends VRMenu{//TODO center the multiblock
    public VRMenuComponentButton done = add(new VRMenuComponentButton(-.25, 1.75, -1, .5, .125, .1, 0, 0, 0, "Done", true, false));
    private VRMenuComponentTextPanel textPanel = add(new VRMenuComponentTextPanel(.25, 1, 1.2, .5, .25, .05, 0, 180, 0, "Size"));
    private final CuboidalMultiblock multiblock;
    private boolean refreshNeeded;
    public VRMenuResize(VRGUI gui, VRMenu parent, CuboidalMultiblock multiblock){
        super(gui, parent);
        done.setTooltip("Finish resizing and return to the editor screen");
        done.addActionListener(() -> {
            gui.open(new VRMenuEdit(gui, multiblock).alreadyOpen());
        });
        this.multiblock = multiblock;
    }
    @Override
    public void onGUIOpened(){
        refreshNeeded = true;
    }
    @Override
    public void tick(){
        if(refreshNeeded){
            components.clear();
            add(done);
            add(textPanel);
            double blockSize = 1d/Math.max(multiblock.getInternalWidth(), Math.max(multiblock.getInternalHeight(), multiblock.getInternalDepth()));
            for(int y = 0; y<multiblock.getInternalHeight(); y++){
                final int Y = y;
                add(new VRMenuComponentPlusButton(multiblock.getInternalWidth(), Y+.5, multiblock.getInternalDepth(), blockSize, multiblock.getInternalHeight()<multiblock.getMaxY(), () -> {
                    insertY(Y);
                }, (renderer) -> {
                    renderer.drawCube(-.01, Y+.49, -.01, multiblock.getInternalWidth()+.01, Y+1.51, multiblock.getInternalDepth()+.01, 0);
                }));
                add(new VRMenuComponentMinusButton(-1, Y, -1, blockSize, multiblock.getInternalHeight()>multiblock.getMinY(), true, () -> {
                    deleteY(Y);
                }, (renderer) -> {
                    renderer.drawCube(-.01, Y-.01, -.01, multiblock.getInternalWidth()+.01, Y+1.01, multiblock.getInternalDepth()+.01, 0);
                }));
            }
            add(new VRMenuComponentPlusButton(multiblock.getInternalWidth(), -.5, multiblock.getInternalDepth(), blockSize, multiblock.getInternalHeight()<multiblock.getMaxY(), () -> {
                expand(0, 1, 0);
            }, (renderer) -> {
                renderer.drawCube(-.01, -.51, -.01, multiblock.getInternalWidth()+.01, .51, multiblock.getInternalDepth()+.01, 0);
            }));
            for(int x = 0; x<multiblock.getInternalWidth(); x++){
                final int X = x;
                add(new VRMenuComponentMinusButton(x, multiblock.getInternalHeight()/2d, -1, blockSize, true, true, () -> {
                    deleteX(X);
                }, (renderer) -> {
                    renderer.drawCube(X-.01, -.01, -.01, X+1.01, multiblock.getInternalHeight()+.01, multiblock.getInternalDepth()+.01, 0);
                }));
            }
            for(int z = 0; z<multiblock.getInternalDepth(); z++){
                final int Z = z;
                add(new VRMenuComponentMinusButton(-1, multiblock.getInternalHeight()/2d, z, blockSize, true, false, () -> {
                    deleteZ(Z);
                }, (renderer) -> {
                    renderer.drawCube(-.01, -.01, Z-.01, multiblock.getInternalWidth()+.01, multiblock.getInternalHeight()+.01, Z+1.01, 0);
                }));
            }
            add(new VRMenuComponentPlusButton(-2, multiblock.getInternalHeight()/2d-.5, multiblock.getInternalDepth()/2d-.5, blockSize, true, () -> {
                expand(-1, 0, 0);
            }, (renderer) -> {
                renderer.drawCube(-1.01, -.01, -.01, .01, multiblock.getInternalHeight()+.01, multiblock.getInternalDepth()+.01, 0);
            }));
            add(new VRMenuComponentPlusButton(multiblock.getInternalWidth()/2d-.5, multiblock.getInternalHeight()/2d-.5, -2, blockSize, true, () -> {
                expand(0, 0, -1);
            }, (renderer) -> {
                renderer.drawCube(-.01, -.01, -1.01, multiblock.getInternalWidth()+.01, multiblock.getInternalHeight()+.01, .01, 0);
            }));
            add(new VRMenuComponentPlusButton(multiblock.getInternalWidth()+1, multiblock.getInternalHeight()/2d-.5, multiblock.getInternalDepth()/2d-.5, blockSize, true, () -> {
                expand(1, 0, 0);
            }, (renderer) -> {
                renderer.drawCube(multiblock.getInternalWidth()-.01, -.01, -.01, multiblock.getInternalWidth()+1.01, multiblock.getInternalHeight()+.01, multiblock.getInternalDepth()+.01, 0);
            }));
            add(new VRMenuComponentPlusButton(multiblock.getInternalWidth()/2d-.5, multiblock.getInternalHeight()/2d-.5, multiblock.getInternalDepth()+1, blockSize, true, () -> {
                expand(0, 0, 1);
            }, (renderer) -> {
                renderer.drawCube(-.01, -.01, multiblock.getInternalDepth()-.01, multiblock.getInternalWidth()+.01, multiblock.getInternalHeight()+.01, multiblock.getInternalDepth()+1.01, 0);
            }));
            refreshNeeded = false;
        }
        super.tick();
    }
    @Override
    public void render(Renderer renderer, TrackedDevicePose.Buffer tdpb){
        GL11.glPushMatrix();
        GL11.glTranslated(-.5, .5, -.5);
        double size = Math.max(multiblock.getInternalWidth(), Math.max(multiblock.getInternalHeight(), multiblock.getInternalDepth()));
        GL11.glScaled(1/size, 1/size, 1/size);
        multiblock.draw3D();
        renderer.setColor(Core.theme.get3DMultiblockOutlineColor());
        renderer.drawCubeOutline(-1/16f, -1/16f, -1/16f, multiblock.getInternalWidth()+1/16f, multiblock.getInternalHeight()+1/16f, multiblock.getInternalDepth()+1/16f, 1/16f);
        GL11.glPopMatrix();
        textPanel.text = new FormattedText(multiblock.getDimensionsStr());
        super.render(renderer, tdpb);
    }
    public void expand(int x, int y, int z){
        if(x>0)multiblock.expandRight(x);
        if(x<0)multiblock.expandLeft(-x);
        if(y>0)multiblock.expandUp(y);
        if(y<0)multiblock.exandDown(-y);
        if(z>0)multiblock.expandToward(z);
        if(z<0)multiblock.expandAway(-z);
        onGUIOpened();
    }
    private void deleteX(int x){
        multiblock.deleteX(x);
        onGUIOpened();
    }
    private void deleteY(int y){
        multiblock.deleteY(y);
        onGUIOpened();
    }
    private void deleteZ(int z){
        multiblock.deleteZ(z);
        onGUIOpened();
    }
    private void insertX(int x){
        multiblock.insertX(x);
        onGUIOpened();
    }
    private void insertY(int y){
        multiblock.insertY(y);
        onGUIOpened();
    }
    private void insertZ(int z){
        multiblock.insertZ(z);
        onGUIOpened();
    }
    private static class VRMenuComponentPlusButton extends VRMenuComponentButton{
        private final Consumer<Renderer> highlight;
        private final double X;
        private final double Y;
        private final double Z;
        public VRMenuComponentPlusButton(double x, double y, double z, double size, boolean enabled, Runnable al, Consumer<Renderer> highlight){
            super(-.5+x*size, .5+y*size, -.5+z*size, size, size, size, 0, 0, 0, "", enabled, false);
            this.X = x;
            this.Y = y;
            this.Z = z;
            addActionListener(al);
            this.highlight = highlight;
        }
        @Override
        public void renderComponent(Renderer renderer, TrackedDevicePose.Buffer tdpb){
            Color col = Core.theme.getComponentColor(Core.getThemeIndex(this));
            if(enabled){
                if(isPressed)col = Core.theme.getComponentPressedColor(Core.getThemeIndex(this));
                else if(!isDeviceOver.isEmpty())col = Core.theme.getComponentMouseoverColor(Core.getThemeIndex(this));
            }else{
                col = Core.theme.getComponentDisabledColor(Core.getThemeIndex(this));
            }
            renderer.setColor(col);
            renderer.drawCubeOutline(0, 0, 0, width, height, depth, .005);//5mcm
            GL11.glPushMatrix();
            GL11.glTranslated(width/2, height/2, depth/2);//center
            renderer.setColor(Core.theme.getAddButtonTextColor());
            renderer.drawCube(-width/4, -.005, -.005, width/4, .005, .005, 0);
            renderer.drawCube(-.005, -width/4, -.005, .005, width/4, .005, 0);
            renderer.drawCube(-.005, -.005, -width/4, .005, .005, width/4, 0);
            GL11.glPopMatrix();
        }
        @Override
        public void renderForeground(Renderer renderer){
            super.renderForeground(renderer);
            if(isDeviceOver.isEmpty())return;
            renderer.setColor(Core.theme.getAddButtonTextColor(), .5f);
            GL11.glPushMatrix();
            GL11.glScaled(width, height, depth);
            GL11.glTranslated(-X, -Y, -Z);
            highlight.accept(renderer);
            GL11.glPopMatrix();
        }
    }
    private static class VRMenuComponentMinusButton extends VRMenuComponentButton{
        private final Consumer<Renderer> highlight;
        private final double X;
        private final double Y;
        private final double Z;
        private final boolean isX;
        public VRMenuComponentMinusButton(double x, double y, double z, double size, boolean enabled, boolean isX, Runnable al, Consumer<Renderer> highlight){
            super(-.5+x*size, .5+y*size, -.5+z*size, size, size, size, 0, 0, 0, "", enabled, false);
            this.X = x;
            this.Y = y;
            this.Z = z;
            addActionListener(al);
            this.isX = isX;
            this.highlight = highlight;
        }
        @Override
        public void renderComponent(Renderer renderer, TrackedDevicePose.Buffer tdpb){
            Color col = Core.theme.getComponentColor(Core.getThemeIndex(this));
            if(enabled){
                if(isPressed)col = Core.theme.getComponentPressedColor(Core.getThemeIndex(this));
                else if(!isDeviceOver.isEmpty())col = Core.theme.getComponentMouseoverColor(Core.getThemeIndex(this));
            }else{
                col = Core.theme.getComponentDisabledColor(Core.getThemeIndex(this));
            }
            renderer.setColor(col);
            renderer.drawCubeOutline(0, 0, 0, width, height, depth, .005);//5mm
            GL11.glPushMatrix();
            GL11.glTranslated(width/2, height/2, depth/2);//center
            renderer.setColor(Core.theme.getDeleteButtonTextColor());
            if(isX)renderer.drawCube(-width/4, -.005, -.005, width/4, .005, .005, 0);
            else renderer.drawCube(-.005, -.005, -width/4, .005, .005, width/4, 0);
            GL11.glPopMatrix();
        }
        @Override
        public void renderForeground(Renderer renderer){
            super.renderForeground(renderer);
            if(isDeviceOver.isEmpty())return;
            renderer.setColor(Core.theme.getDeleteButtonTextColor(), .5f);
            GL11.glPushMatrix();
            GL11.glScaled(width, height, depth);
            GL11.glTranslated(-X, -Y, -Z);
            highlight.accept(renderer);
            GL11.glPopMatrix();
        }
    }
}