package charge.station.monitor.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class InOut {
    @Id
    @GeneratedValue
    @Column(name = "in_out_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_info_id")
    private ChargeInfo chargeInfo;

    @Column(name = "car_num")
    private String carNum;

    @Column(name = "in_time")
    private String inTime;

    @Column(name = "out_time")
    private String outTime;

}
