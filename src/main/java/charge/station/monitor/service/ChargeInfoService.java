package charge.station.monitor.service;

import charge.station.monitor.domain.ChargeInfo;
import charge.station.monitor.domain.Site;
import charge.station.monitor.dto.ChargeInfoRequestDto;
import charge.station.monitor.repository.ChargeInfoRepository;
import charge.station.monitor.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChargeInfoService {

    private final ChargeInfoRepository chargeInfoRepository;
    private final SiteRepository siteRepository;

    @Transactional
    public Long addChargeInfo(ChargeInfoRequestDto dto){
        Site site = siteRepository.findById(dto.getSiteId()).get();
        ChargeInfo chargeInfo = ChargeInfo.builder()
                                        .site(site)
                                        .code(dto.getCode())
                                        .build();
        validDuplicateSite(chargeInfo);
        chargeInfoRepository.save(chargeInfo);
        return chargeInfo.getId();
    }

    public void validDuplicateSite(ChargeInfo chargeInfo){
        chargeInfoRepository.findByCodeAndSite(chargeInfo.getCode(), chargeInfo.getSite()).ifPresent(site1 -> {
            throw new IllegalStateException("이미 존재하는 충전소입니다.");
        });
    }

    public List<ChargeInfo> selectAll(){
        return chargeInfoRepository.findAll();
    }
}
