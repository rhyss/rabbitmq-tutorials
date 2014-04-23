import com.rabbitmq.client.{QueueingConsumer, ConnectionFactory}

val exchangeName = "logs";

val factory = new ConnectionFactory();
factory.setHost("33.33.33.10");
factory.setUsername("guest")
factory.setPassword("guest")
val connection = factory.newConnection();
val channel = connection.createChannel();

channel.exchangeDeclare(exchangeName, "fanout");
val queueName = channel.queueDeclare().getQueue();
channel.queueBind(queueName, exchangeName, "");

System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

val consumer = new QueueingConsumer(channel);
channel.basicConsume(queueName, true, consumer);

while (true) {
  val delivery = consumer.nextDelivery();
  val message = new String(delivery.getBody());

  System.out.println(" [x] Received '" + message + "'");
}