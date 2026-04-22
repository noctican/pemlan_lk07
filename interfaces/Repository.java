package interfaces;

import java.util.List;

public interface Repository<T, ID> {
    void create(T data);
    List<T> findAll();
    T findById(ID id);
    void update(ID id, T newData);
    void delete(ID id);
}