import com.rabbitmq.client.ConnectionFactory

val exchangeName = "logs"

val factory = new ConnectionFactory()
factory.setHost("33.33.33.10")
factory.setUsername("guest")
factory.setPassword("guest")
val connection = factory.newConnection()
val channel = connection.createChannel()

channel.exchangeDeclare(exchangeName, "fanout")

def getMessage: String = {
  println("Waiting for message")
  val message = readLine()
  if(message.trim.isEmpty) "Hello World!" else message
}

while(true){
  val message = getMessage
  channel.basicPublish(exchangeName, "", null, message.getBytes)
  System.out.println(" [x] Sent '" + message + "'")
}

channel.close()
connection.close()
