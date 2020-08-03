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

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.google.gson.Gson;
import java.util.Date;

// servlet that sends the date to calendar.html. 
@WebServlet("/date")
public class SendDateServlet extends HttpServlet {
  Gson gson = new Gson();
  Date date = new Date();

  // hardcoded values for user input for dev purposes. 
  // TODO (piercedw@) : Integrate with Gabriel's authentication feature and fetch user data from datastore.
  // ex. private final static String username = entity.getproperty, etc...

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
    response.setContentType("text/html;");
    String jsonDate = gson.toJson(date.getMonth() + "/" + date.getDay() + "/" + (date.getYear()+1900));
    response.getWriter().println(jsonDate);
  }
}