package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.NamedNativeQuery;
import java.util.List;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@NamedNativeQuery(name = "suggestProducts", resultClass = Product.class, query = "SELECT * FROM Product ORDER BY embedded <=> cast(:vector as vector) DESC LIMIT 5;")
public class Product extends PanacheEntity {

    public String name;
    public String description;
    public double price;

    @Column
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 384)
    public float[] embedded;

    public List<Product> suggestProducts(float[] vector) {
        return  getEntityManager().createNamedQuery("suggestProducts", Product.class).setParameter("vector", vector).getResultList();
    }
}
