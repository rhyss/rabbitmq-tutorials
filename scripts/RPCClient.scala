import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.{QueueingConsumer, ConnectionFactory}
import java.util.UUID
import scala.util.Try

class RPCClient {

  val requestQueueName = "rpc_queue"

  val factory = new ConnectionFactory()
  factory.setHost("33.33.33.10")
  factory.setUsername("guest")
  factory.setPassword("guest")
  val connection = factory.newConnection()
  val channel = connection.createChannel()

  val replyQueueName = channel.queueDeclare().getQueue
  val consumer = new QueueingConsumer(channel)
  channel.basicConsume(replyQueueName, true, consumer)

  def call(message : String) : String = {
    var response : String = null
    val corrId = UUID.randomUUID().toString
    val props = new BasicProperties
    .Builder()
    .correlationId(corrId)
    .replyTo(replyQueueName)
    .build()

    channel.basicPublish("", requestQueueName, props, message.getBytes)

    var waiting = true
    while (waiting) {
      val delivery = consumer.nextDelivery()
      println(s"Got message with corrId '${delivery.getProperties.getCorrelationId} expecting '$corrId'")
      if (delivery.getProperties.getCorrelationId.equals(corrId)) {
        response = new String(delivery.getBody,"UTF-8")
        waiting = false
      }
    }

    response
  }

  def close() = {
    connection.close()
  }
}


var fibonacciRpc : RPCClient = null
var response : String = null
Try {
  fibonacciRpc = new RPCClient()
  System.out.println(" [x] Requesting fib(30)")
  response = fibonacciRpc.call("30")
  System.out.println(s" [.] Got '$response'")
} recover {
  case e : Exception => e.printStackTrace()
  }

if (fibonacciRpc!= null) {
  Try {
    fibonacciRpc.close()
  } recover {
    case e: Exception => e.printStackTrace()
  }
}

