import com.rabbitmq.client.{QueueingConsumer, ConnectionFactory}

class BasicReceiver {
  val queueName = "Hello"
  val factory = new ConnectionFactory()
  factory.setHost("33.33.33.10")
  factory.setUsername("guest")
  factory.setPassword("guest")
  val connection = factory.newConnection()
  val channel = connection.createChannel()

  channel.queueDeclare(queueName, false, false, false, null)

  val consumer = new QueueingConsumer(channel)
  channel.basicConsume(queueName, true, consumer)

  def getMessage : String = {
    println(" [*] Waiting for messages.")
    val delivery = consumer.nextDelivery()
    val message = new String(delivery.getBody)
    println(" [x] Received '" + message + "'")
    message
  }

  def close {
    channel.close()
    connection.close()
  }
}
