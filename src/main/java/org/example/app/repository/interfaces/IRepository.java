package org.example.app.repository.interfaces;

import org.example.app.entity.BaseEntity;
import org.example.app.utils.ActionAnswer;


public interface IRepository<T extends BaseEntity> {
    ActionAnswer<T> create(T obj);
    ActionAnswer<T> readAll(int limit, int offset);
    ActionAnswer<T> update(T obj);
    ActionAnswer<T> delete(Long id);
    ActionAnswer<T> readById(Long id);
}
