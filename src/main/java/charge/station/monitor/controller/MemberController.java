package charge.station.monitor.controller;

import charge.station.monitor.dto.member.LoginRequestDto;
import charge.station.monitor.dto.member.MemberRequestDto;
import charge.station.monitor.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("member/*")
public class MemberController {

    private final MemberService memberService;



    /**
     * 회원가입 유저의 유저 번호를 반환함.
     */
    @PostMapping("join")
    public ResponseEntity<Long> joinMember(@Valid @RequestBody MemberRequestDto memberRequestDto){
        Long id = memberService.join(memberRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    /**
     * 현재 토큰을 반환중인데, 나중에 수정 필요할수도
     */
    @PostMapping("login")
    public ResponseEntity<String> loginMember(@Valid @RequestBody LoginRequestDto loginRequestDto){
        String token = memberService.login(loginRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }



}
