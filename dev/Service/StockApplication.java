package Service;

import Domain.StockController;
import Domain.PairInt;

import java.sql.SQLException;
import java.util.Map;

public class StockApplication {

    private StockController st;
    private PairInt pi;

    public StockApplication() throws SQLException{
        try {
            st = StockController.getInstance();
        } catch (SQLException e) {
            throw e;
        }
    }

    public int getAmountByPid(int numP) {
        return st.getCurrentAmount(numP);
    }

    public void selling(Map<Integer,Integer> soldPro){
        st.sell(soldPro);
    }

    // public void upStock(int numId){
    //     st.updateStock(numId);
    // }
}

