package com.hipstercoffee

object Server extends App {
  unfiltered.jetty.Http.anylocal.plan(web.WebApi).run()
}
