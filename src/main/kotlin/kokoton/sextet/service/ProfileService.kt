package kokoton.sextet.service
import kokoton.sextet.model.Profile
import kokoton.sextet.model.ProfileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProfileService(
    @Autowired private val profileRepository: ProfileRepository
) {

    // XP를 증가시키는 메서드
    fun increaseXp(user: Profile, score: Int) {
        // 사용자의 기존 xp에 score만큼 추가
        user.xp += score

        // XP 업데이트를 반영하기 위해 저장
        profileRepository.save(user)
    }

    // 다른 기능들도 추가 가능: 포인트 업데이트 등
}