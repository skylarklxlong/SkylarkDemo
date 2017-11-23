package online.himakeit.skylarkdemo.github.heart;

/**
 * Created by：LiXueLong 李雪龙 on 2017/11/13 14:17
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:保存点信息，或者说是向量信息。包含向量的基本运算。
 */
public class PointInfo {

    public int x;
    public int y;

    public PointInfo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //旋转
    public PointInfo rotate(float theta) {
        int x = this.x;
        int y = this.y;
        this.x = (int) (Math.cos(theta) * x - Math.sin(theta) * y);
        this.y = (int) (Math.sin(theta) * x + Math.cos(theta) * y);
        return this;
    }

    //乘以一个常数
    public PointInfo mult(float f) {
        this.x *= f;
        this.y *= f;
        return this;
    }

    //复制
    public PointInfo clone() {
        return new PointInfo(this.x, this.y);
    }

    //该点与圆心距离
    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    //向量相减
    public PointInfo subtract(PointInfo p) {
        this.x -= p.x;
        this.y -= p.y;
        return this;
    }

    //向量相加
    public PointInfo add(PointInfo p) {
        this.x += p.x;
        this.y += p.y;
        return this;
    }

    public PointInfo set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
}
