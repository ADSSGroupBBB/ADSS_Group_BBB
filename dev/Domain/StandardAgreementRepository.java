package Domain;

import dto.AgreementDto;
import dto.SupplierDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface   StandardAgreementRepository {
    AgreementDto saveStandardAgreement (int supplierNumber,String date);
    void addProStandardAgreement(int id,int numPro, double price,int catalogNumber,int amountToDiscount,int discount);
    void deleteProductStandardAgree(int numAgree,int productNumber);
    void setDateStandardAgreement(int numAgreement,String date);
    void deleteStandardAgreement(int numSup,int numAgree);
    Optional<AgreementDto> getStandardAgreement(int numA );
    void setNumCatalogStandard(int numAgree,int productNumber,int catalogNumber);
    void setPriceStandardAgreement(int numAgree,int productNumber ,double price);
    void setAmountToDiscountStandardAgreement(int productNumber,int numAgree,int amountToDiscount);
    void setDiscountStandardAgreement(int productNumber,int numAgree,int discount);
    List<Optional<AgreementDto>> allAgreementsByStandardSup(int numS );
    boolean existProStandardAgreementByName(String nameP,int numA);
    QuantityAgreement productFromAgreementByName(int numAgree,String nameP);
    Map<Integer, Agreement> getAllStandardAgreements();
    }
