package charge.station.monitor.service;

import charge.station.monitor.domain.Site;
import charge.station.monitor.dto.SiteRequestDto;
import charge.station.monitor.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SiteService {

    private final SiteRepository siteRepository;

    @Transactional
    public Long addSite(SiteRequestDto dto){
        Site site = Site.builder()
                .name(dto.getName())
                .location(dto.getLocation())
                .build();
        validDuplicateSite(site);
        siteRepository.save(site);
        return site.getId();
    }

    public void validDuplicateSite(Site site){
        siteRepository.findByName(site.getName()).ifPresent(site1 -> {
            throw new IllegalStateException("이미 존재하는 사이트입니다.");
        });
    }

    public List<Site> selectAll(){
        return siteRepository.findAll();
    }
}
