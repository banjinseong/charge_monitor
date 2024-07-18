package charge.station.monitor.repository;

import charge.station.monitor.domain.InOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InOutRepository extends JpaRepository<InOut,Long> {
    @Query(value = "SELECT out_time FROM in_out WHERE car_num = :carNum AND charge_info_id = :chargeInfoId ORDER BY in_time DESC LIMIT 1", nativeQuery = true)
    String findLatestOutTimeByCarNumAndChargeInfo(String carNum,Long chargeInfoId);

    @Query("SELECT COUNT(io) FROM InOut io WHERE io.carNum = :carNum AND io.chargeInfo.id = :chargeInfoId")
    long countByCarNumAndChargeInfo(String carNum,Long chargeInfoId);


    @Query(value = "SELECT * FROM in_out WHERE car_num = :carNum AND charge_info_id = :chargeInfoId ORDER BY in_time DESC LIMIT 1", nativeQuery = true)
    InOut findLatestByCarNumAndChargeInfo(String carNum,Long chargeInfoId);

}
