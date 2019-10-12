#!groovy
import jenkins.model.Jenkins
// import jenkins.model.*
// import hudson.model.*
// import hudson.tasks.*
// import hudson.tools.*
// import jenkins.model.${conf_subsystem}
// import java.lang.reflect.Field
// import java.lang.reflect.Modifier

def changes = []
def jenkins = Jenkins.getInstance()
def config = jenkins

if ('${conf_subsystem}' != 'Jenkins') {
  // config = ${conf_subsystem}.get()
  config = jenkins.getDescriptor('${conf_subsystem}')
}

if (config.get${conf_key}() != '${conf_value}') {
  config.set${conf_key}('${conf_value}')
  changes << "${conf_subsystem}:${conf_key} -> ${conf_value}"
}

// report if any changes
if (changes.size() >= 1) {
  jenkins.save()
  return "Changes " + changes.size() + " = [ " + changes.join(', ') + " ]"
} else {
  return "No changes"
}
