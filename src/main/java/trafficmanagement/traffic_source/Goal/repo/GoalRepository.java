package trafficmanagement.traffic_source.Goal.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import trafficmanagement.traffic_source.Goal.model.Goal;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Integer> {}
