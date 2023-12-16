import api.Direction;
import hw3.Snake;

public class SimpleSnakeTests
{
  public static void main(String[] args)
  {
    // try an empty snake
    Snake s = new Snake('g');
    System.out.println(s.getId()); // expected 'g'
    System.out.println(s.getPieces()); // expected []
    
    // add some pieces now
    s.addPiece(2, 3);
    s.addPiece(2, 4);
    s.addPiece(3, 4);
    s.addPiece(4, 4);
    System.out.println(s.getPieces()); 
    // expected [(2, 3), (2, 4), (3, 4), (4, 4)]
   
    System.out.println(s.isGreen()); // expected true
    System.out.println(s.getHead()); // (2, 3)
    System.out.println(s.isHead(2, 3)); // expected true
    System.out.println(s.getTail()); // (4, 4)
    System.out.println(s.isTail(4, 4)); // expected true
    
    
    // moving the head adds a new piece at the beginning, others shift right,
    // the tail piece is discarded
    s.moveHead(Direction.UP);
    System.out.println(s.getPieces()); 
    // expected [(1, 3), (2, 3), (2, 4), (3, 4)]
    
    // head and tail are different now...
    System.out.println(s.getHead()); // (1, 3)
    System.out.println(s.getTail()); // (3, 4)

    // moving the tail adds a new piece at the end, others shift left,
    // the head piece is discarded
    s.moveTail(Direction.RIGHT);
    System.out.println(s.getPieces()); 
    // expected [(2, 3), (2, 4), (3, 4), (3, 5)]

    // moveHeadAndGrow is similar to moveHead, but the tail is not discarded
    // so the length increases by 1
    s.moveHeadAndGrow(Direction.DOWN);
    System.out.println(s.getPieces()); 
    // expected [((3, 3), (2, 3), (2, 4), (3, 4), (3, 5)]
    
    // moveHeadAndShrink discards two pieces at the end so the length
    // decreases by 1
    s.moveHeadAndShrink(Direction.LEFT);
    System.out.println(s.getPieces()); 
    // expected [(3, 2), (3, 3), (2, 3), (2, 4)]

    System.out.println();
    
    // try adding piece at an index
    s = new Snake('x');
    System.out.println(s.getId()); //expected 'x'
    s.addPiece(1, 2, 4); // put (2, 4) at index 1
    s.addPiece(3, 4, 4); // put (4, 4) at index 3
    System.out.println(s.getPieces()); 
    // expected [null, (2, 4), null, (4, 4)]
  }
}
