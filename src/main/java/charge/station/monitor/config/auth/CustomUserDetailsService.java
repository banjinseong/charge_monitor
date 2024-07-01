package charge.station.monitor.config.auth;


import charge.station.monitor.domain.Member;
import charge.station.monitor.dto.member.CustomUserInfoDto;
import charge.station.monitor.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override//로그인시 로그인 체크
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.findById
                (Long.parseLong(id)).orElseThrow(() -> new UsernameNotFoundException("없는 회원 입니다..."));

        CustomUserInfoDto dto = new CustomUserInfoDto(member.getId(),
                member.getEmail(), member.getPassword(), member.getUserRole());
        return new PrincipalDetails(dto);
    }
}
