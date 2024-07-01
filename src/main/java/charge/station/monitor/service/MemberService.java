package charge.station.monitor.service;

import charge.station.monitor.config.jwt.JwtUtil;
import charge.station.monitor.domain.Member;
import charge.station.monitor.dto.member.CustomUserInfoDto;
import charge.station.monitor.dto.member.LoginRequestDto;
import charge.station.monitor.dto.member.MemberRequestDto;
import charge.station.monitor.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 보강 수정 해야됨.
     */
    @Transactional
    public Long join(MemberRequestDto memberRequestDto){
        Member member = memberRequestDto.toEntity();
        validateDuplicateMember(member);
        member.encodePassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member){
        memberRepository.findByEmail(member.getEmail()).ifPresent(member1 -> {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        });
    }

    public String login(LoginRequestDto loginRequestDto){
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();
        Member member = memberRepository.findByEmail(email).orElse(null);;
        if(member==null){
            throw new UsernameNotFoundException("이메일이 존재하지 않습니다.");
        }
        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        CustomUserInfoDto info = new CustomUserInfoDto(member.getId()
                ,member.getEmail(), member.getPassword(), member.getUserRole());

        return jwtUtil.createAccessToken(info);
    }

}
