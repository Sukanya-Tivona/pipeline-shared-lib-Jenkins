//def call( String IMAGEVERSION, String projectname,  String AWS_DEFAULT_REGION,  String AWS_ACCOUNT_ID ){
def call(Map config = [;]) {
    stages 
    {
       stage('Checkout') 
       {
       steps {
           sh '''
               git clone -b main https://$PAT@github.com/Observe-Life-AI/ol-services-node.git
               git clone -b feature/OLMS-54-initial-node-container https://$PAT@github.com/Observe-Life-AI/ol-container-images-node.git
           '''

            //sh 'git clone -b main ${APPLICATION_URL} '
               }
       }
        
     
       stage('AWS configure and ecr login ') 
       {
       steps { sh '''
              aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID
              aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY
              aws configure set default.region $config.AWS_DEFAULT_REGION
              aws ecr get-login-password --region ${config.AWS_DEFAULT_REGION} | 
              docker login --username AWS --password-stdin ${config.AWS_ACCOUNT_ID}.dkr.ecr.${config.AWS_DEFAULT_REGION}.amazonaws.com
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
       {
              def IMAGENAMES = ['data-read', 'data-write', 'timelines']
              for (IMAGENAME in IMAGENAMES) 
              {  
                sh "cp $HOME/workspace/$config.projectname/ol-container-images-node/Dockerfile $HOME/workspace/$config.projectname/ol-services-node/ol-node-api-$IMAGENAME"
                  dir("$HOME/workspace/$config.projectname/ol-services-node/ol-node-api-${IMAGENAME}")
                  {    
                      sh "docker build -t ${REPOSITORY_URI}/${IMAGENAME}:${config.IMAGEVERSION} ."
                    // sh "docker tag ${IMAGENAME} ${REPOSITORY_URI}:${IMAGEVERSION}"
                      sh "docker push ${REPOSITORY_URI}/${IMAGENAME}:${config.IMAGEVERSION} "
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

	  

     
	  




