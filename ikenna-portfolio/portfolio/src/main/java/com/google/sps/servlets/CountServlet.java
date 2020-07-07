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

import com.google.gson.Gson;
import com.google.sps.data.Metadata;
import com.google.sps.data.Metadata.Search;
import com.google.sps.database.CommentDatabase;
import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.*; 

/** Servlet that returns JSON content. */
@WebServlet("/count")
public class CountServlet extends HttpServlet {

  private int count;
  private int page;
  private Search search;
  
  private CommentDatabase database;
  private Metadata metadata;

  @Override
  public void init() {
    database = new CommentDatabase();
    metadata = new Metadata();
    count = metadata.getCount();
    page = metadata.getPage();
    search = metadata.getSearch();
    metadata.setMaxPages(database.getMaxPages(count));
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json;");
    response.getWriter().println(getJson());
  }

  
  /**
   * This method creates metadata from the comments container
   * that describes how the comments in the comments container
   * should be displayed.
   * @param request 
   * @param response 
  */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String countString = request.getParameter("count");
    if(!countString.equals(""))
      count = Integer.parseInt(countString);
    String movePage = request.getParameter("move-page");
    if(movePage != null) {
      if(movePage.equals("left") && page != 0) {
        page--;
      }
      else if(movePage.equals("right") && page != database.getMaxPages(count) - 1) {
        page++;
      }
    }
    String filter = request.getParameter("filter");
    if(filter != null) {
      for(Search s : Search.values()) {
        if(s.name().equalsIgnoreCase(filter)) {
          search = s;
        }
      }
    }
    Metadata metadata = new Metadata(count, page, database.getMaxPages(count), search);
    this.metadata = new Metadata(metadata);
    response.sendRedirect("/index.html#comments-sect");
  }

  private String getJson() {
    Gson gson = new Gson();
    String json = gson.toJson(metadata);
    return json;
  }
}
