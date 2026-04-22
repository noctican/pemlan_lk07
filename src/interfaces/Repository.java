package src.interfaces;

import src.exceptions.DataNotFoundException;
import src.exceptions.DuplicateDataException;
import java.util.List;

public interface Repository<T, ID> {
    void create(T data) throws DuplicateDataException;
    List<T> findAll();
    T findById(ID id);
    void update(ID id, T newData) throws DataNotFoundException;
    void delete(ID id) throws DataNotFoundException;
}