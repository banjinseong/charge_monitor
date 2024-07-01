package charge.station.monitor.repository;

import charge.station.monitor.domain.ChargeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeInfoRepository extends JpaRepository<ChargeInfo,Long> {

}
