package com.hipstercoffee
package application

import org.scalatest.{WordSpec, MustMatchers}

class BeansCatalogueSpec extends WordSpec with MustMatchers {
  "findByIdentity" must {
    "return None if no beans with the given identity exist" in {
      BeansCatalogue findByIdentity "foobar" mustEqual None
    }
  }
}