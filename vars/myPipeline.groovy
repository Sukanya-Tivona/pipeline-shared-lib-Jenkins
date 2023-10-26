// vars/myPipeline.groovy

def call(Map config) {
    pipeline {
        agent any
        parameters {
            string(name: 'IMAGEVERSION', defaultValue: config.IMAGEVERSION, description: 'Version number to build for')
            string(name: 'projectname', defaultValue: config.projectname, description: 'name of the pipeline')
            string(name: 'AWS_DEFAULT_REGION', defaultValue: config.AWS_DEFAULT_REGION, description: 'region name')
            string(name: 'AWS_ACCOUNT_ID', defaultValue: config.AWS_ACCOUNT_ID, description: 'aws account id')
        }

        stages {
            stage('Checkout') {
                steps {
                    withCredentials([usernamePassword(credentialsId: 'My_Github_Tivona_login', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                    sh '''
                        git clone -b main https://$PAT@github.com/Observe-Life-AI/ol-services-node.git
                        git clone -b feature/OLMS-54-initial-node-container https://$PAT@github.com/Observe-Life-AI/ol-container-images-node.git
                    '''
                }
            }

            stage('AWS configure and ECR login') {
                steps {
                    sh '''
                        aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID
                        aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY
                        aws configure set default.region $AWS_DEFAULT_REGION
                        aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com
                    '''
                }
            }

            stage('Build and Push Docker Images') {
                steps {
                    script {
                        def IMAGENAMES = ['data-read', 'data-write', 'timelines']
                        for (IMAGENAME in IMAGENAMES) {
                            sh "cp $HOME/workspace/${config.projectname}/ol-container-images-node/Dockerfile $HOME/workspace/${config.projectname}/ol-services-node/ol-node-api-$IMAGENAME"
                            dir("$HOME/workspace/${config.projectname}/ol-services-node/ol-node-api-${IMAGENAME}") {
                                sh "docker build -t ${REPOSITORY_URI}/${IMAGENAME}:${config.IMAGEVERSION} ."
                                sh "docker push ${REPOSITORY_URI}/${IMAGENAME}:${config.IMAGEVERSION}"
                            }
                        }
                    }
                }
            }
        }

        post {
            cleanup {
                echo "Clean up in post work space"
                cleanWs()
            }
        }
    }
}
