package charge.station.monitor.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Site {
    @Id
    @GeneratedValue
    @Column(name = "site_id")
    private Long id;

    @Column(name = "site_name")
    private String name;

    private String location;

    @OneToMany(mappedBy = "site")
    private List<ChargeInfo> chargeInfos = new ArrayList<>();
}
