package com.nhale.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product implements Serializable, Comparable<Product> {
    private static final long serialVersionUID = 3935632643327695030L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description")
    private String description;

    // xác thực độ lớn giá trị của trường
    @Min(value = 1, message = "{product.price.minErr}")
    @Max(value = 10000000, message = "{product.price.maxErr}")
    @Column(name = "price", precision = 10)
    private BigDecimal price;

    @Column(name = "image", length = 100)
    private String image;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private Set<ProMan> promen = new LinkedHashSet<>();

    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();

    // thuộc tính chứa dữ liệu file nhận được từ client
    @NotNull(message = "{product.file.nullErr}")
    @Transient// annotation xác nhận thuộc tính này không liên kết với trường nào trong csdl
    @JsonIgnore
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Set<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Set<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<ProMan> getPromen() {
        return promen;
    }

    public void setPromen(Set<ProMan> promen) {
        this.promen = promen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int compareTo(Product o) {
        return this.id.compareTo(o.id);
    }
}