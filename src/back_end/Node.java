package back_end;

public class Node {
    public Product product;
    public Node next;
    public Node previous;

    public Node(Product product){
        this.product = product;
        this.next = null;
        this.previous = null;
    }

    public Product getProduct(){
        return product;
    }
    public void setProduct(Product product){
        this.product = product;
    }

    public Node getNext(){
        return next;
    }
    public void setNext(Node next){
        this.next = next;
    }

    public Node getPrevious(){
        return previous;
    }
    public void setPrevious(Node previous){
        this.previous = previous;
    }

    @Override 
    public String toString(){
        return "Node{" + "product=" + product +'}';
    }
}
