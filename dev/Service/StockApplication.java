package Service;

import Domain.Stock;
import Domain.PairInt;
public class StockApplication {
    Stock st = Stock.getInstance();
    PairInt pi;
    public Stock getSt() {
        return st;
    }

    public PairInt getPi() {
        return pi;
    }
}
