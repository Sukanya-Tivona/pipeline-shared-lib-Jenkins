def call() {
    pipeline{
      agent any
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
    }
    }
}
        
     
