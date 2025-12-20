package cmc_demoproject.posts.Posts

import cmc_demoproject.posts.user.dto.RegistMemberDTO
import cmc_demoproject.posts.user.entity.Users
import cmc_demoproject.posts.user.repository.UsersRepository
import cmc_demoproject.posts.user.service.UserServiceImpl
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification
import spock.lang.Subject

class UserServiceImplSpec extends Specification {

    // Mock 생성
    def usersRepository = Mock(UsersRepository)
    def passwordEncoder = Mock(PasswordEncoder)

    @Subject
    def userService = new UserServiceImpl(usersRepository, passwordEncoder)

    def "회원가입 성공 - 새로운 이메일로 가입할 때"() {
        given: "회원가입 정보 DTO 생성"
        def dto = new RegistMemberDTO(email: "test@test.com", password: "1234", userName: "테스터")

        and: "중복 이메일이 없음을 가정"
        usersRepository.findByEmail(dto.email) >> Optional.empty()
        passwordEncoder.encode(dto.password) >> "encoded_password"

        when: "회원가입 로직 실행"
        userService.register(dto)

        then: "Repository의 save 메서드가 한 번 호출되어야 함"
        1 * usersRepository.save({ user ->
            user.email == "test@test.com" &&
                    user.password == "encoded_password" &&
                    user.role == "USER"
        })
    }

    def "회원가입 실패 - 이미 존재하는 이메일일 때 예외 발생"() {
        given: "중복된 이메일 정보"
        def dto = new RegistMemberDTO(email: "duplicate@test.com")
        usersRepository.findByEmail(dto.email) >> Optional.of(new Users())

        when: "회원가입 실행"
        userService.register(dto)

        then: "IllegalArgumentException 예외 발생"
        def e = thrown(IllegalArgumentException)
        e.message == "이미 사용중인 이메일입니다."

        and: "save 메서드는 호출되지 않아야 함"
        0 * usersRepository.save(_)
    }
}