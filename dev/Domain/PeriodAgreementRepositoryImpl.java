package Domain;

import dto.PeriodAgreementDto;

import java.util.LinkedList;

public class PeriodAgreementRepositoryImpl implements PeriodAgreementRepository{
    PeriodAgreementDto savePeriodAgreement (int supplierNumber, String date) {
        SupplierController s = SupplierController.getInstance();
        LinkedList<Days> days = s.getDays(supplierNumber);
        PeriodAgreement agree = new PeriodAgreement(supplierNumber, date, days, address, contactPhone);
        s.addAgreement(supplierNumber, agree);
        allPeriodAgreements.put(agree.getIDNumber(), agree);
    }
}
