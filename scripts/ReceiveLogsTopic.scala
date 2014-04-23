import com.rabbitmq.client.{QueueingConsumer, ConnectionFactory}

val EXCHANGE_NAME = "topic_logs"

val factory = new ConnectionFactory()
factory.setHost("33.33.33.10")
val connection = factory.newConnection()
val channel = connection.createChannel()

channel.exchangeDeclare(EXCHANGE_NAME, "topic")
val queueName = channel.queueDeclare().getQueue

println("Enter binding keys")
val bindingKeys = readLine()

if (bindingKeys.length < 1){
  System.err.println("Usage: ReceiveLogsTopic [binding_key]...")
  System.exit(1)
}

  for(bindingKey <- bindingKeys.split(" ")){
    println(s"Binding: Queue[$queueName] Exchange[$EXCHANGE_NAME] key[$bindingKey]")
    channel.queueBind(queueName, EXCHANGE_NAME, bindingKey)
  }

  System.out.println(" [*] Waiting for messages. To exit press CTRL+C")

val consumer = new QueueingConsumer(channel)
channel.basicConsume(queueName, true, consumer)

while (true) {
  val delivery = consumer.nextDelivery()
  val message = new String(delivery.getBody)
  val routingKey = delivery.getEnvelope.getRoutingKey

  System.out.println(" [x] Received '" + routingKey + "':'" + message + "'")
}
