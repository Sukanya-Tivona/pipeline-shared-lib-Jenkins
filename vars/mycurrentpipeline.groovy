def call(Map pipelineParams = [:]) {
    pipeline {
        agent any

        environment {
            AWS_ACCESS_KEY_ID     = credentials(pipelineParams.awsAccessKeyId)
            AWS_SECRET_ACCESS_KEY = credentials(pipelineParams.awsSecretAccessKey)
            
           // PAT = credentials(pipelineParams.pat).toString()
        }

        parameters {
            string(name: 'IMAGEVERSION', defaultValue: '1.0', description: 'Version number to build for')
            string(name: 'projectname', defaultValue: 'yourpipelinename', description: 'name of the pipeline')
            string(name: 'AWS_DEFAULT_REGION', defaultValue: 'us-west-2', description: 'region name')
            string(name: 'AWS_ACCOUNT_ID', defaultValue: '562922379100', description: 'aws account id')
	   string(name: 'PAT', defaultValue: '562922379100', description: 'give the value of pat')
	   //credentials(name:'aws_pratice', description:'my aws credentials', required:true)					
    }							
	
        
    stages
    {
       stage('Checkout')
       {
       steps {
          sh '''
              git clone -b main https://$PAT@github.com/Observe-Life-AI/ol-services-node.git
              git clone -b feature/OLMS-54-initial-node-container https://$PAT@github.com/Observe-Life-AI/ol-container-images-node.git
          '''
               }
       }
       stage('AWS configure and ecr login ')
       {
       steps { sh '''
              aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID
              aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY
              aws configure set default.region $AWS_DEFAULT_REGION
              aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com
               '''
              }
       }
       //locally run
       //sudo chmod 777 /var/run/docker.sock
       stage('Build and Push Docker Images')
       {
       steps
       {
       script
       {      def  REPOSITORY_URI = "${pipelineParams.awsAccountId}.dkr.ecr.${pipelineParams.awsRegion}.amazonaws.com"
              def IMAGENAMES = ['data-read', 'data-write', 'timelines']
              for (IMAGENAME in IMAGENAMES)
              {
                sh "cp $HOME/workspace/$projectname/ol-container-images-node/Dockerfile $HOME/workspace/$projectname/ol-services-node/ol-node-api-$IMAGENAME"
                  dir("$HOME/workspace/$projectname/ol-services-node/ol-node-api-${IMAGENAME}")
                  {
                      sh "docker build -t ${REPOSITORY_URI}/${IMAGENAME}:${IMAGEVERSION} ."
                    // sh "docker tag ${IMAGENAME} ${REPOSITORY_URI}:${IMAGEVERSION}"
                      sh "docker push ${REPOSITORY_URI}/${IMAGENAME}:${IMAGEVERSION} "
                  }
              }
       }
       }
       }
    }
   //post build cleanup
    post {
        cleanup {
            echo "Clean up in post work space"
            cleanWs()
        }
    
    }
}
}

