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

    //이미지 위치(서버에 저장)
    @Column(name = "img_url")
    private String imgUrl;

    //전력량
    private double power;

    //현재 차량 번호
    @Column(name = "car_num")
    private String currentNum;

    //이전 이미지 차량 번호
    @Column(name = "before_num")
    private String pastNum;

    //시간
    @Column(name = "local_date")
    private String localDate;

    @Builder
    public Monitor(String imgUrl, double power, ChargeInfo chargeInfo, String currentNum, String pastNum, String localDate){
        this.imgUrl = imgUrl;
        this.power = power;
        this.chargeInfo = chargeInfo;
        this.currentNum = currentNum;
        this.pastNum = pastNum;
        this.localDate = localDate;
    }
}
