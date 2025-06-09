package Service;

import Domain.*;

import java.sql.SQLException;
import java.util.*;



public class OrderApplication {
    OrderController oc=OrderController.getInstance();
    //automaticOrder autoO = new automaticOrder();



    public int addOrder(int numAgree , int numSupplier, String address, String date, String contactPhone) throws SQLException{
         return oc.addNewOrder(numAgree,numSupplier,address,date,contactPhone);
    }
    public boolean orderExist(int orderNumber)throws SQLException{
        return oc.existOrder(orderNumber);
    }
    public String printByAgree(int numAgreement)throws SQLException{
        return oc.printProByAgree(numAgreement);
    }
    public int numProAgreement(int numAgreement)throws SQLException{
        return oc.numProByAgree(numAgreement);
    }
    public boolean addItem(int orderNumber, int numP,int amount)throws SQLException{
        return oc.addItemOrder(orderNumber, numP, amount);
    }
    public void setStatusOrder(int orderNumber,String status)throws SQLException{
        oc.updateStatus(orderNumber,status);
    }
    public String printOrder(int orderNumber)throws SQLException{
        return oc.StringOrder(orderNumber);
    }
    public String createPeriodOrder() throws SQLException {
        return oc.addPeriodOrder();
    }

}
