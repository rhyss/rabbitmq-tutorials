import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.{QueueingConsumer, ConnectionFactory, Connection}
import scala.util.Try

val rpcQueueName = "rpc_queue"

def fib(n : Int) : Int = {
  println(s"Calculating fib($n)")
  n match {
    case 0 => 0
    case 1 => 1
    case i => fib(i - 1) + fib(i - 2)
  }
}

var connection : Connection = null

Try {
  val factory = new ConnectionFactory()
  factory.setHost("33.33.33.10")

  val connection = factory.newConnection()
  val channel = connection.createChannel()

  channel.queueDeclare(rpcQueueName, false, false, false, null)

  channel.basicQos(1)

  val consumer = new QueueingConsumer(channel)
  channel.basicConsume(rpcQueueName, false, consumer)

  System.out.println(" [x] Awaiting RPC requests")

  while (true) {
    var response : String = null

    val delivery = consumer.nextDelivery()

    val props = delivery.getProperties
    val replyProps = new BasicProperties
      .Builder()
      .correlationId(props.getCorrelationId)
      .build()

    Try {
      val message = new String(delivery.getBody,"UTF-8")
      val n = Integer.parseInt(message)

      System.out.println(s" [.] fib($message)")
      response = s"${fib(n)}"
    } recover {
      case e : Exception => e.printStackTrace()
      ""
    }

    println(s"Got response [$response]")

    channel.basicPublish( "", props.getReplyTo, replyProps, response.getBytes("UTF-8"))
    channel.basicAck(delivery.getEnvelope.getDeliveryTag, false)
  }
} recover {
  case e : Exception => e.printStackTrace()
}

if (connection != null) {
  Try{
    connection.close()
  } recover {
    case e : Exception => e.printStackTrace()
  }
}
