// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.progress;

import com.google.sps.util.*;
import java.util.Arrays;
import java.util.Random; 

/*


In order of priority
- Create logic to build model



*/

public class Progress {

  private ProgressModel buildModel(Data data) {
    ProgressModel model = buildMainMilestones(data);
    if(model != null) {
      model = buildSupplementalMilestones(model);
    }
    return model;
  }
  
  private ProgressModel buildMainMilestones(Data data) {
    // Logic to build linear progression
    // TODO
    // getCahngesCount then get values change by and build based off that
    return null;
  }
  
  private int getChangesCount(int daysAvailable) {}

  private int[] getValuesChangeBy(int changesCount) {} 

  private FitnessSet createFitnessSet(FitnessSet fs, FitnessSet goal, float setValuesChangeBy) {
    String name = goal.name;
    int sets = fs.sets;
    String setType1 = goal.setType1;
    String setType2 = goal.setType2;
    float[] setType1Values = null;
    float[] setType2Values = null;

    Random rand = new Random(); 
    boolean randomFinished = false;
    while(!randomFinished) {
      int increment = rand.nextInt(4);
      switch(increment) {
        case 0: // Fall Through
        case 1:
          // increase sets
          if(fs.getSets() < goal.getSets()) {
            sets++;
            setType1Values = copyAndAddValue(fs.getSetTypeValues(true));
            setType2Values = copyAndAddValue(fs.getSetTypeValues(false));
            randomFinished = true;
            break;
          }
        case 2:
          // increase set1
          if(Arrays.equals(fs.getSetTypeValues(true), goal.getSetTypeValues(true))) {
            setType1Values = incrementSet(fs.getSetTypeValues(true), setValuesChangeBy);
            setType2Values = cloneArray(fs.getSetTypeValues(false));
            randomFinished = true;
            break;
          }
        case 3:
          // increse set2
          if(setType2 != null && Arrays.equals(fs.getSetTypeValues(false), goal.getSetTypeValues(false))) {
            setType1Values = cloneArray(fs.getSetTypeValues(true));
            setType2Values = incrementSet(fs.getSetTypeValues(false), setValuesChangeBy);
            randomFinished = true;
            break;
          }
        default:
          break;
      }
    }
    return new FitnessSet(name, sets, setType1, setType2, setType1Values, setType2Values);
  }

  private float[] cloneArray(float[] arr) {
    return arr == null ? null : arr.clone();
  } 

  private float[] incrementSet(float[] setValues, float setValuesChangeBy) {
    float[] copy = Arrays.sort(setValues.clone(), Collections.reverseOrder());
    if(copy[0] == copy[copy.length - 1]) {
      copy[0] += setValuesChangeBy;
    }
    else {
      int i = 1;
      while(copy[0] == copy[i]) {
        i++;
      }
      copy[i] += setValuesChangeBy;
    }
    return copy;
  }
  
  private float[] copyAndAddValue(float[] setValues) {
    float[] copy = copyOf(setValues, setValues.length + 1);
    copy[copy.length - 1] = copy[copy.length - 2];
    return copy;
  }

  private ProgressModel buildSupplementalMilestones(ProgressModel model) {
    // Logic to add static fitness sets
    // TODO
    return model;
  }

  private ProgressModel updateProgressModel(Data data, ProgressModel model) {
    Milestone milestone = model.getCurrentMainMilestone();
    if(milestone == null) {
      return null; // Might be better to throw an error
    }
    HashMap<String, SupplementalMilestone> supplementalMilestoneSets = milestone.getSupplementalMilestones();
    FitnessSet[] sessionSets = data.lastSession().getSets();
    
    for(FitnessSet sessionSet : sessionSets) {
      if(supplementalMilestoneSets != null && supplementalMilestoneSets.contains(sessionSet.getName())) {
        model.progressSupplementalMilestone(sessionSet);
      }
      else if(milestone.getName().equals(sessionSet.getName())) {
        boolean progressed = model.progressMainMilestone(sessionSet);
        model = progressed ? buildSupplementalMilestones(model) : model;
      }
    }

    return model;
  }

  public ProgressModel getUpdatedProgressModel(Data data) {
    ProgressModel model = data.getProgressModel(); 
    if(model == null) {
      return buildModel(data);
    }
    return updateProgressModel(data, model);
  }

//---------------------------------------------------------------------------------------
// Alternative implementation which makes it so you dont have to store progress model (theorectically); instead you store milestone only

  // Method for considered alternatives
  private Milestone buildSupplementalMilestones(Milestone milestone) {
    // Logic to add static fitness sets
    // TODO
    // NOTE: Deciding whether node structure is possible for saving data
    return milestone;
  }

  // Method for considered alternatives
  private Milestone updateMilestones(Data data, Milestone milestone) {
    HashMap<String, SupplementalMilestone> supplementalMilestoneSets = milestone.getSupplementalMilestones();
    FitnessSet[] sessionSets = data.lastSession().getSets();
    
    for(FitnessSet sessionSet : sessionSets) {
      if(supplementalMilestoneSets != null && supplementalMilestoneSets.contains(sessionSet.getName())) {
        milestone.progressSupplementalMilestone(sessionSet);
      }
      else if(milestone.getName().equals(sessionSet.getName())) {
        int prevLevel = milestone.getLevel();
        milestone = milestone.progressMainMilestone(sessionSet);
        milestone = milestone.getLevel() > prevLevel ? buildSupplementalMilestones(milestone) : milestone;
      }
    }

    return milestone;
  }
  
  // Method for considered alternatives
  public Milestone getUpdatedMilestones(Data data) {
    Milestone milestone = data.getCurrentMainMilestone(); 
    if(milestone == null) {
      ProgressModel model = buildModel(data);
      return model.getCurrentMainMilestone();
    }
    return updateMilestones(data, milestone);
  }

}
