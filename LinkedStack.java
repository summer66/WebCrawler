package haoyu.webcrawler;

/**
 * A stack implemented by linked list.
 * Each node has two data fields: content to store URL and depth to store the depth of the URL relative to the seed URL
 * Created by Hao Yu on 11/26/2016.
 */
public class LinkedStack
{
    private Node top;
    private int N;

    private class Node
    {
        String content;
        int depth;
        Node next;
    }

    public boolean isEmpty() {return top == null;}
    public int size()  {return N; }

    /**
     * Method to return the content of the top node
     * @return the content of the top node
     */
    public String peek() {return top.content;}

    /**
     * Method to add a new node on the top of the stack
     * @param content
     * @param depth
     */
    public void push(String content, int depth)
    {
        Node oldTop = top;
        top = new Node();
        top.content = content;
        top.depth = depth;
        top.next = oldTop;
        N++;
    }

    /**
     * Method to pop off the top node of the stack and return its depth
     * @return the depth of the top node
     */
    public int pop()
    {
        Node temp = top;
        int depth = top.depth;
        top = top.next;
        temp.next = null;
        N--;
        return depth;
    }

    /**
     * Method to check if the stack contains a node whose contenet matches the content in query
     * @param content
     * @return whether the stack contains the target content
     */
    public boolean contains(String content)
    {
        for(Node first = top; first != null; first = first.next)
        {
            if (first.content == content) return true;
        }
        return false;
    }
}
