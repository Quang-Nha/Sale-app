/*
 *
 */
package com.nhale.repository;

import com.cloudinary.provisioning.Account;
import com.nhale.pojos.User;

import java.util.List;

/**
 * @author LeNha
 */
public interface UserRepository {
    User getUserById(int userId);

    /**
     * thêm {@link User} vào csdl
     */
    boolean addUser(User user);

    /**
     * lấy danh sách User theo từ tìm kiếm truyền vào
     */
    List<User> getUsers(String kw);
}
