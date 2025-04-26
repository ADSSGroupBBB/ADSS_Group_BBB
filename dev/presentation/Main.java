package presentation;

import Domain.*;

import java.util.LinkedList;

public class Main {
    public static void main(String [] args){
        LinkedList<String> contactNames = new LinkedList<>();
        contactNames.add("John Doe");

        LinkedList<Days> deliveryDays = new LinkedList<>();
        deliveryDays.add(Days.Monday);

        LinkedList<Agreement> agreements = new LinkedList<>();
        Product product = new Product("pro",1,unit.g,"man");
        QuantityAgreement quantityAgreement = new QuantityAgreement( 1, product, 100.0, 1, 10, 5);
        Agreement agreement1 = new Agreement(1,"01-01-2025");
        Agreement agreement2=new Agreement(1,"02-02-2025");
        agreements.add(agreement1);
        agreements.add(agreement2);
        Supplier supplier = new Supplier(1,"sup","bank",paymentTerms.Cash,contactNames,"0505050505"
                ,deliveryDays,Delivery.invitation,agreements);
        UserPresentation up=new UserPresentation();
        up.UserPresentationInit();

    }

}
//לבדוק הכנסה בהסכם של מוצר בסיס שקיים