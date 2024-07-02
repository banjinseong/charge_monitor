package charge.station.monitor.repository;


import charge.station.monitor.domain.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor,Long> {
}
