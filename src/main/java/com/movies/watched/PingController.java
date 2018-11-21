package com.movies.watched;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

  public final static String PING_URL = "ping";

  @RequestMapping(value = PING_URL)
  public String ping() {
    return "pong";
  }
}

