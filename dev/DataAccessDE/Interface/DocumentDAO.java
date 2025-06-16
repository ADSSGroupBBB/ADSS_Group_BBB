package DataAccessDE.Interface;
import dto.DocumentDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DocumentDAO {
    Optional<DocumentDTO> findById(String id) throws SQLException;
    List<DocumentDTO> findAll() throws SQLException;
    DocumentDTO save(DocumentDTO document) throws SQLException;
    int getNextId() throws SQLException;
    int getId(String document_id) throws SQLException;
}