package charge.station.monitor.dto;

import charge.station.monitor.domain.Site;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteResponseDto {

    //상태
    private String message;
    //데이터
    private List<Site> data;
}
