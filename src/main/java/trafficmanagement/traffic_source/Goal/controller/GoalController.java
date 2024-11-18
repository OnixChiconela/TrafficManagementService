package trafficmanagement.traffic_source.Goal.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import trafficmanagement.traffic_source.Goal.model.Goal;
import trafficmanagement.traffic_source.Goal.service.GoalService;

@RestController
@RequestMapping("/api/goals")
public class GoalController {
    
    private GoalService goalService;
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping("/new-goal")
    public Goal creategoal(@RequestBody Goal goal) {
        try {
            return this.goalService.createGoal(goal.getName(),  goal.getDescription(), goal.getType(), goal.getTargetValue());
        } catch(Exception error) {
            System.out.println("Goal was not created!!!: " + error);
            return null;
        }
    }

    @PostMapping("/{id}/track") 
    public void trackGoalProgress(@PathVariable Integer id, @RequestBody long value) {
        try {   
            this.goalService.trackGoalProgress(id, value);
        } catch (Exception error) {
            System.out.println("Cannot track goal progress: " + error);
            return;
        }
    }

    @PostMapping("/{id}/conversions") 
    public void trackGoalConversion(@PathVariable Integer id) {
        try {   
            this.goalService.trackGoalConversion(id);
        } catch (Exception error) {
            System.out.println("Cannot track goal progress: " + error);
            return;
        }
    }
    

    @GetMapping() 
    public List<Goal> getAllGoals() {
        try {
            List<Goal> goals = this.goalService.getAllGoal();
            System.out.println("goals: " + goals);
            return goals;
        } catch (Exception error) {
            System.out.println("Cannot get goals: " + error);
            return null;
        }
    }

    @GetMapping("/{id}") 
    public Goal getGoal(@PathVariable int id) {
        try {
            Goal goals = this.goalService.getGoal(id);
            return goals;
        } catch (Exception error) {
            System.out.println("Cannot get goal: " + error);
            return null;
        }
    }

    @PutMapping("/{id}")
    public Goal update(@PathVariable int id, @RequestBody Goal goal) {
        Goal goall = this.goalService.update(id);
        System.out.println("from controller: " + goall);
        return goall;
    }

    @DeleteMapping("/{id}")
    public void deleteGoal(@PathVariable Integer id) {
        try {
            this.goalService.deleteGoal(id);
        } catch (Exception error) {
            return;
        }
    }
}
