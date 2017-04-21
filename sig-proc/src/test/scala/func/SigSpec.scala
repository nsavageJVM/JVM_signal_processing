package func

import org.scalatest._

/**
  * Created by ubu on 21.04.17.
  * val data = Entrypoint.processData("/ecg1.csv")
  */
class SigSpec extends FlatSpec with Matchers {

  "A Entrypoint" should "produce some data" in {

    Entrypoint.processData("/ecg1.csv")
    println(Entrypoint.rawData.size)
    assert( Entrypoint.rawData.size > 10)
  }



}
