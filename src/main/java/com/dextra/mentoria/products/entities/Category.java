package com.dextra.mentoria.products.entities;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "tb_category")
public class Category implements Serializable {
    @Serial
    private static final long serialVersionUID = -2901349284703177665L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(updatable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant createdAt;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant updatedAt;

    public Category() {
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Category setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public Instant getCreatedAt() {

        return createdAt;
    }

    public Category setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    public void PrePersist() {
        createdAt = Instant.now();
    }

    @PreUpdate
    public void PreUpdate() {
        updatedAt = Instant.now();
    }
}
