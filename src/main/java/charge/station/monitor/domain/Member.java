package charge.station.monitor.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public void encodePassword(String password){
        this.password = password;
    }

    @Builder
    public Member(String email, String password){
        this.email = email;
        this.password = password;
        this.userRole = UserRole.ROLE_USER;
    }
}
