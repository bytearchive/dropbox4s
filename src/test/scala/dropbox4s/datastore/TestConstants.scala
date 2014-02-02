package dropbox4s.datastore

import java.util.Properties
import dropbox4s.datastore.auth.AccessToken

/**
 * @author mao.instantlife at gmail.com
 */
object TestConstants {
  val istream = this.getClass.getResourceAsStream("/test.properties")

  val prop = new Properties()
  prop.load(istream)

  val testUser1 = AccessToken(prop.getProperty("usertoken"))
}