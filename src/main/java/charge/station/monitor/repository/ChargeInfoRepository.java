package charge.station.monitor.repository;

import charge.station.monitor.domain.ChargeInfo;
import charge.station.monitor.domain.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChargeInfoRepository extends JpaRepository<ChargeInfo,Long> {
    Optional<ChargeInfo> findByCodeAndSite(int code, Site site);
}
