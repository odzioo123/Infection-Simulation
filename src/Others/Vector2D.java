package Others;
public class Vector2D implements IVector {
    private double x;
    private double y;

    public Vector2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public double[] getComponents()
    {
        return new double[]{x,y};
    }
    @Override
    public double abs()
    {
        return Math.pow((x*x + y*y), 1./2);
    }
    @Override
    public double cdot(IVector param)
    {
        double[] v1 = this.getComponents();
        double[] v2 = param.getComponents();
        return v1[0] * v2[0] + v1[1] * v2[1];
    }
    public void reverse()
    {
        this.x = -x;
        this.y = -y;
    }
}
