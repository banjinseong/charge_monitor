package charge.station.monitor.dto.member;

import charge.station.monitor.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * 인증 인가에 사용될 유저 정보dto
 */
public class CustomUserInfoDto {

    private Long id;

    private String email;

    private String password;

    private UserRole userRole;

}
