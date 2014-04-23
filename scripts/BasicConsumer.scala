import com.rabbitmq.client.{QueueingConsumer, ConnectionFactory}

val queueName = "Hello"
val factory = new ConnectionFactory()
factory.setHost("33.33.33.10")
factory.setUsername("guest")
factory.setPassword("guest")
val connection = factory.newConnection()
val channel = connection.createChannel()

channel.queueDeclare(queueName, true, false, false, null)
channel.basicQos(1)

val consumer = new QueueingConsumer(channel)
channel.basicConsume(queueName, false, consumer)

while(true) {
  println(" [*] Waiting for messages.")
  val delivery = consumer.nextDelivery()
  val message = new String(delivery.getBody)
  println(" [x] Received '" + message + "'")
  doWork(message)
  channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false)
  println(" [x] Done!")
}

def doWork(task : String) {
  for (char <- task.toCharArray()) {
    if (char == '.') Thread.sleep(1000);
  }
}