package trafficmanagement.traffic_source.Goal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import trafficmanagement.traffic_source.Goal.model.Goal;
import trafficmanagement.traffic_source.Goal.repo.GoalRepository;

@Service
public class GoalService {
    
    private GoalRepository goalRepository;
    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public Goal createGoal(String name, String description, String type, long targetValue) {
        try {
            Goal goal = new Goal(name, description, type, targetValue);
            this.goalRepository.save(goal);
            return goal;
        } catch (Exception error) {
            return null;
        }
    }

    public Goal getGoal(int id) {
        try {
            Goal goal = this.goalRepository.findById(id).get();
            return goal;
        } catch (Exception error) {
            return null;
        }
    }

    public Goal update(int id) {
        Goal goal = this.getGoal(id);

        goal.setName(goal.getName());
        goal.setDescription(goal.getDescription());
        goal.setType(goal.getType());
        goal.setTargetValue(goal.getTargetValue());
        this.goalRepository.save(goal);
        System.out.println("from sevice: " + goal);
        return goal;
    }

    public List<Goal> getAllGoal() {
        try {
            List<Goal> goals = this.goalRepository.findAll();
            return goals;
        } catch (Exception error) {
            return null;
        }
    }

    public void trackGoalProgress(Integer goalId, long value) {
        try {
            Goal goal = goalRepository.findById(goalId).orElse(null);
            if(goal != null) {
                goal.incrementCurrentValue(value);
                goalRepository.save(goal);
            }
        } catch (Exception error) {
            System.out.println("error occured whiletrack goal");
        }
    }

    public void trackGoalConversion(Integer goalId) {
        try {
            Goal goal = goalRepository.findById(goalId).orElse(null);
            if(goal != null) {
                goal.incrementConversions();
                goalRepository.save(goal);
            }
        } catch (Exception error) {

        }
    }

    public void deleteGoal(Integer id) {
        try {
            this.goalRepository.deleteById(id);
        } catch (Exception error) {
            return;
        }
    }
}
