import org.scalatest.{Matchers, FlatSpec}
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class WorkQueueTest extends FlatSpec with Matchers {

  "Workers" should "complete work queue" in {
    val publisher = new BasicPublisher
    val worker1 = new Worker("1")
    val worker2 = new Worker("2")
    val worker3 = new Worker("3")

    publisher.publish("First message.")
    publisher.publish("Second message..")
    publisher.publish("Third message...")
    publisher.publish("Fourth message....")
    publisher.publish("Fifth message.....")


      val f: Future[List[String]] =
        for {r1 <- future {worker1.doWork}
             r2 <- future {worker2.doWork}
             r3 <- future {worker3.doWork}
             r4 <- future {worker1.doWork}
             r5 <- future {worker2.doWork}
        } yield r1 :: r2 :: r3 :: r4 :: r5 :: Nil

    val result = Await.result(f, 20 seconds)

    result should contain("First message.")
    result should contain("Second message..")
    result should contain("Third message...")
    result should contain("Fourth message....")
    result should contain("Fifth message.....")
  }
}
