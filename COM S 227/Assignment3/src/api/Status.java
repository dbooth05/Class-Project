package api;

/**
 * Enum representing internal state of a BasketCell in the
 * Snake Escape game grid. Clients normally do not directly
 * use this type, since it can't be accessed after the BasketCell
 * is first constructed.
 * @author smkautz
 */
public enum Status
{
  EMPTY, WALL, EXIT, APPLE, MUSHROOM;
}
