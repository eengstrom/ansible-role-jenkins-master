// Experiments - snippets for groovy script dialog.
// In case someone references this, just fail
FAIL

import hudson.tasks.*
import java.lang.reflect.Field
import java.lang.reflect.Modifier
def jenkins = Jenkins.getInstance()

// def loc = jenk.getDescriptor("JenkinsLocationConfiguration")
// def mail = jenk.getDescriptor("hudson.tasks.Mailer")
// mail.setSmtpHost("foo.bar.com")
// mail.getSmtpHost()

def config = jenkins.getDescriptor("hudson.scm.SubversionSCM")
println(config.getWorkspaceFormat())

field = config.getClass().getDeclaredField("workspaceFormat")

def required_format = 8 //31 is SVN 1.8 WF.
// if (config.getWorkspaceFormat() != required_format) {
if (config.get(config, 'workspaceFormat') != required_format) {
  field.setAccessible(true) //Private field -- make it public
  field.set(config, required_format)
  field.setAccessible(false) //And make it private again
}
config.getWorkspaceFormat()



---

#!groovy
import jenkins.model.Jenkins
import jenkins.model.*
import hudson.model.*
import hudson.tasks.*
import hudson.tools.*
// import jenkins.model.${conf_subsystem}
import java.lang.reflect.Field
import java.lang.reflect.Modifier

// https://stackoverflow.com/questions/49259147/how-do-i-determine-if-a-field-is-private-or-protected-using-reflection-in-java?noredirect=1&lq=1


fail- this is old

def changes = []
def jenkins = Jenkins.getInstance()
def config = jenkins

if ('${conf_subsystem}' != 'Jenkins') {
  // config = ${conf_subsystem}.get()
  config = jenkins.getDescriptor('${conf_subsystem}')
}

// first, just try to use (templated) accessors
try {
  if (config.get${conf_key}() != '${conf_value}') {
    config.set${conf_key}('${conf_value}')
    changes << "${conf_subsystem}:${conf_key} -> ${conf_value}"
  }
} catch(Exception e1) {

  // Ok, that failed, so try using reflection.
  try {
    // get the field (via reflection), and any modifiers.
    def field = config.getClass().getDeclaredField('${conf_key}')
    int modifiers = field.getModifiers()

    // if it's private, hack around that...
    if (Modifier.isPrivate(modifiers)) {
      wasPrivate = true
      field.setAccessible(true) // hmmm... private field; make it public!
    }

    // Change the value of the field and record that change.
    if (field.get(config) != '${conf_value}') {
      field.set(config, '${conf_value}')
      changes << "${conf_subsystem}:${conf_key} -> ${conf_value}"
    }

    // unhack the accessible feature...
    if (wasPrivate) {
      field.setAccessible(false) // make field private again
    }
  } catch (Exception e2) {
    println("Exception: " + e2.toString());
    system.exit(1)
  }
}

/// report if any changes
if (changes.size() >= 1) {
  jenkins.save()
  return "Changes " + changes.size() + " = [ " + changes.join(', ') + " ]"
}
