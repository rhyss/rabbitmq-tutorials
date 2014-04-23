import com.rabbitmq.client.{QueueingConsumer, ConnectionFactory}
import org.scalatest.FlatSpec

class ReceiveTest extends FlatSpec {

  "receive" should "consume message" in {
    val queueName = "Hello"
    val factory = new ConnectionFactory()
    factory.setHost("33.33.33.10")
    factory.setUsername("guest")
    factory.setPassword("guest")
    val connection = factory.newConnection()
    val channel = connection.createChannel()

    channel.queueDeclare(queueName, false, false, false, null)
    println(" [*] Waiting for messages.")

    val consumer = new QueueingConsumer(channel)
    channel.basicConsume(queueName, true, consumer)

    val delivery = consumer.nextDelivery()
    val message = new String(delivery.getBody)
    println(" [x] Received '" + message + "'")
    channel.close()
    connection.close()
  }

}
