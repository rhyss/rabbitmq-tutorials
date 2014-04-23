import com.rabbitmq.client.ConnectionFactory

val EXCHANGE_NAME = "direct_logs"

val factory = new ConnectionFactory()
factory.setUsername("guest")
factory.setPassword("guest")
factory.setHost("33.33.33.10")
val connection = factory.newConnection()
val channel = connection.createChannel()

channel.exchangeDeclare(EXCHANGE_NAME, "direct")

def getSeverity = {
  println("Enter severity")
  readLine()
}

def getMessage: String = {
  println("Waiting for message")
  val message = readLine()
  if(message.trim.isEmpty) "Hello World!" else message
}

val severity = getSeverity


while (true) {
  val message = getMessage
  channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes)
  System.out.println(" [x] Sent '" + severity + "':'" + message + "'")
}

channel.close()
connection.close()
