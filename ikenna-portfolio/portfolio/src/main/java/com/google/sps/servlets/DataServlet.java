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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import com.google.sps.data.Metadata;
import com.google.sps.data.Metadata.Sort;
import com.google.sps.database.CommentDatabase;
import java.io.IOException;
import java.lang.*;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that creates comments from form data. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    CommentDatabase database = new CommentDatabase();
    List<Comment> comments = new ArrayList<>();
    HttpSession session = request.getSession();
    Metadata metadata = (Metadata) session.getAttribute("metadata");
    if(metadata == null) {
      metadata = new Metadata();
    }

    QueryResultList<Entity> results = (QueryResultList<Entity>) database.getContents(metadata.getSort(), metadata.getCount(), metadata.getPage());  
    Iterator r = results.iterator();
    while(r.hasNext()) {
      Entity entity = (Entity) r.next();
      long id = entity.getKey().getId();
      String name = (String) entity.getProperty("name");
      String text = (String) entity.getProperty("text");
      long timestamp = (long) entity.getProperty("timestamp");
      Comment comment = new Comment(name, text, timestamp);
      comment.setId(id);
      comments.add(comment);
    }
    
    if(comments.isEmpty()) {
      comments.add(new Comment("", "", 0));
    }
    
    response.setContentType("application/json;");
    response.getWriter().println(getJson(comments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    CommentDatabase database = new CommentDatabase();

    if(request.getParameter("count") != null) {
      HttpSession session = request.getSession();
      int count = Integer.parseInt(request.getParameter("count"));
      int page = Integer.parseInt(request.getParameter("page"));
      String sortLabel = request.getParameter("sortLabel");
      session.setAttribute("metadata", new Metadata(count, page, database.getMaxPages(count), Sort.valueOf(sortLabel)));
    } 
    else {
      Comment comment = generateComment(request);
      comment.setId(database.storeEntity(comment));
      response.sendRedirect("/index.html#comments-sect");
    }
  }

  private Comment generateComment(HttpServletRequest request) {
    String name = request.getParameter("name-box");
    String text = request.getParameter("text-box");
    long timestamp = System.currentTimeMillis();
    Comment comment = new Comment(name, text, timestamp);
    return comment;
  }
  
  private String getJson(List<Comment> comments) {
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    return json;
  }
}
