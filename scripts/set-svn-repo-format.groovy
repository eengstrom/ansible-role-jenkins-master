#!groovy
import jenkins.model.Jenkins
import java.lang.reflect.Field
import java.lang.reflect.Modifier

// Special routine to set the SVN repo version.
// With thanks to:
// - https://stackoverflow.com/questions/48667298/how-to-set-subversion-workspace-format-with-a-groovy-script-in-jenkins

def changes = []
def jenkins = Jenkins.getInstance()
def config = jenkins.getDescriptor("hudson.scm.SubversionSCM")

def svn_format = ${jenkins_svn_format}  // this value is templated in place.
if (config.getWorkspaceFormat() != svn_format) {
  field = config.getClass().getDeclaredField("workspaceFormat")
  field.setAccessible(true) // hack: Private field -- make it public
  field.set(config, svn_format)
  field.setAccessible(false) // unhack: make it private again
  changes << "Subversion SCM Workspace Format -> ${jenkins_svn_format}"
}

// save and report if any changes
if (changes.size() >= 1) {
  jenkins.save()
  return "Changes " + changes.size() + " = [ " + changes.join(', ') + " ]"
} else {
  return "No changes"
}
