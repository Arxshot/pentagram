package mac.ethan;

import sun.misc.Unsafe;

import java.awt.*;

public abstract class Drawer {

    public static Drawer HEAD = new Head();
    public static Drawer TAIL = new Tail();
    public static Drawer DEFAULT = new Head();

    protected long duration = 0;
    protected Drawer next = TAIL;

    protected void update(long currentTime, long pastTime, Graphics g){
        draw(Math.min(currentTime, duration), Math.max(pastTime, 0), g);
        if(duration < currentTime){
            next.update(currentTime - duration, pastTime - duration, g);
        }
    }

    protected Drawer[] getCurrentLayer(long currentTime){
        if(currentTime < duration){
            return new Drawer[]{this};
        }
        if(next != null){
            return next.getCurrentLayer(currentTime - duration);
        }
        return new Drawer[]{};
    }

    protected Drawer setNext(Drawer drawer){
        this.next = drawer;
        return this;
    }

    public Drawer setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    protected Drawer spliteWith(Drawer other) {
        return new Spliter(this, other);
    }

    private void draw(long currentTime, long pastTime, Graphics g){

    }

}
class Head extends Drawer{

    private long startTime = 0;

    public Head(){

    }

    @Override
    protected void update(long currentTime, long pastTime, Graphics g) {
        currentTime -= startTime;
        pastTime -= startTime;
        super.update(currentTime, pastTime, g);
    }

    @Override
    protected Drawer[] getCurrentLayer(long currentTime) {
        currentTime -= startTime;
        return super.getCurrentLayer(currentTime);
    }

    public Drawer setStartTime(long startTime){
        this.startTime = startTime;
        return this;
    }

}
class Tail extends Drawer{

    public Tail(){

    }

    @Override
    protected void update(long currentTime, long pastTime, Graphics g) {
    }

    @Override
    protected Drawer[] getCurrentLayer(long currentTime) {
        return null;
    }

}
class Spliter extends Drawer{

    private Drawer a;
    private Drawer b;

    public Spliter(Drawer a, Drawer b){
        this.a = a;
        this.b = b;
    }

    @Override
    public void update(long currentTime, long pastTime, Graphics g) {
        a.update(currentTime, pastTime, g);
        b.update(currentTime, pastTime, g);
    }

    @Override
    protected Drawer[] getCurrentLayer(long currentTime) {
        Drawer[] a = this.a.getCurrentLayer(currentTime);
        Drawer[] b = this.b.getCurrentLayer(currentTime);
        if(a[0] != null) {
            if(b[0] != null){
                Drawer[] c = new Drawer[a.length + b.length];
                int i = 0;
                for (; i < a.length; i++) {
                    c[i] = a[i];
                }
                for (int j = 0; j < b.length; i++, j++) {
                    c[i] = b[j];
                }
                return c;
            } else {
                return a;
            }
        } else if(b[0] != null){
            return b;
        } else {
            return null;
        }
    }

}