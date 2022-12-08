package com.nhale.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User implements Serializable {
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";
    private static final long serialVersionUID = -4249304720096514780L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Column(name = "phone", length = 45)
    private String phone;

    @Column(name = "username", nullable = false, length = 45, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 200)
    @JsonIgnore
    private String password;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "user_role", nullable = false, length = 10)
    private String userRole;

    @Column(name = "avatar", nullable = false, length = 150)
    private String avatar;

    // thuộc tính chứa dữ liệu file nhận được từ client
    @Transient// annotation xác nhận thuộc tính này không liên kết với trường nào trong csdl
    @JsonIgnore
    private MultipartFile file;

    @Transient
    @JsonIgnore
    private String confirmPassword;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Set<SaleOrder> saleOrders = new LinkedHashSet<>();

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Set<Comment> comments = new LinkedHashSet<>();

    public MultipartFile getFile() {
        return file;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<SaleOrder> getSaleOrders() {
        return saleOrders;
    }

    public void setSaleOrders(Set<SaleOrder> saleOrders) {
        this.saleOrders = saleOrders;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

}