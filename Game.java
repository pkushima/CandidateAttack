import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.awt.Color;

public class Game 
{ 
    public static void mainMenu() 
    {                   
        // Main Menu dimensions
        StdDraw.setXscale(0.0, 16.0);
        StdDraw.setYscale(0.0, 16.0);
        
        // Main Menu Background
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledSquare(8.0, 8.0, 8.0);
        
        // Main Menu Title
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.filledRectangle(8.0, 11.0, 4.0, 1.0);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(8.0, 11.0, "Main Menu");
        
        // Main Menu Play
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledRectangle(8.0, 7.0, 2.0, 1.0);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(8.0, 7.0, "Play");
        
        // Main Menu High Scores
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledRectangle(8.0, 3.0, 2.0, 1.0);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(8.0, 3.0, "High Score");
        
        // Main Menu Exit
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledRectangle(14.5, 0.5, 1.5, 0.5);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(14.5, 0.5, "Exit");
        
        while (true) 
        {
            if (StdDraw.mousePressed())
            {
                //  Exit not clicked
                if ((StdDraw.mouseX() >= 13.0 && StdDraw.mouseX() <= 16.0 
                         && StdDraw.mouseY() >= 0.0 && StdDraw.mouseY() <=  1.0)) 
                {
                    System.exit(0);
                }
                
                else if (StdDraw.mouseX() >= 6.0 && StdDraw.mouseX() <= 10.0) 
                {
                    // if Play clicked
                    if (StdDraw.mouseY() >= 6.0 && StdDraw.mouseY() <= 8.0) 
                    {                                            
                        playGame();
                        break;   
                    }
                    // if High Scores clicked
                    else if (StdDraw.mouseY() >= 2.0 && StdDraw.mouseY() <= 4.0) 
                    {
                        System.out.println("High Scores");
                    }
                }
            }  
        }     
    }
    
    public static void playGame()
    {
        StdDraw.setXscale(-1.0, 1.0);
        StdDraw.setYscale(-1.0, 1.0);
        
        // initialize new ArrayList
        ArrayList<Ball> balls = new ArrayList<Ball>();
        
        int count = 0; 
        int DIFFICULTY = 60;  // determines the difficulty level
        int SCALE = 1;
        int score = 0;
       
        while (true) 
        {               
            // check whether we should end the game
            for (int i = 0; i < balls.size(); i++) 
            { 
                if (balls.get(i).returnTime() >= 3) 
                {                    
                    if (balls.get(i).returnColor() == StdDraw.BLACK) // black ball is removed if not touched
                    {
                        balls.remove(balls.get(i));
                    }                   
                    else gameOver(score);    // game ends if a non black ball is not touched              
                }                
            }  
            
            if (count == SCALE * 10 * DIFFICULTY && DIFFICULTY >= 20) 
            {
                //increase difficulty
                DIFFICULTY -= 10;
                SCALE++;
                count = 0;
            }
            
            // draw background
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.filledSquare(0.0, 0.0, 1.0);
            
            // generate balls at regular intervals based on the difficulty level
            if (count % DIFFICULTY == 0) 
            {
                Point2D.Double p2D;
                boolean overlapping;
                
                do
                {
                    p2D = new Point2D.Double(Math.random() * 1.5 - 0.75, Math.random()*1.5 - 0.75);
                    
                    overlapping = false;
                    
                    for(int i = 0; i < balls.size(); i++)
                    {
                        if (balls.get(i).returnPoint().distance(p2D) <= 2 * balls.get(i).returnRadius()) 
                        {
                            overlapping = true;
                            break;
                        }
                    }
                } 
                while(overlapping && balls.size() >= 1);
                
                balls.add(new Ball(p2D));
            }
            
            count++;           
            
            // check for collisions
            for (int i = 0; i < balls.size(); i++)
            {
                for (int j = i + 1; j < balls.size(); j++)
                {
                    balls.get(i).collide(balls.get(j));
                }                
            }
            
            if (StdDraw.mousePressed()) 
            {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY(); 
                Point2D.Double input = new Point2D.Double(x, y);
                
                for (int i = 0; i < balls.size(); i++)
                {
                    if (balls.get(i).intersects(input)) 
                    {
                        balls.get(i).addTap();
                        if (balls.get(i).satisfied())
                        {                    
                            if (balls.get(i).returnColor() == StdDraw.RED) score++;
                            else if (balls.get(i).returnColor() == StdDraw.BLUE) score ++;
                            else if (balls.get(i).returnColor() == StdDraw.GREEN) 
                            {
                                Point2D.Double p2D;
                                p2D = new Point2D.Double(Math.random() * 1.5 - 0.75, Math.random()*1.5 - 0.75);
                                balls.add(new Ball(p2D, 0));
                                
                                p2D = new Point2D.Double(Math.random() * 1.5 - 0.75, Math.random()*1.5 - 0.75);
                                balls.add(new Ball(p2D, 0));
                                
                            }
                            else if (balls.get(i).returnColor() == StdDraw.MAGENTA) score += 2;
                            else gameOver(score); // end game if black ball is touched
                            balls.remove(balls.get(i));  
                        }
                    } 
                }
            }
            
            // move and draw  all balls
            for (int i = 0; i < balls.size(); i++)
            {
                StdDraw.setPenColor(balls.get(i).returnColor());
                balls.get(i).move();
                balls.get(i).draw();
            }
            // wait
            StdDraw.show(20);           
        }
    }
    
    public static void gameOver(int score) 
    {        
        // Game Over screen dimensions
        StdDraw.setXscale(0.0, 16.0);
        StdDraw.setYscale(0.0, 16.0);
        
        // Game Over Background
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledSquare(8.0, 8.0, 8.0);
        
        // Game Over Title
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.filledRectangle(8.0, 11.0, 4.0, 1.0);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(8.0, 11.0, "Game Over");
        
        // Main Menu Score
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledRectangle(8.0, 7.0, 2.0, 1.0);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(8.0, 7.0, "Score: " + score);
        
        // Retry
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledRectangle(8.0, 3.0, 2.0, 1.0);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(8.0, 3.0, "Retry");
        
        // Main Menu Button
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledRectangle(8.0, 0.5, 1.5, 0.5);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(8.0, 0.5, "Main Menu");
        
        // Button Interactions
        if (StdDraw.mousePressed()) 
        {
            // if Retry clicked, initiate new Game
            if (StdDraw.mouseX() >= 6.0 && StdDraw.mouseX() <= 10.0 && StdDraw.mouseY() >= 2.0 && StdDraw.mouseY() <= 4.0) 
            {
                playGame();
                //break;
            }
            // if Main Menu button clicked, return to Main Menu
            else if (StdDraw.mouseX() >= 6.5 && StdDraw.mouseX() <= 9.5 
                         && StdDraw.mouseY() >= 0.0 && StdDraw.mouseY() <=  1.0) 
            {
                return;     // work on this    
            }           
        }
    }
    
    public static void highScores()
    {
        // High Score Dimensions
        StdDraw.setXscale(0.0, 16.0);
        StdDraw.setYscale(0.0, 16.0);
        
        // Main Menu Background
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledSquare(8.0, 8.0, 8.0);
        
        // High Scores Title
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.filledRectangle(8.0, 13.0, 4.0, 1.0);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(8.0, 13.0, "High Scores");
        
        
        // create list of high scores        
        for (int i = 1; i <= 10; i++) 
        { 
            StdDraw.setPenColor(StdDraw.GRAY);
            StdDraw.filledRectangle(8.0, 11.0 - i , 2.0, 0.5);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.text(8.0, 11.0, i + ": ");
        }
        
        
        // Return Button
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledRectangle(14.5, 0.5, 1.5, 0.5);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(14.5, 0.5, "Back");
    }
    
    public static void main(String[] args)
    {       
        // Main Menu dimensions
        StdDraw.setXscale(0.0, 16.0);
        StdDraw.setYscale(0.0, 16.0);
        
        // Main Menu Background
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledSquare(8.0, 8.0, 8.0);
        
        // Main Menu Title
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.filledRectangle(8.0, 11.0, 4.0, 1.0);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(8.0, 11.0, "Main Menu");
        
        // Main Menu Play
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledRectangle(8.0, 7.0, 2.0, 1.0);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(8.0, 7.0, "Play");
        
        // Main Menu High Scores
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledRectangle(8.0, 3.0, 2.0, 1.0);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(8.0, 3.0, "High Score");
        
        // Main Menu Exit
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledRectangle(14.5, 0.5, 1.5, 0.5);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(14.5, 0.5, "Exit");
        
        while (true) 
        {
            if (StdDraw.mousePressed())
            {
                //  Exit not clicked
                if ((StdDraw.mouseX() >= 13.0 && StdDraw.mouseX() <= 16.0 
                         && StdDraw.mouseY() >= 0.0 && StdDraw.mouseY() <=  1.0)) 
                {
                    System.exit(0);
                }
                
                else if (StdDraw.mouseX() >= 6.0 && StdDraw.mouseX() <= 10.0) 
                {
                    // if Play clicked
                    if (StdDraw.mouseY() >= 6.0 && StdDraw.mouseY() <= 8.0) 
                    {                                            
                        playGame();
                    }
                    // if High Scores clicked
                    else if (StdDraw.mouseY() >= 2.0 && StdDraw.mouseY() <= 4.0) 
                    {
                        highScores();                        
                    }
                }
            }             
        }
    }
}


