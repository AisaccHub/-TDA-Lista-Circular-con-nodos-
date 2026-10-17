package back_end;

public class Product {
    
    private int id;
    private float price;
    private String name;
    private String description;

    public Product (int id, float price, String name, String description){
        this.id = id;
        this.price = price;
        this.name = name;
        this.description = description;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public float getPrice(){
        return price;
    }
    public void setPrice(float price){
        this.price = price;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    @Override 
    public String toString(){
        return "Product{" + "id=" + id +", price=" + price +", name='" + name + '\'' +", description='" + description + '\'' +'}';
    }
}
