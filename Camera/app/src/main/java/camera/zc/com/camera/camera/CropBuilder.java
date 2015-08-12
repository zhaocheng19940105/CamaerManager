package camera.zc.com.camera.camera;

import java.io.Serializable;

public class CropBuilder implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2031464569857486460L;
    private int x = 0;
    private int y = 0;
    private int weight = 0;
    private int height = 0;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public CropBuilder(int x, int y, int weight, int height) {
        super();
        this.x = x;
        this.y = y;
        this.weight = weight;
        this.height = height;
    }



}
