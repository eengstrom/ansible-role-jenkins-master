#!groovy
import hudson.model.RestartListener

def instance = Jenkins.instance

def timeout_seconds = 30
def max_iterations = 10

instance.doQuietDown();
if (! instance.isQuietingDown()) {
  return "ERROR: Shutdown mode not enabled; Jenkins restart aborted."
}

while (true) {
  if (max_iterations-- <= 0) {
    return "ERROR: Waited too many iterations for shutdown; Jenkins restart aborted."
  } else if (RestartListener.isAllReady()) {
    instance.restart()
    println "All jenkins jobs are idle; Jenkins restart initiated"
    sleep(1000) // sleep for a second to let it start to shutdown before returning
    break
  } else {
    println "WARN: Not all Jenkins jobs are idle; sleeping ${timeout_seconds} ({$max_iterations})..."
    sleep(timeout_seconds*1000)
  }
}
