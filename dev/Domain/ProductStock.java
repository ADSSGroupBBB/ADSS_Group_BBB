package Domain;

public class ProductStock {
    private int numProduct;
    private int currentAmount;
    private int minimumCount;
    private boolean beOrdered;

    public int getNumProduct() {
        return numProduct;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void addCurrentAmount(int currentAmount) {
        this.currentAmount = this.currentAmount+currentAmount;
        if(this.currentAmount>this.minimumCount) {
            setBeOrdered(false);
        }
    }
    public void lessCurrentAmount(int currentAmount) {
        this.currentAmount = Math.max(this.currentAmount-currentAmount,0);
        }

    public void setBeOrdered(boolean beOrdered) {
        this.beOrdered = beOrdered;
    }

    public int getMinimumCount() {
        return minimumCount;
    }

    public boolean isBeOrdered() {
        return beOrdered;
    }

    public ProductStock(int numProduct, int currentAmount, int minimumCount){
        this.numProduct=numProduct;
        this.currentAmount=currentAmount;
        this.minimumCount=minimumCount;
        this.beOrdered=false;
    }
}
