import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.awt.Color;

public class Ball  // Ball game
{
    private final double radius; // Ball radius
    private Point2D.Double p;  // Ball position
    private double vx, vy;  // Ball velocity
    private Stopwatch time; // lifespan of ball
    private Color color;
    private int tap;
    private String picture;
    
    public Ball(Point2D.Double p)
    {
        this.p = p;      
        radius = 0.12;
        tap = 0;
        vx = 0.015 - Math.random() * 0.03;
        vy = 0.015 - Math.random() * 0.03;     
        time = new Stopwatch();  
        double gen = Math.random()*4;
        if (gen < 1) 
        {
            color = StdDraw.RED;
            picture = "bernie.png";
        }
        else if (gen < 2) 
        {
            color = StdDraw.BLUE;
            picture = "clinton.png";
        }
        else if (gen < 3)
        {
            color = StdDraw.GREEN;
            picture = "trump.png";
        }
        else 
        {
            color = StdDraw.BLACK;
            picture = "ted.png";
        }
    }
    
    public Ball(Point2D.Double p, int a)
    {
        this.p = p;      
        radius = 0.12;
        tap = 0;
        vx = 0.015 - Math.random() * 0.03;
        vy = 0.015 - Math.random() * 0.03;     
        time = new Stopwatch();
        color = StdDraw.MAGENTA;
        picture = "angrytrump.png";
    }
      
    public double returnTime()
    {
        return time.elapsedTime();
    }
    
    public Point2D.Double returnPoint()
    {
        return p;
    }
    
    public double returnRadius()
    {
        return radius;
    }
    
    public Color returnColor()
    {
        return color;
    }
    
    public void addTap()
    {
        tap++;
    }
    
    public boolean satisfied()
    { 
        if (this.returnColor() == StdDraw.RED || this.returnColor() == StdDraw.BLACK 
                || this.returnColor() == StdDraw.GREEN || this.returnColor() == StdDraw.MAGENTA)
        {
            return true;
        }
        else if (this.returnColor() == StdDraw.BLUE)
        {
            if (this.tap >= 7) return true;
        }
        return false;
    }
    
    public void draw() 
    {
        //StdDraw.filledCircle(this.p.getX(), this.p.getY(), this.radius); 
        StdDraw.picture(this.p.getX(), this.p.getY(), picture);
    }
    
    private void bounceOffVerticalWall() 
    {
        if (Math.abs(this.p.getX() + this.vx) > 1.0 - this.radius) this.vx = -vx;
    }
    
    private void bounceOffHorizontalWall() 
    {
        if (Math.abs(this.p.getY() + this.vy) > 1.0 - this.radius) this.vy = -vy;
    }
    
    public void collide(Ball b2)
    {
        if (this.p.distance(b2.p) <= 2 * this.radius)
        {
            double tempvx = this.vx;
            double tempvy = this.vy;
            
            this.vx = b2.vx;
            this.vy = b2.vy;
            b2.vx = tempvx;
            b2.vy = tempvy;
        }
    }
       
    public boolean intersects(Point2D.Double q)
    {        
        double distance = this.p.distance(q);
        
        if(Math.abs(distance) >=  this.radius)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public void move()
    {
        this.bounceOffVerticalWall();
        this.bounceOffHorizontalWall();
        p = new Point2D.Double(p.getX() + vx, p.getY() + vy);
    }       
}
    

