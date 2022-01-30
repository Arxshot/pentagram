package mac.ethan;

import java.awt.*;

public class Spark {

    public Vec pos;
    public Vec velcoity;
    public Color color;

    public Spark(){
        this(new Vec(), new Vec(), Color.black);
    }

    public Spark(Vec pos, Vec velcoity, Color color){
        this.pos = pos;
        this.velcoity = velcoity.normalize();
        this.color = color;
    }

    public void update(double dt){
        double d = (1000 * dt);
        int a = (int)(color.getAlpha() - d/2);
        if (0 <= a) {
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), a);
        } else {
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0);
        }
        velcoity.add(Vec.random().multiple(0.1));
        pos.x += velcoity.x * dt * 100 * Main.pixelSize;
        pos.y += velcoity.y * dt * 100 * Main.pixelSize;
    }

    public void paint(Graphics g){
        g.setColor(color);
        g.fillRect((int)pos.x, (int)pos.y, Main.pixelSize, Main.pixelSize);
    }

}
