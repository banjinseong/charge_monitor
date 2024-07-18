package charge.station.monitor.repository;


import charge.station.monitor.domain.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor,Long> {
    @Query(value = "SELECT * FROM monitor WHERE charge_info_id = :chargeInfoId ORDER BY local_date DESC LIMIT 1", nativeQuery = true)
    Monitor findLatestByChargeInfoId(Long chargeInfoId);
}
