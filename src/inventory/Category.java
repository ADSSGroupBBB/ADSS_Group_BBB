package inventory;

public class Category {
    private String name;
    private  Category parent;//מאפשר קטגוריה בתוך קטגוריה


    public  Category(String name, Category parent){
        this.name=name;
        this.parent=parent;
    }

    public String getFullPath(){
        if (parent == null){
            return name;
        }
        else {
            return parent.getFullPath()+ " > " + name;
        }
    }
}
