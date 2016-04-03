import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.awt.Color;
import java.io.*;

public class Game 
{ 
    public static void playGame(int[] scores)
    {
        
        //StdAudio.loop("UpbeatFunk.wav"); 
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
                    else 
                    {
                        gameOver(score, scores);    // game ends if a non black ball is not touched 
                    }
                }                
            }  
            
            if (count == SCALE * 10 * DIFFICULTY && DIFFICULTY >= 20) 
            {
                //increase difficulty
                DIFFICULTY -= 10;
                SCALE++;
                count = 0;
            }
            

            //draw background
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
                            else 
                            {
                                gameOver(score, scores); // end game if black ball is touched
                            }
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
    
    public static void gameOver(int score, int[] scores) 
    {                
        String fileName = "HighScores.txt";
        
        try
        {
            FileWriter fileWriter = new FileWriter(fileName);
            
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
            for (int i = 0; i < 10; i++)
            {
                bufferedWriter.write("" + scores[i]);
                bufferedWriter.newLine();
            }
            
            bufferedWriter.write("" + score);
            
            bufferedWriter.close();
        }
        
        catch (IOException ex)
        {
            StdOut.println("Error writing file '" + fileName + "'");
        }
        
        // Game Over screen dimensions
        StdDraw.setXscale(0.0, 16.0);
        StdDraw.setYscale(0.0, 16.0);
        
         // Game Over Background        
        StdDraw.picture(8.0, 8.0, "gameover.png");
                
        // Main Menu High Scores
        StdDraw.picture(8.0, 8.0, "blanksquare.png");
        StdDraw.text(8.0, 8.0, "Score: " + score);
        
        // Main Menu Retry
        StdDraw.picture(8.0, 4.0, "retry.png");
        
        // Main Menu Retry
        StdDraw.picture(8.0, 1.0, "menu.png");        
        
        // Button Interactions
        
        if (StdDraw.mousePressed()) 
        {       
            // if Retry clicked, initiate new Game
            if (StdDraw.mouseX() >= 5.0 && StdDraw.mouseX() <= 11.0 && 
                StdDraw.mouseY() >= 4.0 && StdDraw.mouseY() <= 7.0) 
            {
                playGame(scores);               
            }
            // if Main Menu button clicked, return to Main Menu
            else if (StdDraw.mouseX() >= 5.0 && StdDraw.mouseX() <= 11.0 
                         && StdDraw.mouseY() >= 0.0 && StdDraw.mouseY() <=  3.0) 
            {
                return;     // work on this    
            }           
        }
        
    }
    
    public static void highScores(int[] scores)
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
        
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledRectangle(8.0, 6.0, 2.0, 5.5);
        
        // create list of high scores
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 1; i <= 10; i++) 
        { 
            StdDraw.text(7.0, 11.5 - i, i + ": ");
        }
        for (int i = 0; i < 10; i++) 
        { 
            StdDraw.text(8.0, 10.5 - i, scores[i] + "");
        }
                
        // Return Button
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledRectangle(14.5, 0.5, 1.5, 0.5);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(14.5, 0.5, "Back");
        
         while (true) 
        {
            if (StdDraw.mousePressed())
            {
                if (StdDraw.mouseX() >= 13.0 && StdDraw.mouseX() <= 16.0 &&
                    StdDraw.mouseY() >= 0.0 && StdDraw.mouseY() <= 1.0)
                {
                    break;
                }
            }
            
         }
        
    }
    
    public static void main(String[] args)
    {       
        
        int[] scores = new int[10];
        int lastScore = 0;
        
        // read high scores from txt file
        String fileName = "HighScores.txt";
        
        try
        {
            FileReader fileReader = new FileReader(fileName);
            
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            for (int i = 0; i < 10; i++)
            {
                scores[i] = Integer.parseInt(bufferedReader.readLine());
            }
            
            lastScore = Integer.parseInt(bufferedReader.readLine());
            
            bufferedReader.close();
        }
        
        catch (FileNotFoundException ex)
        {
            StdOut.println("Unable to open file '" + fileName + "'");
        }
        catch (IOException ex)
        {
            StdOut.println("Error reading file '" + fileName + "'");
        }
        
        int temp1 = 0;
        int temp2 = 0;
        
        // comparing last score (11th entry in file) to the 10 scores in array
        for (int i = 0; i < 10; i++)
        {
            if (lastScore > scores[i])
            {
                temp1 = scores[i];
                
                for (int j = i; j < 9; j++)
                {
                    temp2 = scores[j + 1];
                    scores[j + 1] = temp1;
                    temp1 = temp2; 
                }
                
                scores[i] = lastScore;
                break;
            }
        }
       
        try
        {
            FileWriter fileWriter = new FileWriter(fileName);
            
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
            for (int i = 0; i < 10; i++)
            {
                bufferedWriter.write("" + scores[i]);
                bufferedWriter.newLine();
            }
            
            bufferedWriter.write("" + lastScore);
            
            bufferedWriter.close();
        }
        
        catch (IOException ex)
        {
            StdOut.println("Error writing file '" + fileName + "'");
        }
        
        while (true)
        {
            // Main Menu dimensions
            StdDraw.setXscale(0.0, 16.0);
            StdDraw.setYscale(0.0, 16.0);
            
            // Main Menu Background        
            StdDraw.picture(8.0, 8.0, "backgroundfinal.png");
            
            // Main Menu Play
            StdDraw.picture(8.0, 10.0, "play.png");
            
            // Main Menu High Scores
            StdDraw.picture(8.0, 6.0, "highscore.png");
            
            // Main Menu Exit
            StdDraw.picture(8.0, 2.0, "exit.png");
            
            while (true) 
            {
                if (StdDraw.mousePressed())
                {
                    //  if Exit clicked, exit the program
                    if (StdDraw.mouseX() >= 5.0 && StdDraw.mouseX() <= 11.0 &&
                        StdDraw.mouseY() >= 1.5 && StdDraw.mouseY() <= 3.6) 
                    {
                        System.exit(0);
                    }
                    // if Play clicked
                    else if (StdDraw.mouseX() >= 4.0 && StdDraw.mouseX() <= 12.0 &&
                             StdDraw.mouseY() >= 9.0 && StdDraw.mouseY() <= 11.0) 
                    {                                        
                        playGame(scores);
                    }
                    // if High Scores clicked
                    else if (StdDraw.mouseX() >= 5.0 && StdDraw.mouseX() <= 11.0 &&
                             StdDraw.mouseY() >= 5.0 && StdDraw.mouseY() <= 7.0) 
                    {
                        highScores(scores);                        
                    }
                    break;
                }
                
            }
                     
        }
    }
}





