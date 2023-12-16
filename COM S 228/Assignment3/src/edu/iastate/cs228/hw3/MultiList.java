package edu.iastate.cs228.hw3;

import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Implementation of the list interface based on linked nodes
 * that store multiple items per node.  Rules for adding and removing
 * elements ensure that each node (except possibly the last one)
 * is at least half full.
 */
public class MultiList<E extends Comparable<? super E>> extends AbstractSequentialList<E>
{
  /**
   * Default number of elements that may be stored in each node.
   */
  private static final int DEFAULT_NODESIZE = 4;
  
  /**
   * Number of elements that can be stored in each node.
   */
  private final int nodeSize;
  
  /**
   * Dummy node for head.  It should be private but set to public here only  
   * for grading purpose.  In practice, you should always make the head of a 
   * linked list a private instance variable.  
   */
  public Node head;
  
  /**
   * Dummy node for tail.
   */
  private Node tail;
  
  /**
   * Number of elements in the list.
   */
  private int size;
  
  /**
   * Constructs an empty list with the default node size.
   */
  public MultiList()
  {
    this(DEFAULT_NODESIZE);
  }

  /**
   * Constructs an empty list with the given node size.
   * @param nodeSize number of elements that may be stored in each node, must be 
   *   an even number
   */
  public MultiList(int nodeSize)
  {
    if (nodeSize <= 0 || nodeSize % 2 != 0) throw new IllegalArgumentException();
    
    // dummy nodes
    head = new Node();
    tail = new Node();
    head.next = tail;
    tail.previous = head;
    this.nodeSize = nodeSize;
  }
  
  /**
   * Constructor for grading only.  Fully implemented. 
   * @param head
   * @param tail
   * @param nodeSize
   * @param size
   */
  public MultiList(Node head, Node tail, int nodeSize, int size)
  {
	  this.head = head; 
	  this.tail = tail; 
	  this.nodeSize = nodeSize; 
	  this.size = size; 
  }

  @Override
  public int size()
  {
    return size;
  }
  
  @Override
  public boolean add(E item) 
  {    
	  	if(item == null) {
	  		throw new NullPointerException();
	  	}
	  	if(tail.previous.count >= nodeSize || tail.previous == head) {
	  		Node newNode = new Node();
	  		newNode.addItem(item);
	  		newNode.previous = tail.previous;
	  		newNode.next = tail;
	  		tail.previous.next = newNode;
	  		tail.previous = newNode;
	  	} else {
	  		tail.previous.addItem(item);
	  	}
	  	size++;
		return true;
  }
  
  @Override
  public void add(int pos, E item)
  {
	  if(pos < 0) {
		  throw new IllegalArgumentException();
	  }
	  
	  NodeInfo info = find(pos);
	  add(info.node, info.offset, item);
	  size++;
  }

  @Override
  public E remove(int pos)
  {
	  NodeInfo info = find(pos);
	  E data = null;
	  
	  if(pos >= size || pos < 0) {
		  throw new IllegalArgumentException();
	  }
	  
	  if(info.node.next == tail && info.node.count == 1) {
		  info.node.previous.next = tail;
		  tail.previous = head;
		  data = info.node.data[info.offset];
		  info.node.removeItem(info.offset);
	  } else if(info.node.next == tail || info.node.count > nodeSize / 2) {
		  data = info.node.data[info.offset];
		  info.node.removeItem(info.offset);
	  } else {
		  
		  data = info.node.data[info.offset];
		  info.node.removeItem(info.offset);
		  Node next = info.node.next;
		  
		  if(next.count > nodeSize / 2) {
			  E nextData = next.data[0];
			  info.node.addItem(nextData);
			  next.removeItem(0);
		  } else if(next.count < nodeSize / 2) {
			  for(int i = 0; i < next.count; i++) {
				  E nextData = next.data[i];
				  info.node.addItem(nextData);
				  next.removeItem(i);
			  }
			  tail.previous = info.node;
			  info.node.next = tail;
		  }	
	  }
	  size--;
    return data;
  }
  
  /**
   * Helper method to insert E item at a specific offset in node
   * @param node
   * @param offset
   * @param item
   * @return
   */
  private NodeInfo add(Node node, int offset, E item) {
	  
	  if(item == null) {
		  throw new NullPointerException();
	  }
	  if(size == 0) {
		  Node curr = new Node();
		  curr.previous = node;
		  curr.next = node.next;
		  node.next.previous = curr;
		  node.next = curr;
	  } else if(offset == 0 && node.previous != head && node.previous.count < nodeSize) {
		  node = node.previous;
		  offset = node.count;
	  } else if(offset == 0 && node == tail) {
		  Node curr = new Node();
		  curr.previous = node;
		  curr.next = node.next;
		  node.next.previous = curr;
		  node.next = curr;
	  } else if(node.count < nodeSize) {
		  //no work needed to be done
	  } else if(node.count == nodeSize) {
		  Node newNode = split(node);
		  
		  if(offset > nodeSize / 2) {
			  node = newNode;
			  offset -= nodeSize / 2;
		  } 
	  }
	  node.addItem(offset, item);
	  return new NodeInfo(node, offset);
  }
  
  private class NodeInfo {
	  
	  public Node node;
	  public int offset;
	  public NodeInfo(Node node, int offset) {
		  this.node = node;
		  this.offset = offset;
	  }
  }
  
  /**
   * Finds and returns the node info of where in the multiList 
   * at a position the node occurs with an offset
   * @param pos
   * @return
   */
  private NodeInfo find(int pos) {
	  Node curr = head;
	  while(curr.next != null) {
		  curr = curr.next;
		  if(pos == 0) {
			  return new NodeInfo(curr, 0);
		  } else if(pos < curr.count) {
			  return new NodeInfo(curr, pos);
		  } else {
			  pos -= curr.count;
		  }
	  }
	  return new NodeInfo(tail, 0);
  }
  
  /**
   * Allows data to be put in at index where shifting data is needed
   * @param node
   * @return
   */
  private Node split(Node node) {
	  	Node curr = new Node();
		curr.previous = tail.previous;
		curr.next = tail;
		tail.previous.next = curr;
		tail.previous = curr;

	  for(int i = nodeSize - 1; i >= nodeSize / 2; i--) {
		  curr.addItem(0, node.data[i]);
		  node.removeItem(i);
	  }
	  
	  return curr;
  }
  
  /**
   * Sort all elements in the Multi list in NON-DECREASING order. You may do the following. 
   * Traverse the list and copy its elements into an array, deleting every visited node along 
   * the way.  Then, sort the array by calling the insertionSort() method.  (Note that sorting 
   * efficiency is not a concern for this project.)  Finally, copy all elements from the array 
   * back to the Multi list, creating new nodes for storage. After sorting, all nodes but 
   * (possibly) the last one must be full of elements.  
   *  
   * Comparator<E> must have been implemented for calling insertionSort().    
   */
  public void sort()
  {
	  Comparator<? super E> comp = new Comparator<>() {
		@Override
		public int compare(E d1, E d2) {
			
			return d1.compareTo(d2);
		}
	  };
	  
	  E[] data = (E[]) new Comparable[size];
	  int pos = 0;
	  Node curr = head.next;
	  while(curr != null) {
		  for(int i = curr.count; i > 0; i--) {
			  data[pos] = curr.data[0]; 
			  curr.removeItem(0);
			  pos++;
		  }
		  curr = curr.next;
	  }
	  head.next = tail;
	  tail.previous = head;
	  
	  insertionSort(data, comp);
	  
	  for(int i = 0; i < data.length; i++) {
		  this.add(data[i]);
	  }
  }
  
  /**
   * Sort all elements in the Multi list in NON-INCREASING order. Call the bubbleSort()
   * method.  After sorting, all but (possibly) the last nodes must be filled with elements.  
   *  
   * Comparable<? super E> must be implemented for calling bubbleSort(). 
   */
  public void sortReverse() 
  {	  
	  E[] data = (E[]) new Comparable[size];
	  int pos = 0;
	  Node curr = head.next;
	  while(curr != null) {
		  for(int i = curr.count; i > 0; i--) {
			  data[pos] = curr.data[0]; 
			  curr.removeItem(0);
			  pos++;
		  }
		  curr = curr.next;
	  }
	  head.next = tail;
	  tail.previous = head;
	  
	 bubbleSort(data);
	  
	  for(int i = 0; i < data.length; i++) {
		  this.add(data[i]);
	  }
  }
  
  @Override
  public Iterator<E> iterator()
  {
    return new MultiListIterator(0);
  }

  @Override
  public ListIterator<E> listIterator()
  {
    return new MultiListIterator();
  }

  @Override
  public ListIterator<E> listIterator(int index)
  {
    return new MultiListIterator(index);
  }
  
  /**
   * Returns a string representation of this list showing
   * the internal structure of the nodes.
   */
  public String toStringInternal()
  {
    return toStringInternal(null);
  }

  /**
   * Returns a string representation of this list showing the internal
   * structure of the nodes and the position of the iterator.
   *
   * @param iter
   *            an iterator for this list
   */
  public String toStringInternal(ListIterator<E> iter) 
  {
      int count = 0;
      int position = -1;
      if (iter != null) {
          position = iter.nextIndex();
      }

      StringBuilder sb = new StringBuilder();
      sb.append('[');
      Node current = head.next;
      while (current != tail) {
          sb.append('(');
          E data = current.data[0];
          if (data == null) {
              sb.append("-");
          } else {
              if (position == count) {
                  sb.append("| ");
                  position = -1;
              }
              sb.append(data.toString());
              ++count;
          }

          for (int i = 1; i < nodeSize; ++i) {
             sb.append(", ");
              data = current.data[i];
              if (data == null) {
                  sb.append("-");
              } else {
                  if (position == count) {
                      sb.append("| ");
                      position = -1;
                  }
                  sb.append(data.toString());
                  ++count;

                  // iterator at end
                  if (position == size && count == size) {
                      sb.append(" |");
                      position = -1;
                  }
             }
          }
          sb.append(')');
          current = current.next;
          if (current != tail)
              sb.append(", ");
      }
      sb.append("]");
      return sb.toString();
  }


  /**
   * Node type for this list.  Each node holds a maximum
   * of nodeSize elements in an array.  Empty slots
   * are null.
   */
  private class Node
  {
    /**
     * Array of actual data elements.
     */
    // Unchecked warning unavoidable.
    public E[] data = (E[]) new Comparable[nodeSize];
    
    /**
     * Link to next node.
     */
    public Node next;
    
    /**
     * Link to previous node;
     */
    public Node previous;
    
    /**
     * Index of the next available offset in this node, also 
     * equal to the number of elements in this node.
     */
    public int count;

    /**
     * Adds an item to this node at the first available offset.
     * Precondition: count < nodeSize
     * @param item element to be added
     */
    void addItem(E item)
    {
      if (count >= nodeSize)
      {
        return;
      }
      data[count++] = item;
      //useful for debugging
      //      System.out.println("Added " + item.toString() + " at index " + count + " to node "  + Arrays.toString(data));
    }
  
    /**
     * Adds an item to this node at the indicated offset, shifting
     * elements to the right as necessary.
     * 
     * Precondition: count < nodeSize
     * @param offset array index at which to put the new element
     * @param item element to be added
     */
    void addItem(int offset, E item)
    {
      if (count >= nodeSize)
      {
    	  return;
      }
      for (int i = count - 1; i >= offset; --i)
      {
        data[i + 1] = data[i];
      }
      ++count;
      data[offset] = item;
      //useful for debugging 
//      System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
    }

    /**
     * Deletes an element from this node at the indicated offset, 
     * shifting elements left as necessary.
     * Precondition: 0 <= offset < count
     * @param offset
     */
    void removeItem(int offset)
    {
      E item = data[offset];
      for (int i = offset + 1; i < nodeSize; ++i)
      {
        data[i - 1] = data[i];
      }
      data[count - 1] = null;
      --count;
    }    
  }
 
  private class MultiListIterator implements ListIterator<E>
  {
	// constants you possibly use ...
	  int pos;
	  NodeInfo cursor;
	  int dir; //1 is right, -1 is left
	  
	// instance variables ... 
	  E data;
    /**
     * Default constructor 
     */
    public MultiListIterator()
    {
    	pos = 0;
    	dir = 0;
    	cursor = find(pos);
    }

    /**
     * Constructor finds node at a given position.
     * @param pos
     */
    public MultiListIterator(int pos)
    {
    	if(pos > size || pos < 0) { 
    		throw new NoSuchElementException();
    	}
    	dir = 0;
    	this.pos = pos;
    	cursor = find(pos);
    }

    @Override
    public boolean hasNext()
    {
    	return pos < size;
    }

    @Override
    public E next()
    {
    	if(!hasNext()) {
    		throw new NoSuchElementException();
    	}
    	
    	data = cursor.node.data[cursor.offset];
    	++pos;
    	cursor = find(pos);
    	dir = -1;
    	return data;
    }
    
    @Override
    public int nextIndex() {
    	return pos;
    }
    
    @Override
    public int previousIndex() {
    	return pos - 1;
    }
    
    @Override
    public void remove() {
    	if(dir == 0) {
    		throw new IllegalStateException();
    	} 
    	if(dir == 1) {
    		MultiList.this.remove(pos);
    		cursor = find(pos);
    	} else if(dir == -1) {
    		MultiList.this.remove(pos - 1);
    		--pos;
    		cursor = find(pos);
    	}
    	size--;
    	dir = 0;
    }
    
    @Override
    public void set(E e) {
    	if(dir == 0) {
    		throw new IllegalStateException();
    	}
    	if(dir == 1) {
    		cursor.node.data[cursor.offset]= e; 
    	} else if(dir == -1) {
    		NodeInfo previous = find(pos - 1);
    		previous.node.data[previous.offset] = e;
    	} 
    }
    
    
    @Override
    public E previous() {
    	if(!hasPrevious()) {
    		throw new NoSuchElementException();
    	}
    	pos--;
    	cursor = find(pos);
    	dir = 1;
    	data = cursor.node.data[cursor.offset];
    	
    	return data;
    }
    
    
    @Override
    public boolean hasPrevious() {
    	return pos > 0;
    }

	@Override
	public void add(E e) {
		MultiList.this.add(pos, e);
		dir = 0;
		pos++;
		cursor = find(pos);
	}
	
	
    // Other methods you may want to add or override that could possibly facilitate 
    // other operations, for instance, addition, access to the previous element, etc.
    // 
    // ...
    // 
  }
  

  /**
   * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING order. 
   * @param arr   array storing elements from the list 
   * @param comp  comparator used in sorting 
   */
  private void insertionSort(E[] arr, Comparator<? super E> comp)
  {
	  for(int i = 0; i < arr.length; i++) {
		  
		  int j = i - 1;
		  while(j > -1 && comp.compare(arr[j], arr[i]) >= 1) {
			  swap(arr, i, j);
			  i--;
			  j--;
		  }
	  }
  }
  
  /**
   * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a 
   * description of bubble sort please refer to Section 6.1 in the project description. 
   * You must use the compareTo() method from an implementation of the Comparable 
   * interface by the class E or ? super E. 
   * @param arr  array holding elements from the list
   */
  private void bubbleSort(E[] arr)
  {
	  // TODO
	  Comparator<? super E> comp = new Comparator<>() {
		@Override
		public int compare(E d1, E d2) {
			
			return d2.compareTo(d1);
		}
	  };
	  
	  int swaps = 1;
	  while(swaps != 0) {
		  swaps = 0;
		  for(int i = 0; i < arr.length - 1; i++) {
			  E temp = arr[i];
			  if(comp.compare(arr[i], arr[i+1]) >= 1) {
				  swap(arr, i, i+1);
				  swaps++;
			  }
		  }
	  }
	    
  }
  
  /**
   * Private method to swap the values at indicies i and j
   * @param arr
   * @param i
   * @param j
   */
  private void swap(E[] arr, int i, int j) {
	  E temp = arr[i];
	  arr[i] = arr[j];
	  arr[j] = temp;  
  }
 
}





