package CS355.LWJGL;


//You might notice a lot of imports here.
//You are probably wondering why I didn't just import org.lwjgl.opengl.GL11.*
//Well, I did it as a hint to you.
//OpenGL has a lot of commands, and it can be kind of intimidating.
//This is a list of all the commands I used when I implemented my project.
//Therefore, if a command appears in this list, you probably need it.
//If it doesn't appear in this list, you probably don't.
//Of course, your milage may vary. Don't feel restricted by this list of imports.
import org.lwjgl.input.Keyboard;

import java.util.Iterator;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3d;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 *
 * @author Brennan Smith
 */
public class StudentLWJGLController implements CS355LWJGLController 
{
  //This is a model of a house.
  //It has a single method that returns an iterator full of Line3Ds.
  //A "Line3D" is a wrapper class around two Point2Ds.
  //It should all be fairly intuitive if you look at those classes.
  //If not, I apologize.
  private WireFrame model = new HouseModel();

    private float cameraX = 50F;
    private float cameraY = -20F;
    private float cameraZ = 0F;
    private float cameraA = 105.0F;


  //This method is called to "resize" the viewport to match the screen.
  //When you first start, have it be in perspective mode.
  @Override
  public void resizeGL() 
  {
      glViewport (0, 0, LWJGLSandbox.DISPLAY_WIDTH, LWJGLSandbox.DISPLAY_HEIGHT);
      glMatrixMode (GL_PROJECTION);
      glLoadIdentity();
      gluPerspective(50,LWJGLSandbox.DISPLAY_WIDTH/ LWJGLSandbox.DISPLAY_HEIGHT,10,200);

      glMatrixMode(GL_MODELVIEW);
      glLoadIdentity();
      glRotatef(cameraA,0.0F,1.0F,0.0F);
      glTranslatef(cameraX,cameraY,cameraZ);

  }

    @Override
    public void update() 
    {
        
    }

    //This is called every frame, and should be responsible for keyboard updates.
    //An example keyboard event is captured below.
    //The "Keyboard" static class should contain everything you need to finish
    // this up.
    @Override
    public void updateKeyboard() 
    {
        if(Keyboard.isKeyDown(Keyboard.KEY_W)) 
        {
            cameraX -= .5 * (float)Math.sin((double)degreesToRadians(cameraA));
            cameraZ += .5 * (float)Math.cos((double)degreesToRadians(cameraA));
            cameraMove();
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_S))
        {
            cameraX += .5 * (float)Math.sin((double)degreesToRadians(cameraA));
            cameraZ -= .5 * (float)Math.cos((double)degreesToRadians(cameraA));
            cameraMove();
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A))
        {
            cameraX -= .5 * (float)Math.sin((double)(degreesToRadians(cameraA - 90)));
            cameraZ += .5 * (float)Math.cos((double)(degreesToRadians(cameraA - 90)));
            cameraMove();
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D))
        {
            cameraX -= .5 * (float)Math.sin((double)(degreesToRadians(cameraA + 90)));
            cameraZ += .5 * (float)Math.cos((double)(degreesToRadians(cameraA + 90)));
            cameraMove();
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_Q))
        {
            cameraA -= .5;
            cameraMove();
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_E))
        {
            cameraA += .5;
            cameraMove();
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_R))
        {
            cameraY -= .5;
            cameraMove();
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_F))
        {
            cameraY += .5;
            cameraMove();
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_H))
        {
            cameraX = 50F;
            cameraY = -20F;
            cameraZ = 0F;
            cameraA = 105.0F;
            cameraMove();
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_O))
        {
            glMatrixMode (GL_PROJECTION);
            glLoadIdentity();
            glOrtho(-30.0, 30.0, -30.0, 30.0, 10.0, 200.0);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_P))
        {
            glMatrixMode (GL_PROJECTION);
            glLoadIdentity();
            gluPerspective(50,LWJGLSandbox.DISPLAY_WIDTH/ LWJGLSandbox.DISPLAY_HEIGHT,10,200);
        }
    }

    private void cameraMove(){
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glRotatef(cameraA,0.0F,1.0F,0.0F);
        glTranslatef(cameraX,cameraY,cameraZ);
    }

    //This method is the one that actually draws to the screen.
    @Override
    public void render() 
    {
        //This clears the screen.
        glClear(GL_COLOR_BUFFER_BIT);

        glMatrixMode(GL_MODELVIEW);

        //Do your drawing here.
        drawHouse(0,0,0,1,1,1);
        drawHouse(0,30,0,255,0,0);
        drawHouse(0,60,0,0,255,0);
        drawHouse(-90,80,20,0,0,255);
        drawHouse(180,0,40,255,165,0);
        drawHouse(180,30,40,0,206,209);
        drawHouse(180,60,40,255,0,147);

    }

    private void drawHouse(float rotation, float translationX, float translationZ, float colorR, float colorG, float colorB)
    {
        glPushMatrix();
        glColor3f(colorR,colorG,colorB);
        glTranslatef(translationX,0,translationZ);
        glRotatef(rotation, 0.0F, 1.0F, 0.0F);

        glBegin(GL_LINES);
        Iterator<Line3D> lines = model.getLines();
        while(lines.hasNext()) {
            Line3D curLine = lines.next();
            Point3D start = curLine.start;
            Point3D end = curLine.end;
            glVertex3d(start.x,start.y,start.z);
            glVertex3d(end.x,end.y,end.z);
        }
        glEnd();
        glPopMatrix();
    }

    private float degreesToRadians(float degrees){
        return degrees * (float)Math.PI / 180;
    }
}
