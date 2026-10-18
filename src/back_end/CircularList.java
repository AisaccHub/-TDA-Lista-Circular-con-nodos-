package back_end;

public class CircularList {
    private Node first;
    private Node last;
    
    private int size;
    private Node current;

    public CircularList(){
        this.first = null;
        this.last = null;
        this.size = 0;
        this.current = null;
    }

    // ---------- Insert ----------
    public void insertFirst(Product product){
        Node newNode = new Node(product);

        if (first == null){
            newNode.setNext(newNode);
            newNode.setPrevious(newNode);
            first = newNode;
            last = newNode;
            current = newNode;
        } else {
            newNode.setNext(first);
            newNode.setPrevious(last);
            first.setPrevious(newNode);
            last.setNext(newNode);
            first = newNode;
        }
        size++;
    }

    public void insertLast(Product product){
        Node newNode = new Node(product);

        if (last == null){
            newNode.setNext(newNode);
            newNode.setPrevious(newNode);
            first = newNode;
            last = newNode;
            current = newNode;
        } else {
            newNode.setNext(first);
            newNode.setPrevious(last);
            last.setNext(newNode);
            first.setPrevious(newNode);
            last = newNode;
        }
        size++;
    }

    public void insertAt(Product product, int position){

        if (first == null || position <= 0){
            insertFirst(product);
            return;
        }
        if (last == null || position >= size){
            insertLast(product);
            return;
        }

        Node newNode = new Node(product);
        Node current = first;

        for (int i = 0; i < position - 1; i++){
            current = current.getNext();
        }

        newNode.setNext(current.getNext());
        newNode.setPrevious(current);
        current.getNext().setPrevious(newNode);
        current.setNext(newNode);

        size++;
    }

    // ---------- Remove ----------
    public Product removeFirst(){
        if (first == null){
            return null;
        }

        Product removed = first.getProduct();
        Node removedNode = first;

        if (first == last){
            first = null;
            last = null;
        } else {
            first = first.getNext();
            first.setPrevious(last);
            last.setNext(first);
        }

        if (current == removedNode){
            current = first;
        }

        size--;
        return removed;
    }

    public Product removeLast(){
        if (last == null){
            return null;
        }

        Product removed = last.getProduct();
        Node removedNode = last;

        if (first == last){
            first = null;
            last = null;
        } else {
            last = last.getPrevious();
            last.setNext(first);
            first.setPrevious(last);
        }

        if (current == removedNode){
            current = first;
        }

        size--;
        return removed;
    }

    public Product removeAt(int position){
        if (first == null || position < 0 || position >= size){
            return null;
        }
        if (position == 0){
            return removeFirst();
        }
        if (position == size - 1){
            return removeLast();
        }

        Node current = first;
        for (int i = 0; i < position; i++){
            current = current.getNext();
        }

        Product removed = current.getProduct();
        Node removedNode = current;

        current.getPrevious().setNext(current.getNext());
        current.getNext().setPrevious(current.getPrevious());

        if (this.current == removedNode){
            this.current = removedNode.getNext();
        }

        size--;
        return removed;
    }

    // ---------- Navigation ----------
    public Product next(){
        if (current == null){
            return null;
        }
        current = current.getNext();
        return current.getProduct();
    }

    public Product previous(){
        if (current == null){
            return null;
        }
        current = current.getPrevious();
        return current.getProduct();
    }

    // ---------- Auxiliary ----------
    public int getSize(){
        return size;
    }

    public boolean isEmpty(){
        return first == null;
    }

    public void clear(){
        first = null;
        last = null;
        size = 0;
        current = null;
    }

    // ---------- Show ----------
    public Product showFirst(){
        if (first == null){
            return null;
        }
        return first.getProduct();
    }

    public Product showLast(){
        if (last == null){
            return null;
        }
        return last.getProduct();
    }

    public Product showAt(int position){
        if (first == null || position < 0 || position >= size){
            return null;
        }

        Node current = first;
        for (int i = 0; i < position; i++){
            current = current.getNext();
        }
        return current.getProduct();
    }

    // ---------- Search ----------
    public int searchByValue(int id){
        if (first == null){
            return -1;
        }

        Node current = first;
        for (int i = 0; i < size; i++){
            if (current.getProduct().getId() == id){
                return i;
            }
            current = current.getNext();
        }
        return -1;
    }

    public Product searchByPosition(int position){
        return showAt(position);
    }
}
