object SimplePlayRunHook {
  import java.net.InetSocketAddress
  import play.PlayRunHook
  import sbt._
  import java.io.File

  def apply(base: File): PlayRunHook = {  
    println(s"+++ in the apply() method")
    object SimplePlayRunHookProcess extends PlayRunHook {
      override def beforeStarted(): Unit = {
        println(s"++++ simplePlayRunHook.beforeStarted: $base")
      }
                     
      override def afterStarted(address: InetSocketAddress): Unit = {
        println(s"++++ simplePlayRunHook.afterStarted: $address, $base")
      }

      override def afterStopped(): Unit = {
        println("++++ simplePlayRunHook.afterStopped")
      }

    }
    SimplePlayRunHookProcess
  }
}
