def call(Map pipelineParams) {
    pipeline {
        agent any

        environment {
            AWS_ACCESS_KEY_ID     = credentials(pipelineParams.awsAccessKeyId).toString()
            AWS_SECRET_ACCESS_KEY = credentials(pipelineParams.awsAccessKeyId).toString()
            REPOSITORY_URI = "${pipelineParams.awsAccountId}.dkr.ecr.${pipelineParams.awsRegion}.amazonaws.com"
            PAT = credentials(pipelineParams.pat).toString()
        }

        parameters {
            string(name: 'IMAGEVERSION', defaultValue: '1.0', description: 'Version number to build for')
            string(name: 'projectname', defaultValue: 'yourpipelinename', description: 'name of the pipeline')
            string(name: 'AWS_DEFAULT_REGION', defaultValue: 'us-west-2', description: 'region name')
            string(name: 'AWS_ACCOUNT_ID', defaultValue: '562922379100', description: 'aws account id')
        }

        stages {
            // stage('Checkout') {
            //     steps {
            //         script {
            //             checkoutGit(pipelineParams)
            //         }
            //     }
            // }
            stage('Checkout')
             {
       steps {
          sh '''
              git clone -b main https://$PAT@github.com/Observe-Life-AI/ol-services-node.git
              git clone -b feature/OLMS-54-initial-node-container https://$PAT@github.com/Observe-Life-AI/ol-container-images-node.git
          '''
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
