package Domain;

import dto.AgreementDto;
import dto.ProductDto;

public interface StandardAgreementRepository {
    AgreementDto saveStandardAgreement (int supplierNumber,String date);
    void addProStandardAgreement(int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount);
     void deleteProductStandardAgree(int numAgree,int productNumber);
     void setDateStandardAgreement(int numAgreement,String date);
     void setAddressPeriod(int numAgreement,String address);
     void setContactPhonePeriod(int numAgreement,String ContactPhone);



    }
