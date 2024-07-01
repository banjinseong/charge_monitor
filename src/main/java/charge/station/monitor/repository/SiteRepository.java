package charge.station.monitor.repository;

import charge.station.monitor.domain.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SiteRepository extends JpaRepository<Site,Long> {
    Optional<Site> findByName(String name);
}
