package kr.co.polycube.backendtest.Repository;

import kr.co.polycube.backendtest.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
