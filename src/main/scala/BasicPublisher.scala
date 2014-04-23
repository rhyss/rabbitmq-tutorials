import com.rabbitmq.client.ConnectionFactory

class BasicPublisher {
  val factory = new ConnectionFactory()
  factory.setHost("33.33.33.10")
  factory.setUsername("guest")
  factory.setPassword("guest")
  val connection = factory.newConnection()
  val channel = connection.createChannel()
  val queueName = "Hello"

  channel.queueDeclare(queueName, false, false, false, null)

  def publish(message: String) {
    channel.basicPublish("", queueName, null, message.getBytes)
    println(" [x] Sent '" + message + "'")
  }

  def close {
    channel.close()
    connection.close()
  }
}
