import org.scalatest.{Matchers, FlatSpec}

class BasicSendAndReceive extends FlatSpec with Matchers {

  "basic publish" should "send a message and basic receive should consume it" in {
    val publisher = new BasicPublisher
    val receiver = new BasicReceiver

    publisher.publish("Hello World!")

    receiver.getMessage should equal("Hello World!")
  }
}
