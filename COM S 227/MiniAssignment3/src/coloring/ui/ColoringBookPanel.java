package coloring.ui;



import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import coloring.ColoringBook;
import coloring.api.Cell;
import coloring.api.CellObserver;
import coloring.api.Status;


/**
 * UI for a coloring book.  Clicking on any blank cell in the image
 * will fill to the boundary with the current color.
 */
public class ColoringBookPanel extends JPanel implements CellObserver
{
  /**
   * Slowest animation speed...
   */
  private static final int  MAX_SLEEP_TIME = 200;
  
  /**
   * Default animation speed...
   */
  private static final int DEFAULT_SLEEP_TIME = 150;

  /**
   * Size in pixels of the cells for the grid.
   */
  private int cellSize = 10;
  
  // Swing components
  private JButton colorButton;
  private JCheckBox animationCheckBox;
  private JSlider cellSizeControl;
  private JSlider animationSpeedControl;
  private JPanel mainPanel;
  private JPanel scorePanel;
  
  
  /**
   * The game to be displayed by this panel.
   */
  private ColoringBook game;
  
  /**
   * Delay between updates when animating clearRegion.
   */
  private volatile int sleepTime; // shared with helper threads
  
  /**
   * Indicates whether or not an animation is currently taking place.
   */
  private volatile boolean animating; // shared with helper threads
  
  
  
  /**
   * Current drawing color.
   */
  private Color currentColor = Color.RED;
  
  /**
   * Constructs the component.
   */
  public ColoringBookPanel(ColoringBook game, JPanel scorePanel, JPanel mainPanel, int initialSize)
  {
    this.game = game;
    this.sleepTime = DEFAULT_SLEEP_TIME;
    this.mainPanel = mainPanel;
    this.scorePanel = scorePanel;
    addMouseListener(new MyMouseListener());
    
    // components for score panel
  
    colorButton = new JButton("Select color");
    colorButton.addActionListener(new ColorButtonListener());
    scorePanel.add(colorButton);
    
    cellSize = Math.min(initialSize, 40);
    cellSizeControl= new JSlider(JSlider.HORIZONTAL, 1, 40, cellSize);
    cellSizeControl.addChangeListener(new CellSizeControlListener());
    scorePanel.add(new JLabel("Size:"));
    scorePanel.add(cellSizeControl);

    // subtract from MAX_SLEEP_TIME so speed increases left to right
    animationSpeedControl = new JSlider(JSlider.HORIZONTAL, 0, MAX_SLEEP_TIME, MAX_SLEEP_TIME - DEFAULT_SLEEP_TIME);
    animationSpeedControl.addChangeListener(new AnimationSpeedControlListener());
    scorePanel.add(new JLabel("Speed:"));
    scorePanel.add(animationSpeedControl);

    animationCheckBox = new JCheckBox("Animate");  
    animationCheckBox.addActionListener(new AnimateCheckBoxListener());
    scorePanel.add(animationCheckBox);

  }

  /**
   * Receives notification when a cell status is modified.
   * If animating, delays for 'sleepTime' ms.
   */
  @Override
  public void update(Cell cell)
  {
    if (animating && sleepTime > 0)
    {
      // ok to call from auxiliary thread
      repaint();
      
      // for the nonboundary cells, delay for 'sleepTime'
      if (!cell.isBoundary())
      {
        try
        {
          Thread.sleep(sleepTime);
        }
        catch (InterruptedException ignore)
        {
        }
      }
    }
  }


  @Override
  public void paintComponent(Graphics g)
  {
    g.clearRect(0, 0, getWidth(), getHeight());
    
    // draw cells
    for (int row = 0; row < game.getRows(); ++row)
    {
      for (int col = 0; col < game.getColumns(); ++col)
      {
        Cell c = game.getCell(row, col);
        
        // fill the cell 
        Color color = getColor(c);
        g.setColor(color);
        g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
      }
    }

    // draw grid overlay
    if (cellSize >= 10)
    {
      g.setColor(Color.WHITE);
      for (int row = 0; row < game.getRows(); ++row)
      {
        for (int col = 0; col < game.getColumns(); ++col)
        {
          g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize);
        }
      }
    }
    
    // possibly draw lines to animate exploring
    if (animating)
    {
      drawArrows(g);
    }
  }

  /**
   * Returns a color for cells with the given Status.
   * 
   * @param status
   * @return
   */
  private Color getColor(Cell m)
  {
    // dark cyan for cells with value zero
    final Color regionColor = new Color(0, 200, 200);

    if (m.isBoundary())
    {
      return m.getColor();
    }
    
    Status s = m.getStatus();
    if (s == Status.UNSEEN) 
    {
      return Color.WHITE;
    }
    else if (isExploring(m))
    {
      return Color.GREEN;
    }
    else if (s == Status.SEEN)
    {
      return Color.BLUE;
    }
    else if (s == Status.DONE)
    {
      return m.getColor();
    }

    return Color.DARK_GRAY;
  }

  /**
   * Returns true if c is in one of the EXPLORING states.
   * @param c
   * @return
   */
  private boolean isExploring(Cell c)
  {
    Status s = c.getStatus();
    return (s == Status.EXPLORE_UP || s == Status.EXPLORE_LEFT
        || s == Status.EXPLORE_DOWN || s == Status.EXPLORE_RIGHT);
  }

  /**
   * Draws a line from the center of each cell in EXPLORING state to
   * indicate the direction in which it is exploring.
   * @param g
   */
  private void drawArrows(Graphics g)
  {

    g.setColor(Color.BLACK);
    ((Graphics2D)g).setStroke(new BasicStroke(2.0f));

    for (int row = 0; row < game.getRows(); ++row)
    {
      for (int col = 0; col < game.getColumns(); ++col)
      {
        Cell c = game.getCell(row, col);
        {
          if (isExploring(c))
          {
            int x = col * cellSize + cellSize / 2; // - (maze.getColumns() / 2);
            int y = row * cellSize + cellSize / 2; // + (maze.getRows() / 2);
            int x2 = x;
            int y2 = y;
            // String text = "";
            switch (c.getStatus())
            {
              case EXPLORE_UP:
                y2 = y - cellSize;
                break;
              case EXPLORE_LEFT:
                x2 = x - cellSize;
                break;
              case EXPLORE_DOWN:
                y2 = y + cellSize;
                break;
              case EXPLORE_RIGHT:
                x2 = x + cellSize;
                break;
              default:
            }

            g.drawLine(x, y, x2, y2);
          }
        }
      }
    }
  
  }
  
  
  /**
   * Worker thread for running the clearRegion animation.
   */
  private class AnimationWorker extends SwingWorker<Void, Void>
  {
    private int row;
    private int col;
    public AnimationWorker(int r, int c)
    {
      row = r;
      col = c;
    }
    
    @Override
    protected Void doInBackground() throws Exception
    {
      Thread.currentThread().setName("clearRegion worker thread");
      game.fill(row, col, currentColor);
      return null;
    }
    
    @Override
    protected void done()
    {
      // executed in EDT
      animating = false;
      //cellSizeControl.setEnabled(true);
      //colorButton.setEnabled(true);
      repaint();
    }
    
  }
  
  /**
   * Handler for mouse clicks.
   */
  private class MyMouseListener implements MouseListener
  {

    @Override
    public void mouseClicked(MouseEvent e)
    {
      
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
      // ignore clicks if an animation is in progress
      if (!animating)
      {
        final int col = e.getX() / cellSize;
        final int row = e.getY() / cellSize;
 
        if (e.getButton() == MouseEvent.BUTTON1)
        {
          Cell c = game.getCell(row, col);
          if (!c.isBoundary() && sleepTime > 0 && animationCheckBox.isSelected())
          {
            // if it's a cell with count 0, run in worker thread to get animation
            animating = true;
            //cellSizeControl.setEnabled(false);
            //colorButton.setEnabled(false);
            new AnimationWorker(row, col).execute();
          }
          else
          {
            game.fill(row, col, currentColor);
          }
        }
      }
      repaint();
    
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
      // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
      // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
      // TODO Auto-generated method stub
    }
  }

  /**
   * Handler for hint button.
   */
  private class ColorButtonListener implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      if (!animating)
      {
        JColorChooser cc = new JColorChooser();
        JDialog dialog = JColorChooser.createDialog(ColoringBookPanel.this, "Color chooser", true, cc, null, null);
        dialog.setVisible(true); 

        // dialog is modal, so this won't execute until closed
        currentColor = cc.getColor();
        repaint();
      }
    }    
  }

  /**
   * Handler for changes in the "Animate" check box.
   */
  private class AnimateCheckBoxListener implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      if (animating && !animationCheckBox.isSelected())
      {
        animating = false;
      }     
    }        
  }
  
  private class CellSizeControlListener implements ChangeListener
  {

    @Override
    public void stateChanged(ChangeEvent e)
    {
      // TODO Auto-generated method stub
      if (!cellSizeControl.getValueIsAdjusting())
      {
        int size = (int) cellSizeControl.getValue();
        cellSize = size;
        
        ColoringBookPanel.this.setPreferredSize(new Dimension(game.getColumns() * cellSize, game.getRows() * cellSize));    
        mainPanel.removeAll();
        mainPanel.add(scorePanel);
        mainPanel.add(ColoringBookPanel.this);
        JFrame f = (JFrame) SwingUtilities.windowForComponent(ColoringBookPanel.this);
        f.pack();
        //System.out.println(f.getWidth() + ", " + f.getHeight());
        repaint();
      }
    }
    
    
  }

  
  private class AnimationSpeedControlListener implements ChangeListener
  {

    @Override
    public void stateChanged(ChangeEvent e)
    {
      // TODO Auto-generated method stub
      if (!cellSizeControl.getValueIsAdjusting())
      {
        int ms = (int) animationSpeedControl.getValue();
        sleepTime = MAX_SLEEP_TIME - ms;
      }
    }
    
    
  }

}
