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

import com.google.sps.fit.*;
import com.google.sps.util.*;
import java.util.*;

public class Milestone extends BananaNode {

  private final int level;
  private final FitnessSet fSet;

  public Milestone(int level, FitnessSet fSet) {
    this.level = level;
    this.fSet = fSet;
  }

  public HashMap<String, SupplementalMilestone> getSupplementalMilestones() {
    return null;
  }
  
  public int getLevel() {
    return level;
  }


  public String getName() {
    return fSet.getName();
  }

  public FitnessSet getFitnessSet() {
    return fSet;
  }
}
