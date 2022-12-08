/*
 *
 */
package com.nhale.service;

import com.nhale.pojos.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * @author LeNha
 */
public interface UserService extends UserDetailsService {
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
