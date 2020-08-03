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

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import com.google.gson.Gson;
import java.util.Date;

@WebServlet("/calendar-creation")
public class CreatePlanServlet extends HttpServlet {
  Gson gson = new Gson();

  // hardcoded values for user input for dev purposes. 
  // TODO (piercedw@) : Integrate with Gabriel's authentication feature and fetch user data from datastore.
  // ex. private final static String username = entity.getproperty, etc...

  private final static String email = "norville_rogers@example.com"; 
  private final static String nickname = "Shaggy";
  private final static int age = 19;
  private final static int wks = 14; 
  private final static double goal = 26.2;
  private final static double mileTime = 11.0; 
  
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
    response.setContentType("text/html;");
    
    TrainingSched training = new TrainingSched(email);
    training.createPlan(age, wks, goal, mileTime);
    response.getWriter().println(gson.toJson(training.getWorkouts()));
  }
}