package Domain;

import dto.SupplierDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface SupplierRepository {
    SupplierDto addSupplier(Supplier sup);
    Optional<SupplierDto> getSupplier(int id) ;
    void update(Supplier sup);
    void remove(Supplier sup);

}
