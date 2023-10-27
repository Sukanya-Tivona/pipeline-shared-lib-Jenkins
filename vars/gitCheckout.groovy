#!/usr/bin/env groovy
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
