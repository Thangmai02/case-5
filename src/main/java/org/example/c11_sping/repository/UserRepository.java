package org.example.c11_sping.repository;

import org.example.c11_sping.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Tìm người dùng theo tên đăng nhập và mật khẩu
    User findByUsnAndPass(String usn, String pass);

    // Tìm người dùng theo tên đăng nhập (chính xác)
    Optional<User> findByUsn(String usn);

    // Tìm người dùng có tên đăng nhập chứa từ khóa (tìm kiếm)
    List<User> findByUsnContaining(String usn);
}
