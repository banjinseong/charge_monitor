package charge.station.monitor.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ChargeInfo {
    @Id
    @GeneratedValue
    @Column(name = "charge_info_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;

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
