#!/usr/bin/env groovy
// def call(String name ) {
//   echo "Hello, ${name},how are you ?"
// }
// shared-library/vars/cloneGitRepo.groovy

def call(Map config) {
    def gitURL = config.gitURL
    def branch = config.branch ?: 'master'
    def destination = config.destination ?: '.'

    checkout([$class: 'GitSCM',
        branches: [[name: "*/${branch}"]],
        userRemoteConfigs: [[url: gitURL]],
        doGenerateSubmoduleConfigurations: false,
        extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: destination]]])
}
