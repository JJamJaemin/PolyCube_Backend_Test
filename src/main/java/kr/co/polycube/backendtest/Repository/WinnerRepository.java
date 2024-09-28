package kr.co.polycube.backendtest.Repository;

import kr.co.polycube.backendtest.Model.Winner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WinnerRepository extends JpaRepository<Winner, Long> {
}