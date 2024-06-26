package charge.station.monitor.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PowerImage {
    @Id
    @GeneratedValue
    @Column(name = "power_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_info_id")
    private ChargeInfo chargeInfo;

    private String time;

    @Column(name = "img_url")
    private String imgUrl;

    private double power;
}
