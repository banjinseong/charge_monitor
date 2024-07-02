package charge.station.monitor.dto;

import charge.station.monitor.repository.SiteRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChargeInfoRequestDto {

    private SiteRepository siteRepository;

    private Long siteId;

    private int code;

}
