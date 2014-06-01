package com.hipstercoffee

object Server extends App {
  unfiltered.jetty.Http.local(8080).plan(web.WebApi).run()
}
