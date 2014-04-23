import com.rabbitmq.client.ConnectionFactory

val factory = new ConnectionFactory()
factory.setHost("33.33.33.10")
factory.setUsername("guest")
factory.setPassword("guest")
val connection = factory.newConnection()
val channel = connection.createChannel()
val queueName = "Hello"

channel.queueDeclare(queueName, true, false, false, null)

while(true){
  println("Waiting for input")
  def getMessage: String = {
    val message = readLine()
    if(message.trim.isEmpty) "Hello World!" else message
  }
  val message = getMessage
  channel.basicPublish("", queueName, null, message.getBytes)
  println(" [x] Sent '" + message + "'")
}

channel.close()
connection.close()


