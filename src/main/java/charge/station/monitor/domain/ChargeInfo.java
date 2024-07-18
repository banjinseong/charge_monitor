package charge.station.monitor.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "charge_info")
public class ChargeInfo {
    @Id
    @GeneratedValue
    @Column(name = "charge_info_id")
    private Long id;

    //충전소 위치(도/시/구)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;

    //충전소 번호(1,2,3,4....)
    @Column(name = "charge_code")
    private int code;

    @OneToMany(mappedBy = "chargeInfo")
    private List<Monitor> monitors = new ArrayList<>();

    @OneToMany(mappedBy = "chargeInfo")
    private List<InOut> inOuts = new ArrayList<>();

    @Builder
    public ChargeInfo(Site site, int code){
        this.site = site;
        this.code = code;
    }

}
