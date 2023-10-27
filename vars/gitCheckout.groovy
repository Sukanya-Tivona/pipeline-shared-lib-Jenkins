#!/usr/bin/env groovy
def call(Map config) {
    def credentialsId = config.credentialsId
    def repositoryUrl = config.repositoryUrl
    def branch = config.branch ?: 'main'
    def destination = config.destination ?: '.'

    script {
        withCredentials([string(credentialsId: credentialsId)]) {
            checkout([$class: 'GitSCM', 
                userRemoteConfigs: [[url: repositoryUrl]],
                branches: [[name: branch]],
                extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: destination]]
            ])
        }
    }
}
