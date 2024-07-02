package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.NamedNativeQuery;
import java.util.List;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@NamedNativeQuery(name = "suggestProducts", resultClass = Product.class, query = "SELECT * FROM Product ORDER BY embedded <-> cast(:vector as vector) LIMIT 5;")
public class Product extends PanacheEntity {

    @Column(length = 512)
    public String name;

    @Column(length = 65_535)
    @JsonbTransient
    public String description;
    public double price;

    @Column
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 384)
    @JsonbTransient
    public float[] embedded;

    public Product(){

    }

    public Product(String name, String description, double price, float[] embedded) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.embedded = embedded;
    }

    public static List<Product> suggestProducts(float[] vector) {
        return  getEntityManager().createNamedQuery("suggestProducts", Product.class).setParameter("vector", vector).getResultList();
    }
}
