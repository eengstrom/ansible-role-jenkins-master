# Conventions used

All scripts output success text;
Various conditions that indicate failure when using `jenins_script`:
 - ""            == script output empty, which should NEVER happen
 - "ERROR"       == script completed, but internally decided something was off
 - "GroovyShell" == script probably threw an exception.

# Useful Pointers

Just a list of useful pointers I've found for Jenkins related Groovy scripts:

  - Set location URL and email address:
    https://gist.github.com/ivan-pinatti/dfcf85a4aaadc97104ba7fd91a65c320

  - Miscellaneous "Jenkins Script Console Scripts"
    https://github.com/samrocketman/jenkins-script-console-script"

  - General Ansible "integrated" scripts:
    https://github.com/hogarthj/jenkins_script_demo/tree/master/roles/jenkins/scripts

  - Mailer plug-in configuration
    https://github.com/Accenture/adop-jenkins/pull/17/files

  - Create user gist
    https://gist.github.com/hayderimran7/50cb1244cc1e856873a4

  - Cheat sheet, with examples
    https://cheatsheet.dennyzhang.com/cheatsheet-jenkins-groovy-a4

You can use Role Role Strategy plugin and configure finer permissions as shown in this script -> https://github.com/cloudbees/jenkins-scripts/blob/master/RBAC_Example.groovy

# Examples

## Start Quiet Down:

    import jenkins.model.*;
    // start in a state where no builds are started
    Jenkins.instance.doQuietDown();
