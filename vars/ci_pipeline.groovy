//def call( String IMAGEVERSION, String projectname,  String AWS_DEFAULT_REGION,  String AWS_ACCOUNT_ID,
//AWS_ACCESS_KEY_ID = env.AWS_ACCESS_KEY_ID ,AWS_SECRET_ACCESS_KEY = env.AWS_SECRET_ACCESS_KEY,PAT = env.PAT ){


def call(Map params = [:]) {
    environment
     {
      AWS_ACCESS_KEY_ID     = credentials(params.aws_pratice)
      AWS_SECRET_ACCESS_KEY = credentials(params.aws_pratice)
      PAT = credentials(params.PAT)
      REPOSITORY_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"
      //IMAGEVERSION = '7.0'
      //projectname = 'ci_shared_lib_oct_25'
      //AWS_DEFAULT_REGION = 'us-west-1'
      
     //AWS_ACCOUNT_ID :'562922379100'

      //APPLICATION_REPO_URL = ""
    
      }

    agent any
      parameters {							
        string(name: 'IMAGEVERSION', defaultValue: '1.0', description: 'Version number to build for')
        string(name: 'projectname', defaultValue: 'yourpipelinename', description: 'name of the pipeline')
        string(name: 'AWS_DEFAULT_REGION', defaultValue: 'us-west-2', description: 'region name')
        string(name: 'AWS_ACCOUNT_ID', defaultValue: '562922379100', description: 'aws account id')
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

            //sh 'git clone -b main ${APPLICATION_URL} '
            }
       }
        
     
       stage('AWS configure and ecr login ') 
       {
       steps{ sh '''
              aws configure set aws_access_key_id $params.AWS_ACCESS_KEY_ID
              aws configure set aws_secret_access_key $params.AWS_SECRET_ACCESS_KEY
              aws configure set default.region $params.AWS_DEFAULT_REGION
              aws ecr get-login-password --region ${params.AWS_DEFAULT_REGION} | 
              docker login --username AWS --password-stdin ${params.AWS_ACCOUNT_ID}.dkr.ecr.${params.AWS_DEFAULT_REGION}.amazonaws.com
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
                sh "cp $HOME/workspace/$params.projectname/ol-container-images-node/Dockerfile $HOME/workspace/$params.projectname/ol-services-node/ol-node-api-$IMAGENAME"
                  dir("$HOME/workspace/$params.projectname/ol-services-node/ol-node-api-${IMAGENAME}")
                  {    
                      sh "docker build -t ${REPOSITORY_URI}/${IMAGENAME}:${params.IMAGEVERSION} ."
                    // sh "docker tag ${IMAGENAME} ${REPOSITORY_URI}:${IMAGEVERSION}"
                      sh "docker push ${REPOSITORY_URI}/${IMAGENAME}:${params.IMAGEVERSION} "
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
    


	  

     
	  




