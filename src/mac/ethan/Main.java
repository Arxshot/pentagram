package mac.ethan;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends JFrame {

    private Image imagePentgram;
    private Image imageBuffer;
    private Spark[] sparks = createSparkArray(10000);
    private Drawer[] drawer = createDrawer();
    private int writeIndex = 0;
    private enum State{circle, line , stop};
    private State state = State.circle;

    public static final int pixelSize = 2;
    public static final int size = 750;//ratains
    public static final double pentgramRotate = Math.random() * Math.PI * 2;//ratains
    public static final int pentgramSides = 3;
    public static final Vec[] pentgramPoints = createPentgramPoints(pentgramSides);
    public static final int[] pentgramSidesFactors = createPentgramSidesFactors(pentgramSides);

    private static Vec[] createPentgramPoints(double pentgramSides){
        Vec[] vecs = new Vec[(int)pentgramSides];
        final Vec sizeHalf = new Vec(size / 2, size / 2);
        for (int i = 0; i < pentgramSides; i++) {
            double r = (2 * Math.PI / pentgramSides) * (i) + pentgramRotate;
            vecs[i] = new Vec(Math.cos(r), Math.sin(r)).multiple(size/3).add(sizeHalf);
        }
        return vecs;
    }

    private static int[] createPentgramSidesFactors(double pentgramSides){
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < Math.sqrt(pentgramSides); i++) {
            if (pentgramSides % i == 0){
                temp.add(i);
                temp.add((int)pentgramSides / i);
            }
        }
        int[] ints = new int[temp.size()];
        for (int i = 0; i < ints.length; i++){
            ints[i] = temp.get(i);
        }
        return ints;
    }

    public Main(){
        //setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);

        setTitle("Brick Breaker");
        setSize(size, size);
        setLocation(50, 0);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(true);
        add(panel);

        setBackground(Color.BLACK);
        imagePentgram = createImage(size, size);
        imageBuffer = createImage(size, size);
    }

    public Spark[] createSparkArray(int length){
        Spark[] sparks = new Spark[100];
        for (int i = 0; i < sparks.length; i++) {
            sparks[i] = new Spark();
        }
        return sparks;
    }

    private Drawer[] createDrawer(){
        Drawer[][] drawerGraph = new Drawer[(int)pentgramSides][(int)pentgramSides];
        Drawer[] drawer = new Drawer[1];

        ArrayList<Integer> heads =  new ArrayList<>();
        ArrayList<Drawer> drawerHeads =  new ArrayList<>();
        heads.add((int)MMath.random(0,(int)pentgramSides));

        boolean firstTime = true;

        nextHead:
        while (heads.size() > 0) {
            for (int i = 0; i < heads.size(); i++) { // a is start, b is end
                int a = heads.get(i);
                int b = (int)MMath.random(0,(int)pentgramSides);

                for (int j = 0; j < pentgramSides; j++) {
                    if(a == b){
                        continue;
                    }
                    if (drawerGraph[a][(b + j)%pentgramSides] == null && drawerGraph[(b + j)%pentgramSides][a] == null){

                        drawerGraph[a][(b + j)%pentgramSides] = new Drawer() {
                            private void draw(long currentTime, long pastTime, Graphics g){
                                double r = 0;
                                double speed = 1000;

                                Vec start = pentgramPoints[a];
                                Vec end = pentgramPoints[b];

                                Vec curPos = start;
                                Vec pastPos = start;
                                for (long i = pastTime; i < currentTime; i++) {
                                    r = (i/speed) - (2 * Math.PI) - lineIndex;

                                    curPos = Vec.between(start, end, r);

                                    addPoint(curPos, pastPos, g, speed);

                                    pastPos = curPos;
                                }
                            }
                        }.setDuration(1000);

                        heads.add(i, (b + j)%pentgramSides);
                        if(firstTime){
                            firstTime = false;
                            drawer[0] = drawerGraph[a][b];
                            drawerHeads.add(drawerGraph[a][b]);
                        } else {

                        }
                        continue nextHead;
                    }
                }

                heads.remove(i);
                i--;
            }
        }

        return drawer;
    }

    //private void createLineDrawer

    public static void main(String[] args) {
        // write your code here
        Main main = new Main();
        long startTime = System.currentTimeMillis();
        long currentTime = System.currentTimeMillis();
        long pastTime = System.currentTimeMillis();
        while (true){
            pastTime = currentTime;
            currentTime = System.currentTimeMillis();

            main.update(currentTime - startTime, pastTime - startTime);
            main.repaint();
            try
            {
                Thread.sleep(10);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    private int lineIndex = 0;
    private int lineSkiper = 1;//(int)Math.sqrt(pentgramSides);
    private int lineOffset = 0;//offset them so no over lap
    public void update(long currentTime, long pastTime) {
        double dt = (currentTime - pastTime) / 1000.0;
        Graphics g = imagePentgram.getGraphics();

        final int size =  getWidth();
        final Vec sizeHalf = new Vec(size / 2, size / 2);
        final double thing = 1000.0;// 50;

        switch (state){
            case circle:
                double r = 0;
                Vec curPos = new Vec(Math.cos(pastTime), Math.sin(pastTime)).multiple(size).add(sizeHalf);
                Vec pastPos = new Vec(Math.cos((pastTime - 2)/thing), Math.sin((pastTime - 2)/thing)).multiple(size/3).add(sizeHalf);

                for (long i = pastTime; i < currentTime; i++) {
                    r = i/thing + pentgramRotate;
                    if(r > 2 * Math.PI + pentgramRotate){
                        state = State.line;
                        break;
                    }

                    curPos = new Vec(Math.cos(r), Math.sin(r)).multiple(size/3).add(sizeHalf);

                    addPoint(curPos, pastPos, g, thing);

                    pastPos = curPos;
                }
                if(r < 2 * Math.PI + pentgramRotate) {
                    break;
                }

            case line:
                r = 0;
//                r = (2 * Math.PI / pentgramSides) * (lineIndex * lineSkiper) + pentgramRotate;
//                Vec start = new Vec(Math.cos(r), Math.sin(r)).multiple(size/3).add(sizeHalf);
//                r = (2 * Math.PI / pentgramSides) * (lineIndex * lineSkiper + lineSkiper) + pentgramRotate;
//                Vec end = new Vec(Math.cos(r), Math.sin(r)).multiple(size/3).add(sizeHalf);

                Vec start = pentgramPoints[(lineIndex * lineSkiper) % (int)pentgramSides];
                Vec end = pentgramPoints[(lineIndex * lineSkiper + lineSkiper) % (int)pentgramSides];

                curPos = start;
                pastPos = start;
                for (long i = pastTime; i < currentTime; i++) {
                    r = (i/thing) - (2 * Math.PI) - lineIndex;
                    if(r > 1){
                        //state = State.line2;
                        lineIndex++;
                        if(lineIndex % pentgramSides == 0){
                            lineSkiper++;
                            lineOffset = 0;
                            if(lineSkiper > pentgramSides / 2){
                                this.state = State.stop;
                            }
                        }
                        for (int j = 0; j < pentgramSidesFactors.length; j++) {
                            if (pentgramSidesFactors[j] == lineSkiper ){
                                lineOffset++;
                                break;
                            }
                        }
                        break;
                    }

                    curPos = Vec.between(start, end, r);

                    addPoint(curPos, pastPos, g, thing);

                    pastPos = curPos;
                }
                if(r < 1 + 0.0001f){
                    break;
                }

            case stop:
                for (Drawer d : drawer) {
                    //d.update(currentTime, pastTime, g);
                }
                break;

        }

        g = imageBuffer.getGraphics();
        g.drawImage(imagePentgram, 0, 0, null);
        for (int i = 0; i < sparks.length; i++) {
            sparks[i].paint(g);
            sparks[i].update(dt);
        }
    }

    public void addPoint(Vec curPos, Vec pastPos, Graphics g, double val){
        //166,16,30
        int diff = 50;
        int rC = Math.max(0,ThreadLocalRandom.current().nextInt(166 - diff, 166));
        int gC = Math.max(0,ThreadLocalRandom.current().nextInt(16 - diff, 16));
        int bC = Math.max(0,ThreadLocalRandom.current().nextInt(30 - diff, 30));

        g.setColor(new Color(rC,gC,bC));
        Vec temp = new Vec(curPos.x, curPos.y).add(Vec.random().multiple(getWidth() * pixelSize * 0.0025));
        g.fillRect((int)temp.x, (int)temp.y, pixelSize, pixelSize);

        if(Math.random() < (1 / val) * 500) {
            addSpark(temp, Vec.subtract(pastPos, curPos));
        }
    }

    public void addSpark(Vec pos, Vec velcoity){//254, 222, 23
        int r = ThreadLocalRandom.current().nextInt(254, 255);
        int g = ThreadLocalRandom.current().nextInt(222, 255);
        int b = ThreadLocalRandom.current().nextInt(23, 255);
        addSpark(pos, velcoity.add(Vec.random().multiple(0.05)), new Color(r, g, b));
    }

    public void addSpark(Vec pos, Vec velcoity, Color color){
        sparks[writeIndex % sparks.length] = new Spark(pos, velcoity, color);
        writeIndex++;
    }

    public void paint(Graphics g) {
        g.clearRect(0,0, getWidth(), getHeight());
        g.drawImage(imageBuffer, 0, 0, getWidth(), getHeight(), this);
    }

}