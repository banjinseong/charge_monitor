package charge.station.monitor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitorRequestDto {

    //사이트정보
    private Long siteId;
    //코드정보
    private int code;
    //이미지
    private MultipartFile img;
    //전력량
    private double power;
    //이미지 경로
    private String imgUrl;

}
