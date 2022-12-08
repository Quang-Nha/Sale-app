/*
 *
 */
package com.nhale.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nhale.handlers.LoginSuccessHandler;
import com.nhale.handlers.LogoutSuccessHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author LeNha
 */
@Configuration
@EnableWebSecurity
@EnableTransactionManagement// cho phép tự động tạo transaction khi truy vấn dữ liệu
@ComponentScan(basePackages = {
        "com.nhale.repository",
        "com.nhale.service"
})
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    // lấy bean UserDetailsService hệ thống tự tạo lấy từ class được quét trong @ComponentScan đã implement nó
    // đối tượng xử lý đăng nhập từ form
    @Autowired
    private UserDetailsService userDetailsService;

    // bean của interface AuthenticationSuccessHandler chứa method xử lý khi đăng nhập thành công
    @Autowired
    private AuthenticationSuccessHandler loginSuccessHandler;

    // bean của interface LogoutSuccessHandler chứa method xử lý khi logout thành công
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    /**
     * bean khai báo cách băm mật khẩu, gọi bean này ở nơi cần băm mật khẩu
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * cấu hình xác thực người dùng
     * xác định đối tượng xử lý đăng nhập từ form là userDetailsService
     * xác định thuật toán băm mật khẩu bằng hàm passwordEncoder()
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // xác định đối tượng xử lý đăng nhập từ form là userDetailsService
        // xác định thuật toán băm mật khẩu bằng hàm passwordEncoder()
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * cấu hình phân quyền người dùng
     * nhận thông tin từ form đăng nhập
     * điều hướng trang khi đăng nhập thành công, thất bại hay logout
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // khai báo đường dẫn của method này sẽ xử lý việc đăng nhập
        // chỉ định method này sẽ là controller kiểu method Post,
        // nhận dữ liệu từ form đăng nhập gửi đến đường dẫn của method controller này
        // đã khai báo trong .loginPage()
        // usernameParameter() truyền vào tên param của input trong form để nhập tên tài khoản
        // usernameParameter() truyền vào tên param của input trong form để nhập mật khẩu tài khoản

        // khi truyền các thông tin xong sẽ xử lý đăng nhập tự động, lấy thông tin đăng nhập
        // và truyền vào hàm loadUserByUsername() của UserDetailsService được implements bởi class service
        // xử lý truy vấn tài khoản, hàm này so sánh với dữ liệu tài khoản trong csdl để xác thực tài khoản
        http.formLogin().loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password");

        // cấu hình nếu đăng nhập thành công thì redirect chuyển đến trang chủ "/"
        // đăng nhập thất bại thì redirect chuyển về trang cũ "/Login?error" và có thêm param error
//        http.formLogin().defaultSuccessUrl("/")
//                .failureUrl("/login?error");

        // đăng nhập thất bại thì Forward về trang có method post /loginFail
        // vì form đăng nhập gửi bằng method post nên khi forward cũng phải sang 1 controller có method post
        // do forward nên sẽ chuyển được toàn bộ giá trị của các param trong form đăng nhập đã gửi
        // nếu trang được forward sang(loginFail) lại return về trang đăng nhập thì
        // trang đăng nhập có thể lấy lại chính các param của form nó đã gửi đi và set lại các giá trị cho
        // các input trong form(lưu lại các giá trị input trong form)
        http.formLogin().failureForwardUrl("/loginFail");

        // đăng nhập thành công thì truyền vào 1 đối tượng AuthenticationSuccessHandler(đã tạo bean)
        // method onAuthenticationSuccess() của đối tượng sẽ xử lý các việc cần thiết sau khi đăng nhập
        // như lưu User đã đăng nhập vào session
        // do AuthenticationSuccessHandler là 1 interface nên cần tạo 1 class implements nó và
        // khai báo các công việc cần làm trong method của nó
        http.formLogin().successHandler(this.loginSuccessHandler);

//        // làm tương tự như đăng nhập thành công ở trên
//        http.formLogin().failureHandler(new AuthenticationFailureHandler() {
//            @Override
//            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//
//            }
//        });
//
//        // đăng nhập thất bại thì Forward về trang có method post /loginFail
//        // vì form đăng nhập gửi bằng method post nên khi forward cũng phải sang 1 controller có method post
//        // do forward nên sẽ chuyển được toàn bộ giá trị của các param trong form đăng nhập đã gửi
//        // nếu trang được forward sang(loginFail) lại return về trang đăng nhập thì
//        // trang đăng nhập có thể lấy lại chính các param của form nó đã gửi đi và set lại các giá trị cho
//        // các input trong form(lưu lại các giá trị input trong form)
//        http.formLogin().failureForwardUrl("/loginFail");
//
//        // đăng nhập thành công thì forward về trang có method post /loginSuccess
//        // vì form đăng nhập gửi bằng method post nên khi forward cũng phải sang 1 controller có method post
//        // do forward nên sẽ chuyển được toàn bộ giá trị của các param trong form đăng nhập đã gửi
//        // ở controller đó lấy param chứa tên tài khoản đã đăng nhập từ form rồi
//        // lấy đối tượng user trong csdl theo tên đó
//        // tạo 1 session chứa user này để dùng các thông tin của nó cho các hoạt động khác như set avata
//        http.formLogin().successForwardUrl("/loginSuccess");

        // cấu hình logout thành công thì chuyển về trang login
//        http.logout().logoutSuccessUrl("/login");

        // logout thành công thì truyền vào 1 đối tượng LogoutSuccessHandler(đã tạo bean)
        // method onLogoutSuccess() của đối tượng sẽ xử lý các việc cần thiết sau khi logout
        // như xóa User đã đăng nhập khỏi session
        // do LogoutSuccessHandler là 1 interface nên cần tạo 1 class implements nó và
        // khai báo các công việc cần làm trong method của nó
        http.logout().logoutSuccessHandler(this.logoutSuccessHandler);

        // nếu không có quyền truy cập thì chuyển về trang đăng nhập với param accessDenied
        // có thể sử dụng param trên để xác nhận lỗi và thông báo
        http.exceptionHandling().accessDeniedPage("/login?accessDenied");

        // phân quyền người dùng
        // những trang có antMatchers là path bắt đầu từ / thì tất cả đều truy cập được
        // những trang có antMatchers là path bắt đầu từ /admin/ thì chỉ kiểu tài khoản có role = 200
        // mới truy cập được
        http.authorizeRequests().antMatchers("/").permitAll()
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')"); // access có vẻ không hoạt động với chuỗi số
//                .antMatchers("/admin/**").hasAuthority("200");

        // security sử dụng các dịch vụ liên quan đến form để tránh người dùng chèn 1 số hoạt động vào form
        // thì bật nó lên. còn không liên quan thì tắt
        http.csrf().disable();
    }

    /**
     * bean cấu hình cho cloud Cloudinary để upload file lên đó
     */
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "lenha",
                "api_key", "539966557274424",
                "api_secret", "crcW4uR8nuXjlY6smNQ4flYOT2E",
                "secure", "true"));
    }

    /**
     * tạo bean của interface AuthenticationSuccessHandler chứa method xử lý khi đăng nhập thành công
     * trả về đối tượng của class tự tạo LoginSuccessHandler đã implements AuthenticationSuccessHandler
     */
    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    /**
     * tạo bean của interface LogoutSuccessHandler chứa method xử lý khi logout thành công
     * trả về đối tượng của class tự tạo LogoutSuccessHandlerImpl đã implements LogoutSuccessHandler
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandlerImpl();
    }
}
