package presentation;

import Domain.*;

import java.util.LinkedList;

public class Main {
    public static void main(String [] args){
        SupplierController s = new SupplierController();
        ProductController p = new ProductController();
        AgreementsController a = new AgreementsController();
        SupplierController controller = new SupplierController();
        LinkedList<String> contactNames1 = new LinkedList<>();
        contactNames1.add("John Doe");
        LinkedList<String> deliveryDays1 = new LinkedList<>();
        deliveryDays1.add("Monday");
        s.addNewSupplier(1,"sup1","bank","Cash",contactNames1,"0505050505",deliveryDays1,"constant");
        LinkedList<String> contactNames2 = new LinkedList<>();
        contactNames1.add("dov");
        LinkedList<String> deliveryDays2 = new LinkedList<>();
        s.addNewSupplier(1,"sup2","bank","credit",contactNames2,"0505050505",deliveryDays2,"selfCollection");

        LinkedList<Agreement> agreements = new LinkedList<>();
        //Product
        p.addNewProduct("pro",1,"g","man");
        //QuantityAgreement quantityAgreement = new QuantityAgreement( 1, 100.0, 1, 10, 5);
        Agreement agreement1 = new Agreement(1,"01-01-2025");
        Agreement agreement2=new Agreement(1,"02-02-2025");
        agreements.add(agreement1);
        agreements.add(agreement2);

        UserPresentation up=new UserPresentation();
        up.UserPresentationInit();

    }

}
//לבדוק הכנסה בהסכם של מוצר בסיס שקיים