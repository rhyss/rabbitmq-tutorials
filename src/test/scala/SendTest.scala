import com.rabbitmq.client.AMQP.{Channel, Connection}
import com.rabbitmq.client.ConnectionFactory
import org.scalatest.FlatSpec

class SendTest extends FlatSpec {

  "Basic publish" should "send message" in {
    val factory = new ConnectionFactory()
    factory.setHost("33.33.33.10")
    factory.setUsername("guest")
    factory.setPassword("guest")
    val connection = factory.newConnection()
    val channel = connection.createChannel()
    val queueName = "Hello"

    channel.queueDeclare(queueName, false, false, false, null)
    val message = "Hello World!"
    channel.basicPublish("", queueName, null, message.getBytes())
    println(" [x] Sent '" + message + "'")

    channel.close()
    connection.close()
  }

}
