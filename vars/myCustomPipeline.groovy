def call(Map pipelineParams) {
    pipeline {
        agent any

        environment {
            AWS_ACCESS_KEY_ID     = credentials(pipelineParams.awsAccessKeyId).toString()
            AWS_SECRET_ACCESS_KEY = credentials(pipelineParams.awsAccessKeyId).toString()
            REPOSITORY_URI = "${pipelineParams.awsAccountId}.dkr.ecr.${pipelineParams.awsRegion}.amazonaws.com"
        }

        parameters {
            string(name: 'IMAGEVERSION', defaultValue: '1.0', description: 'Version number to build for')
            string(name: 'projectname', defaultValue: 'yourpipelinename', description: 'name of the pipeline')
        }

        stages {
            stage('Checkout') {
                steps {
                    script {
                        checkoutGit(pipelineParams.pat)
                    }
                }
            }

            stage('AWS configure and ECR login') {
                steps {
                    awsConfigureAndEcrLogin(pipelineParams)
                }
            }

            stage('Build and Push Docker Images') {
                steps {
                    buildAndPushDockerImages(pipelineParams)
                }
            }
        }

        post {
            cleanup {
                echo "Clean up in post workspace"
                cleanWs()
            }
        }
    }
}
