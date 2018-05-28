package br.com.martins.bakingtime.data;

/**
 * Created by Andre Martins dos Santos on 27/05/2018.
 */
public class ByIdSpecification implements Specification {
    private Long id;
    public ByIdSpecification(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
}
