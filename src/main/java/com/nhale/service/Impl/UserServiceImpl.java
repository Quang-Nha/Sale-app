/*
 *
 */
package com.nhale.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nhale.pojos.User;
import com.nhale.repository.UserRepository;
import com.nhale.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * thông báo đây là Service sẽ được hệ thống quét và tìm ra để sử dụng
 * tham số userDetailsService chính là tên thuộc tính đã khai báo ở class cấu hình bảo mật Security là
 * /@Autowired
 * private UserDetailsService userDetailsService;
 * để hệ thống tự tạo 1 bean @Autowired của class UserDetailsService là kiểu dữ liệu của class này
 * vì class này implements AccountService, còn AccountService lại extends UserDetailsService nên
 * class này cũng chính là 1 UserDetailsService
 *
 * @author LeNha
 */
@Service("userDetailsService")
public class UserServiceImpl implements UserService {

    // hệ thống tự động tạo 1 bean AccountRepository từ 1 implements của nó
    // rồi truyền vào thuộc tính này
    // implements đã khai báo annotation @Repository và
    // được khai báo quét vị trí trong class config(WebApplicationContextConfig) và đã implements WebMvcConfigurer
    @Autowired
    private UserRepository userRepository;

    // gọi bean băm mật khẩu
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;


    @Override
    public User getUserById(int userId) {
        return this.userRepository.getUserById(userId);
    }

    /**
     * upload ảnh của User lên cloud cloudinary
     * thêm User vào csdl
     */
    @Override
    public boolean addUser(User user) {
        // set lại pass cho user bằng cách gọi hàm băm mật khẩu ban đầu của nó
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        // set quyền cho tài khoản là người dùng thông thường
        user.setUserRole(User.USER);

        try {
            // thực hiện up load file bằng giá trị thuộc tính file của user
            // thuộc tính này chứa dữ liệu của file gửi từ form của client
            // method trả về 1 Map có 1 key tên secure_url chứa link của hình ảnh đã tải lên cloud
            Map dataMap = this.cloudinary.uploader().upload(user.getFile().getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));

            // set lại đường dẫn ảnh avatar của user là link ảnh đã upload lên cloud
            user.setAvatar((String) dataMap.get("secure_url"));

            return this.userRepository.addUser(user);
        } catch (IOException e) {
            System.err.println("== ADD User ==" + e.getMessage());
        }

        // nêu có lỗi xảy ra thì trả về false
        return false;
    }

    @Override
    public List<User> getUsers(String kw) {
        return this.userRepository.getUsers(kw);
    }

    /**
     * hàm này sẽ do SpringSecurity gọi do class này cũng là 1 UserDetailsService được
     * tạo 1 bean tự động và sử dụng ở class SpringSecurityConfig extends WebSecurityConfigurerAdapter
     *
     * @param username là tên user cần tìm trong csdl được security truyền vào khi nhận từ form,
     *                 để xác thực tài khoản xem có hợp lệ hay không
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // lấy ra danh sách các users có trong csdl theo key là tham số username truyền vào
        // bằng hàm getAccounts(username). thực tế kết quả chỉ có 1 hoặc 0 bản ghi hay kết quả vì
        // truy vấn theo tên mà tên là duy nhất
        List<User> users = getUsers(username);

        // nếu list rỗng tức không có kết quả user nào thì key username nhập vào không hợp lệ vì
        // không tồn tại tại trong csdl. cần ném ra ngoại lệ
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("User do not exist");
        }

        // nếu không bị ném ra ngoại lệ thì lấy ra user trong list. thực tế chỉ có 1 nên lấy ở chỉ số 0
        User user = users.get(0);

        // khai báo quyền cho user lấy được bằng SimpleGrantedAuthority() truyền quyền của user/Account cho nó
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(String.valueOf(user.getUserRole())));

        // trả về 1 User của thư viện truyền vào tên, pass, list các quyền của user/Account lấy được
        // để User tự thực hiện các thao tác chứng thực đăng nhập theo tên và pass, rồi phân quyền
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities
        );
    }
}
