package charge.station.monitor.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Monitor {
    @Id
    @GeneratedValue
    @Column(name = "monitor_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_info_id")
    private ChargeInfo chargeInfo;

    @Column(name = "img_url")
    private String imgUrl;

    private double power;

    //시간
    private String localDate;

    @Builder
    public Monitor(String imgUrl, double power, ChargeInfo chargeInfo){
        this.imgUrl = imgUrl;
        this.power = power;
        this.chargeInfo = chargeInfo;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
        localDate = now.format(formatter);
    }
}
