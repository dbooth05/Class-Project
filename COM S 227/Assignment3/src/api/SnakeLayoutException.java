package api;

/**
 * Exception type used in a Snake Escape game to indicate
 * errors in grid descriptors, snake layout, or invalid snakes.
 * @author smkautz
 */
public class SnakeLayoutException extends RuntimeException
{
  /**
   * Required to quiet compiler warning.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a SnakeLayoutException with the given message.
   * @param msg
   *   string message
   */
  public SnakeLayoutException(String msg)
  {
    super(msg);
  }
}
