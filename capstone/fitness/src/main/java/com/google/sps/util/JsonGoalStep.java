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
import com.google.sps.fit.Exercise.SetType;
import com.google.sps.util.*;
import java.util.*;

/* Json version of progress marker. */
public class JsonGoalStep {

  private final String name;
  private final boolean complete;
  private final HashMap<SetType, float[]> setValues;

  public JsonGoalStep(GoalStep gs) {
    this(gs.getName(), gs.isComplete(), gs.getMarker().getSetValues());
  }

  public JsonGoalStep(String name, boolean complete, HashMap<SetType, float[]> setValues) {
    this.name = name;
    this.complete = complete;
    this.setValues = setValues;
  }

  public String getName() {
    return name;
  }

  public boolean isComplete() {
    return complete;
  }

  public HashMap<SetType, float[]> getSetValues() {
    return setValues;
  }
}