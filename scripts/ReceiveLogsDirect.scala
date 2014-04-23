import com.rabbitmq.client.{QueueingConsumer, ConnectionFactory}

val EXCHANGE_NAME = "direct_logs"


val factory = new ConnectionFactory()
factory.setUsername("guest")
factory.setPassword("guest")
factory.setHost("33.33.33.10")
val connection = factory.newConnection()
val channel = connection.createChannel()

channel.exchangeDeclare(EXCHANGE_NAME, "direct")
val queueName = channel.queueDeclare().getQueue


println("Enter logging levels")
val args: String = readLine()
if (args.length < 1){
  System.err.println("Usage: [info] [warning] [error]")
  System.exit(1)
}

for(severity <- args.split(" ")){
  channel.queueBind(queueName, EXCHANGE_NAME, severity)
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

