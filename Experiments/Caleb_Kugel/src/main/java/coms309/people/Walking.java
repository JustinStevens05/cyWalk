package coms309.people;

import java.util.Arrays;

public class Walking {
  int[] steps;
  int ticker = 0;

  public Walking() {
    steps = new int[5];
  }
  
  public Walking(int[] steps) {
    this.steps = new int[5]; // change to 1440
    updateValues(steps); 
  }

  public void updateValues(int[] stepInstances) {
    for (int step: stepInstances) {
      steps[(ticker++) % steps.length] = step;
    }
  }

  /*
  public int[] getStepsInMinutes() {
    return stepsInMinutes;
  }
  */

  public void append(Walking walk) {
    updateValues(walk.getSteps());
  }

  public void setSteps(int[] steps) {
    this.steps = steps;
  } 

  public int[] getSteps() {
    return steps;
  }

  @Override
  public String toString() {
    return Arrays.toString(steps);
  }
}
