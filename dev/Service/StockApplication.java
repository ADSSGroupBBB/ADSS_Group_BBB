package Service;

import Domain.ProductRepositoryImpl;
import Domain.Stock;
import Domain.PairInt;

import java.util.Map;
import java.util.Objects;

public class StockApplication {
    Stock st = Stock.getInstance();
    PairInt pi;
    public Stock getSt() {
        return st;
    }

    public PairInt getPi() {
        return pi;
    }

    public void selling(Map<Integer,Integer> soldPro){
        st.sell(soldPro);
    }
}
