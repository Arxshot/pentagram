package mac.ethan;

public class Vec {

    public double x, y;

    public Vec(){
        this(0, 0);
    }

    public Vec(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vec add(Vec a){
        this.x += a.x;
        this.y += a.y;
        return this;
    }

    public Vec subtract(Vec a){
        this.x -= a.x;
        this.y -= a.y;
        return this;
    }

    public static Vec subtract(Vec a, Vec b){
        return new Vec(a.x - b.x, a.y - b.y);
    }

    public Vec multiple(Vec a){
        this.x *= a.x;
        this.y *= a.y;
        return this;
    }

    public Vec multiple(double a){
        this.x *= a;
        this.y *= a;
        return this;
    }

    public static Vec randomDir(){
        double r = 2 * Math.PI * Math.random();
        return new Vec(Math.cos(r), Math.sin(r));
    }

    public static Vec random(){
        Vec temp = randomDir();
        double num = Math.random();
        temp.x *= num;
        temp.y *= num;
        return temp;
    }

    public Vec normalize(){
        double n = Math.sqrt(x*x + y*y);
        this.x /= n;
        this.y /= n;
        return this;
    }

    public static Vec between(Vec a, Vec b, double t){
        Vec temp = Vec.subtract(b, a);
        return temp.multiple(t).add(a);
    }

}
