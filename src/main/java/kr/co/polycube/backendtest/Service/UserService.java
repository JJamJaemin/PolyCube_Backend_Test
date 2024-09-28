package kr.co.polycube.backendtest.Service;


import kr.co.polycube.backendtest.DTO.UserDTO;
import kr.co.polycube.backendtest.Model.User;
import kr.co.polycube.backendtest.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //유저 생성
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user = userRepository.save(user);
        userDTO.setId(user.getId());
        return userDTO;
    }
    //유저 불러오기
    public UserDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        return userDTO;
    }
    //유저 수정
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        user.setName(userDTO.getName());
        user = userRepository.save(user);
        userDTO.setId(user.getId());
        return userDTO;
    }

}
