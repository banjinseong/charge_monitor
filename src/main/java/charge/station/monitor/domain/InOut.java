package charge.station.monitor.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "in_out")
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

    @Builder
    public InOut(ChargeInfo chargeInfo, String carNum, String inTime) {
        this.chargeInfo = chargeInfo;
        this.carNum = carNum;
        this.inTime = inTime;
        this.outTime = "";
    }

    public void updateOutTime(String outTime) {
        this.outTime = outTime;
    }

}
