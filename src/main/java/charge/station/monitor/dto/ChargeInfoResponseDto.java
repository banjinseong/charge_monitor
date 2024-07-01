package charge.station.monitor.dto;

import charge.station.monitor.domain.ChargeInfo;
import charge.station.monitor.domain.Site;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeInfoResponseDto {
    //상태
    private String message;
    //데이터
    private List<ChargeInfo> data;
}
