package ui;

import static api.Direction.DOWN;
import static api.Direction.LEFT;
import static api.Direction.RIGHT;
import static api.Direction.UP;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JPanel;

import api.BasketCell;
import api.Direction;
import api.SnakePiece;
import hw3.SnakeBasket;
import hw3.Snake;

/**
 * Main panel for the user interface a Snake Escape game.
 * @author smkautz
 */
public class GamePanel extends JPanel
{ 
  /**
   * Suppress compiler warning.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Score panel associated with the game.
   */
  private ScorePanel scorePanel;

  /**
   * The IGame instance for which this is the UI.
   */
  private SnakeBasket game;
  
  /**
   * Constructs a GamePanel with the given game associated ScorePanel.
   * @param game 
   *   the Game instance for which this is the UI
   * @param scorePanel
   *   panel for displaying scores associated with the game
   */
  public GamePanel(SnakeBasket game, ScorePanel scorePanel)
  {
    this.game = game;
    this.scorePanel = scorePanel;
    addMouseListener(new MyMouseListener());
    addMouseMotionListener(new MyMouseMotionListener());
  }


  /**
   * Start over with a new game.
   * @param game
   */
  public void reset(SnakeBasket game)
  {
    this.game = game;
    scorePanel.reset();
    repaint();
  }
  
  // The paintComponent method is invoked by the Swing framework whenever
  // the panel needs to be rendered on the screen.  In this application,
  // repainting is normally triggered by the calls to the repaint() 
  // method in the timer callback and the mouse handlers

  @Override
  public void paintComponent(Graphics g)
  {
    // clear background
    g.setColor(GameMain.BACKGROUND_COLOR);
    g.fillRect(0, 0, getWidth(), getHeight());

    // paint the walls, apples, mushrooms and the exit
    for (int row = 0; row < game.getRows(); ++row)
    {
      for (int col = 0; col < game.getCols(); ++col)
      {
        int x = GameMain.SIZE * col;
        int y = GameMain.SIZE * row;
        BasketCell cell = game.getCell(row, col);
        if (cell.isWall())
        {
          g.setColor(GameMain.WALL_COLOR);
          g.fillRect(x, y, GameMain.SIZE - 1, GameMain.SIZE - 1);
        }
        else if (cell.isExit())
        {
          g.setColor(GameMain.EXIT_COLOR);
          g.fillRect(x, y, GameMain.SIZE - 1, GameMain.SIZE - 1);       
        }
        else if (cell.isApple())
        {
          paintOneCircle(g, row, col, GameMain.APPLE_COLOR, GameMain.DOT_SIZE);
        }
        else if (cell.isMushroom())
        {
          paintOneCircle(g, row, col, GameMain.MUSHROOM_COLOR, GameMain.DOT_SIZE);
        }

      }
    }

    // draw all the cell outlines
    g.setColor(GameMain.GRID_COLOR);
    for (int row = 0; row < game.getRows(); ++row)
    {
      for (int col = 0; col < game.getCols(); ++col)
      {
        int x = GameMain.SIZE * col;
        int y = GameMain.SIZE * row;
        g.drawRect(x, y, GameMain.SIZE - 1, GameMain.SIZE - 1);
      }
    }     

    // draw the snakes
    ArrayList<Snake> snakes = game.getAllSnakes();
    for (Snake s : snakes)
    {
      Color color;
      char id = s.getId();
      if (id == 'G' || id == 'g')
      {
        color = (GameMain.GREEN_SNAKE_COLOR);      
      }
      else
      {
        color = (GameMain.YELLOW_SNAKE_COLOR);      
      }
      
      // draw a line to connect all the pieces
      ArrayList<SnakePiece> cells = s.getPieces();
      if (cells.size() >= 2)
      {
        for (int i = 0; i < cells.size() - 1; ++i)
        {
          SnakePiece src = cells.get(i);
          SnakePiece dst = cells.get(i + 1);
          makeLine(g, src.col(), src.row(), dst.col(), dst.row(), color);
        }       
      }

      // draw the head, we need a second piece to know how to 
      // orient the triangle
      SnakePiece e = s.getHead();      
      SnakePiece e2 = s.getPieces().get(1);
      makeTriangle(g, e2.col(), e2.row(), e.col(), e.row(), color);

      // draw the tail
      e = s.getTail();
      paintOneCircle(g, e.row(), e.col(), color, GameMain.DOT_SIZE - 2);
 
    }
  }
  
  /**
   * Draws line from center of first cell to center of second
   */
  private void makeLine(Graphics g, int col1, int row1, int col2, int row2, Color color)
  {
    int s = GameMain.SIZE;
    int x1 = col1 * s + s / 2;
    int y1 = row1 * s + s / 2;
    int x2 = col2 * s + s / 2;
    int y2 = row2 * s + s / 2;
    g.setColor(color);
    ((Graphics2D) g).setStroke(new BasicStroke(GameMain.LINE_SIZE));
    g.drawLine(x1, y1, x2, y2);
  }
  
  /**
   * Makes a triangle for snake's head at (row2, col2).  The 
   * other pair (row1, col1) is used to orient the direction
   * of the triangle.
   */
  private void makeTriangle(Graphics g, int col1, int row1, int col2, int row2, Color color)
  {
    Graphics2D g2 = (Graphics2D) g;
    int s = GameMain.SIZE;
    int width = GameMain.SIZE / 3;
    double degrees = 0;
    // figure out what direction, 0 degrees is up
    if (row2 == row1)
    {
      if (col2 == col1 + 1)
      {
        degrees = 270;
      }
      else if (col2 == col1 - 1)
      {
        degrees = 90;
      }
    }
    else if (col2 == col1)
    {
      if (row2 == row1 + 1)
      {
        degrees = 0;
      }
      else if (row2 == row1 - 1)
      {
        degrees = 180;
      }
    }
    
    int x2 = col2 * s + s / 2;
    int y2 = row2 * s + s / 2;
    
    int[] xCoords = {x2 - width, x2 + width, x2};
    int[] yCoords = {y2 - width, y2 - width, y2 + width};
    Polygon p = new Polygon(xCoords, yCoords, 3);
    
    g.setColor(color);
    
    //  set up rotation around center x2, y2
    AffineTransform old = g2.getTransform();
    g2.translate(x2, y2);
    g2.rotate(Math.toRadians(degrees));
    g2.translate(-x2, -y2);
    
    g.fillPolygon(p); 
    
    g2.setTransform(old);
    
  }
  
  /**
   * Draws a circle at the given row and column.
   */
  private void paintOneCircle(Graphics g, int row, int col, Color color, int circleSize)
  {
    // scale everything up by the SIZE
    int x = GameMain.SIZE * col;
    int y = GameMain.SIZE * row;
    int offset = (GameMain.SIZE - circleSize) / 2;
    g.setColor(color);
    g.fillOval(x + offset, y + offset, circleSize, circleSize);
  }
  
  /**
   * Callback for mouse events.
   */
  private class MyMouseListener implements MouseListener
  {

    @Override
    public void mouseClicked(MouseEvent event)
    {
    }

    @Override
    public void mousePressed(MouseEvent event)
    {
      if (!game.isOver())
      {     
        int row = event.getY() / GameMain.SIZE;
        int col = event.getX() / GameMain.SIZE;
        game.grabSnake(row, col);
      }
      repaint();
    }

    @Override
    public void mouseReleased(MouseEvent event)
    {
      game.releaseSnake();  
      scorePanel.updateScore(game.getMoves());
      if (game.isOver())
      {
        scorePanel.gameOver();
      }
      repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }
  }
  
  /**
   * Callback for mouse motion events.
   */
  private class MyMouseMotionListener implements MouseMotionListener
  {

    @Override
    public void mouseDragged(MouseEvent e)
    {
      if (!game.isOver()) 
      {
        int row = e.getY() / GameMain.SIZE;
        int col = e.getX() / GameMain.SIZE;

        Snake current = game.currentSnake();
        SnakePiece cell = game.currentWasGrabbedByHead() ? current.getHead() : current.getTail();
        int curRow = cell.row();
        int curCol = cell.col();
        
        Direction dir = null;
        if (col == curCol)
        {
          if (row == curRow + 1)
          {
            dir = DOWN;
          }
          else if (row == curRow - 1)
          {
            dir = UP;
          }
        }
        else if (row == curRow)
        {
          if (col == curCol + 1)
          {
            dir = RIGHT;
          }
          else if (col == curCol - 1)
          {
            dir = LEFT;
          }          
        }
        if (dir != null)
        {
          game.move(dir);
        }
      }
      
      scorePanel.updateScore(game.getMoves());
      if (game.isOver())
      {
        scorePanel.gameOver();
      }
      repaint();

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
    }
    
  }
  

}
