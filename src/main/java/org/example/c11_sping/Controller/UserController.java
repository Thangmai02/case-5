package org.example.c11_sping.Controller;

import org.example.c11_sping.model.User;
import org.example.c11_sping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Chỉ định domain frontend
public class UserController {
    @Autowired
    UserRepository userRepository;

    // Đăng ký người dùng mới
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            // Kiểm tra xem tên đăng nhập đã tồn tại chưa
            Optional<User> existingUser = userRepository.findByUsn(user.getUsn());
            if (existingUser.isPresent()) {
                return new ResponseEntity<>("Tên đăng nhập đã tồn tại", HttpStatus.CONFLICT);
            }

            userRepository.save(user);
            return new ResponseEntity<>("Đăng ký thành công", HttpStatus.CREATED);
        } catch (Exception e) {
            // Log lỗi và trả về mã lỗi 500
            e.printStackTrace();
            return new ResponseEntity<>("Đã xảy ra lỗi trong quá trình đăng ký", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Đăng nhập người dùng
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            User user1 = userRepository.findByUsnAndPass(user.getUsn(), user.getPass());

            if (user1 == null) {
                return new ResponseEntity<>("Tài khoản hoặc mật khẩu sai!", HttpStatus.UNAUTHORIZED);
            }

            // Có thể trả về thông tin người dùng hoặc token tại đây nếu cần
            return new ResponseEntity<>("Đăng nhập thành công", HttpStatus.OK);
        } catch (Exception e) {
            // Log lỗi và trả về mã lỗi 500
            e.printStackTrace();
            return new ResponseEntity<>("Đã xảy ra lỗi trong quá trình đăng nhập", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Xóa người dùng theo ID
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>("Xóa người dùng thành công", HttpStatus.OK);
        } catch (Exception e) {
            // Log lỗi và trả về mã lỗi 500
            e.printStackTrace();
            return new ResponseEntity<>("Đã xảy ra lỗi trong quá trình xóa người dùng", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Tìm kiếm người dùng theo tên đăng nhập
    @GetMapping("/users/search")
    public ResponseEntity<?> searchUsers(@RequestParam String usn) {
        try {
            List<User> users = userRepository.findByUsnContaining(usn);

            if (users.isEmpty()) {
                return new ResponseEntity<>("Không tìm thấy người dùng nào!", HttpStatus.OK);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            // Log lỗi và trả về mã lỗi 500
            e.printStackTrace();
            return new ResponseEntity<>("Đã xảy ra lỗi trong quá trình tìm kiếm người dùng", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Lấy danh sách tất cả người dùng
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            // Log lỗi và trả về mã lỗi 500
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
