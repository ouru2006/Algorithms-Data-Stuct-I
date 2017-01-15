/*
	Ru Ou
	V00835123
*/
/*  
   CSC 225 - Fall 2015
   Assignment 5 - Template for the 9-puzzle
   
   This template includes some testing code to help verify the implementation.
   Input boards can be provided with standard input or read from a file.
   
   To provide test inputs with standard input, run the program with
	java NinePuzzle
   To terminate the input, use Ctrl-D (which signals EOF).
   
   To read test inputs from a file (e.g. boards.txt), run the program with
    java NinePuzzle boards.txt
	
   The input format for both input methods is the same. Input consists
   of a series of 9-puzzle boards, with the '0' character representing the 
   empty square. For example, a sample board with the middle square empty is
   
    1 2 3
    4 0 5
    6 7 8
   
   And a solved board is
   
    1 2 3
    4 5 6
    7 8 0
   
   An input file can contain an unlimited number of boards; each will be 
   processed separately.
  
   B. Bird    - 07/11/2014
   M. Simpson - 11/07/2015
*/


import java.io.File;
import java.util.*;

@SuppressWarnings("unchecked")
public class NinePuzzle{

	//The total number of possible boards is 9! = 1*2*3*4*5*6*7*8*9 = 362880
	public static final int NUM_BOARDS = 362880;
	private boolean[] marked;  // marked[v] = is there an s-v path
    private int[] edgeTo;      // edgeTo[v] = previous edge on shortest s-v path
    private int[] distTo;      // distTo[v] = number of edges shortest s-v path
    public static LinkedList<Integer>[] G;
    
    /**
     * Initializes an empty graph with <tt>V</tt> vertices and 0 edges.
     * param NUM_BOARDS the number of vertices
     *
     * @param  NUM_BOARDS number of vertices
     */    
	static {
		
        G= (LinkedList<Integer>[])new LinkedList[NUM_BOARDS];
        for (int v = 0; v < NUM_BOARDS; v++) {
            G[v]=new LinkedList<Integer>();
            G[v].add(v);
           
           
        }
        BuildNGraph(); 
    }
	
    /**
     * Computes the shortest path between the source vertex <tt>s</tt>
     * and every other vertex in the graph <tt>G</tt>.
     * @param v the input vertex
     * @param s the goal vertex
     */
    public NinePuzzle(int s) {
        marked = new boolean[G.length];
        distTo = new int[G.length];
        edgeTo = new int[G.length];
        bfs(s);

    }

    // breadth-first search from a single source
    private void bfs(int s) {
        Queue<Integer> q = new Queue<Integer>();
        for (int v = 0; v < G.length; v++)
            distTo[v] = 0;
        distTo[s] = 0;
        marked[s] = true;
        q.enqueue(s);

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w=0; w<G[v].size();w++) {
                if (!marked[G[v].get(w)]) {
                    edgeTo[G[v].get(w)] = v;
                    distTo[G[v].get(w)] = distTo[v] + 1;
                    marked[G[v].get(w)] = true;
                    q.enqueue(G[v].get(w));
                }
            }
        }
    }


    /**
     * Is there a path between the source vertex <tt>s</tt> (or sources) and vertex <tt>v</tt>?
     * @param v the vertex
     * @return <tt>true</tt> if there is a path, and <tt>false</tt> otherwise
     */
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    /**
     * Returns the number of edges in a shortest path between the source vertex <tt>s</tt>
     * (or sources) and vertex <tt>v</tt>?
     * @param v the vertex
     * @return the number of edges in a shortest path
     */
    public int distTo(int v) {
        return distTo[v];
    }

    /**
     * Returns a shortest path between the source vertex <tt>s</tt> (or sources)
     * and <tt>v</tt>, or <tt>null</tt> if no such path.
     * @param v the vertex
     * @return the sequence of vertices on a shortest path, as an Iterable
     */
    public void pathTo(int v) {
        if (!hasPathTo(v)) return;
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x])
            printBoard(getBoardFromIndex(x));
        printBoard(getBoardFromIndex(x));
        
    }

	/*
	Construct the graph G of all states of the 9-puzzle, when start the class;
	use a linkedlist array to represent graph G ;
	first, find the place of 0,
	then there will be 4 possible options: up, down, left, right
	add all the abled vertices to linkedlist array G
	*/
	public static void BuildNGraph(){
		//set the v vertex.
		int[][]vB=new int[3][3];
		int[][]uB=new int[3][3];		
		for (int n=0;n<362880;n++){		
			//set the u vertex.			
			vB=getBoardFromIndex(n);
			uB=getBoardFromIndex(n);
			for (int i = 0; i < 3 ; i++){
				for (int j = 0; j < 3 ; j++){					
					if(vB[i][j]==0){
						//move up
						if(i>0){
							uB[i][j]=uB[i-1][j];
							uB[i-1][j]=0;
							G[n].add(getIndexFromBoard(uB));	
													
							uB=getBoardFromIndex(n);
							
						}
						//move down
						if(i<2){
							uB[i][j]=uB[i+1][j];
							uB[i+1][j]=0;
							
							G[n].add(getIndexFromBoard(uB));
							
							uB=getBoardFromIndex(n);
						}
						// move left
						if(j>0){
							uB[i][j]=uB[i][j-1];
							uB[i][j-1]=0;
							
							G[n].add(getIndexFromBoard(uB));
							
							uB=getBoardFromIndex(n);
						}
						//moveright
						if(j<2){
							uB[i][j]=uB[i][j+1];
							uB[i][j+1]=0;
							
							G[n].add(getIndexFromBoard(uB));
							
							uB=getBoardFromIndex(n);
							
						}					
					}				
				}
			}	
		}		
	}

	/*  SolveNinePuzzle(B)
		Given a valid 9-puzzle board (with the empty space represented by the 
		value 0),return true if the board is solvable and false otherwise. 
		If the board is solvable, a sequence of moves which solves the board
		will be printed, using the printBoard function below.
	*/
	public static boolean SolveNinePuzzle(int[][] B){
	/* ... Your code here ... */
		
		//Construct the graph G of all states of the 9-puzzle, when start the class;
		
		//Find the vertex v of G corresponding to the input board B.
		int vertexV=getIndexFromBoard(B);
		//Find the vertex u of G corresponding to the goal state.
		int [][]goalB=new int[3][3];
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++)
				goalB[i][j]=(i*3+j+1)%9;	
		}	
		int VertexU=getIndexFromBoard(goalB);
		//Run BFS on G starting at u.
		
		NinePuzzle bfs=new NinePuzzle(VertexU);
		if(bfs.distTo[vertexV]!=0){
			System.out.println("The distance is "+bfs.distTo[vertexV]+".");
			bfs.pathTo(vertexV);
		}
		return bfs.hasPathTo(vertexV);
		
	}
	
	/*  printBoard(B)
		Print the given 9-puzzle board. The SolveNinePuzzle method above should
		use this method when printing the sequence of moves which solves the input
		board. If any other method is used (e.g. printing the board manually), the
		submission may lose marks.
	*/
	public static void printBoard(int[][] B){
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++)
				System.out.printf("%d ",B[i][j]);
			System.out.println();
		}
		System.out.println();
	}
	
	
	/* Board/Index conversion functions
	   These should be treated as black boxes (i.e. don't modify them, don't worry about
	   understanding them). The conversion scheme used here is adapted from
		 W. Myrvold and F. Ruskey, Ranking and Unranking Permutations in Linear Time,
		 Information Processing Letters, 79 (2001) 281-284. 
	*/
	public static int getIndexFromBoard(int[][] B){
		int i,j,tmp,s,n;
		int[] P = new int[9];
		int[] PI = new int[9];
		for (i = 0; i < 9; i++){
			P[i] = B[i/3][i%3];
			PI[P[i]] = i;
		}
		int id = 0;
		int multiplier = 1;
		for(n = 9; n > 1; n--){
			s = P[n-1];
			P[n-1] = P[PI[n-1]];
			P[PI[n-1]] = s;
			
			tmp = PI[s];
			PI[s] = PI[n-1];
			PI[n-1] = tmp;
			id += multiplier*s;
			multiplier *= n;
		}
		return id;
	}
		
	public static int[][] getBoardFromIndex(int id){
		int[] P = new int[9];
		int i,n,tmp;
		for (i = 0; i < 9; i++)
			P[i] = i;
		for (n = 9; n > 0; n--){
			tmp = P[n-1];
			P[n-1] = P[id%n];
			P[id%n] = tmp;
			id /= n;
		}
		int[][] B = new int[3][3];
		for(i = 0; i < 9; i++)
			B[i/3][i%3] = P[i];
		return B;
	}
	

	public static void main(String[] args){
		long heapsize = Runtime.getRuntime().totalMemory();
        System.out.println("heapsize is :: " + heapsize);
		/* Code to test your implementation */
		/* You may modify this, but nothing in this function will be marked */

		
		Scanner s;

		if (args.length > 0){
			//If a file argument was provided on the command line, read from the file
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			//Otherwise, read from standard input
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int graphNum = 0;
		double totalTimeSeconds = 0;
		
		//Read boards until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading board %d\n",graphNum);
			int[][] B = new int[3][3];
			int valuesRead = 0;
			for (int i = 0; i < 3 && s.hasNextInt(); i++){
				for (int j = 0; j < 3 && s.hasNextInt(); j++){
					B[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < 9){
				System.out.printf("Board %d contains too few values.\n",graphNum);
				break;
			}
			System.out.println();
			printBoard(B);
			System.out.printf("Attempting to solve board %d...\n",graphNum);
			long startTime = System.currentTimeMillis();
			boolean isSolvable = SolveNinePuzzle(B);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			if (isSolvable)
				System.out.printf("Board %d: Solvable.\n",graphNum);
			else
				System.out.printf("Board %d: Not solvable.\n",graphNum);
		}
		graphNum--;
		System.out.printf("Processed %d board%s.\n Average Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>1)?totalTimeSeconds/graphNum:0);

	}

}
class Queue<Item> implements Iterable<Item> {
    private Node<Item> first;    // beginning of queue
    private Node<Item> last;     // end of queue
    private int N;               // number of elements on queue

    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    /**
     * Initializes an empty queue.
     */
    public Queue() {
        first = null;
        last  = null;
        N = 0;
    }

    /**
     * Returns true if this queue is empty.
     *
     * @return <tt>true</tt> if this queue is empty; <tt>false</tt> otherwise
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Returns the number of items in this queue.
     *
     * @return the number of items in this queue
     */
    public int size() {
        return N;     
    }

    /**
     * Returns the item least recently added to this queue.
     *
     * @return the item least recently added to this queue
     * @throws NoSuchElementException if this queue is empty
     */
    public Item peek() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        return first.item;
    }

    /**
     * Adds the item to this queue.
     *
     * @param  item the item to add
     */
    public void enqueue(Item item) {
        Node<Item> oldlast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else           oldlast.next = last;
        N++;
    }

    /**
     * Removes and returns the item on this queue that was least recently added.
     *
     * @return the item on this queue that was least recently added
     * @throws NoSuchElementException if this queue is empty
     */
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Item item = first.item;
        first = first.next;
        N--;
        if (isEmpty()) last = null;   // to avoid loitering
        return item;
    }

    /**
     * Returns a string representation of this queue.
     *
     * @return the sequence of items in FIFO order, separated by spaces
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this)
            s.append(item + " ");
        return s.toString();
    } 

    /**
     * Returns an iterator that iterates over the items in this queue in FIFO order.
     *
     * @return an iterator that iterates over the items in this queue in FIFO order
     */
    public Iterator<Item> iterator()  {
        return new ListIterator<Item>(first);  
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }
    }