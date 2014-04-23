class Worker(name: String) {

  val receiver = new BasicReceiver

  override def toString = name

  def doWork : String = {
    val task = receiver.getMessage
    for (char <- task.toCharArray()) {
      if (char == '.') Thread.sleep(1000);
    }
    println(s"Worker [${this.toString}] finished task [$task]")
   task
  }
}
