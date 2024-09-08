package coms309.people;

public class Walking {
  int[] stepsInMinutes;
  int ticker = 0;
  
  public Walking(int... stepInstances) {
    stepsInMinutes = new int[1440];
    updateValues(stepInstances); 
  }

  public void updateValues(int... stepInstances) {
    for (int step: stepInstances) {
      stepsInMinutes[(ticker++) % stepsInMinutes.length] = step;
    }
  }

  public int[] getSteps() {
    return stepsInMinutes;
  }

  public void append(Walking walk) {
    updateValues(walk.getSteps());
  }
}
