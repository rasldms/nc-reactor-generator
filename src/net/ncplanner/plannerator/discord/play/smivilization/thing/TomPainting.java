package net.ncplanner.plannerator.discord.play.smivilization.thing;
import java.util.UUID;
import net.ncplanner.plannerator.Renderer;
import net.ncplanner.plannerator.discord.play.smivilization.Hut;
import net.ncplanner.plannerator.discord.play.smivilization.HutThing;
import net.ncplanner.plannerator.discord.play.smivilization.Wall;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
public class TomPainting extends HutThing{
    public TomPainting(UUID uuid, Hut hut){
        super(uuid, hut, "tomdodd4598 Painting", "tompainting", 4598);
    }
    @Override
    public HutThing newInstance(UUID uuid, Hut hut){
        return new TomPainting(uuid, hut);
    }
    @Override
    public void render(Renderer renderer, float imgScale){
        GL11.glColor4d(1, 1, 1, 1);
        ImageStash.instance.bindTexture(ImageStash.instance.getTexture(getTexture()));
        GL11.glBegin(GL11.GL_QUADS);
        switch(wall){
            case LEFT:
                vert(0,0,x,y+getDimY(),z+getDimZ());
                vert(0,1,x,y+getDimY(),z);
                vert(1,1,x,y,z);
                vert(1,0,x,y,z+getDimZ());
                break;
            case BACK:
                vert(0, 0, x, y, z+getDimZ());
                vert(0, 1, x, y, z);
                vert(1, 1, x+getDimX(), y, z);
                vert(1, 0, x+getDimX(), y, z+getDimZ());
                break;
            case RIGHT:
                vert(0,0,x,y,z+getDimZ());
                vert(0,1,x,y,z);
                vert(1,1,x,y+getDimY(),z);
                vert(1,0,x,y+getDimY(),z+getDimZ());
                break;
            default:
                GL11.glEnd();
                throw new IllegalArgumentException("Cannot render on wall "+wall.toString()+"!");
        }
        GL11.glEnd();
    }
    private void vert(double texX, double texY, double x, double y, double z){
        GL11.glTexCoord2d(texX, texY);
        double[] pos = Hut.convertXYZtoXY512(x, y, z);
        GL11.glVertex2d(pos[0], pos[1]);
    }
    @Override
    public int[] getDimensions(){
        if(wall==Wall.BACK)return new int[]{6,1,6};
        else return new int[]{1,6,6};
    }
    @Override
    public int[] getDefaultLocation(){
        return new int[]{0,1,3};
    }
    @Override
    public Wall getDefaultWall(){
        return Wall.LEFT;
    }
    @Override
    public float getRenderWidth(){
        return 0;
    }
    @Override
    public float getRenderHeight(){
        return 0;
    }
    @Override
    public float getRenderOriginX(){
        return 0;
    }
    @Override
    public float getRenderOriginY(){
        return 0;
    }
    @Override
    public Wall[] getAllowedWalls(){
        return new Wall[]{Wall.LEFT,Wall.BACK,Wall.RIGHT};
    }
    @Override
    public boolean isBackgroundObject(){
        return true;
    }
}