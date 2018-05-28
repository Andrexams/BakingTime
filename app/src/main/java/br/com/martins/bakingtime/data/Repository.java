package br.com.martins.bakingtime.data;

import java.util.List;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public interface Repository<T> {
    void add(T item);
    List<T> query(Specification specification);
}
