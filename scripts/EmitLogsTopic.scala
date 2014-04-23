import com.rabbitmq.client.ConnectionFactory

val EXCHANGE_NAME = "topic_logs"

val factory = new ConnectionFactory()
factory.setHost("33.33.33.10")
factory.setUsername("guest")
factory.setPassword("guest")
val connection = factory.newConnection()
val channel = connection.createChannel()

channel.exchangeDeclare(EXCHANGE_NAME, "topic")

def getRouting = {
  println("Enter routing")
  readLine()
}
def getMessage = {
  println("Enter message")
  readLine()
}

while (true) {

  val routingKey = getRouting
  val message = getMessage

  channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes)
  System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'")
}

connection.close()
